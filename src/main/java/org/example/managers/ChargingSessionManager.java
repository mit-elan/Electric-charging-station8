package org.example.managers;

import org.example.entities.Account;
import org.example.entities.ChargingPoint;
import org.example.entities.ChargingSession;
import org.example.entities.Location;
import org.example.enums.ChargingMode;
import org.example.enums.OperatingStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChargingSessionManager {
    private static final ChargingSessionManager INSTANCE = new ChargingSessionManager();
    private final LocationManager locationManager = LocationManager.getInstance();

    private final List<ChargingSession> chargingSessions = new ArrayList<>();
    private int ChargingSessionCounter = 0;


    private ChargingSessionManager() {
    }

    public static ChargingSessionManager getInstance() {
        return INSTANCE;
    }

    public void clearChargingSessions() {
        chargingSessions.clear();
        ChargingSessionCounter = 0;
    }

    public List<ChargingSession> getChargingSessions() {
        return chargingSessions;
    }

    public void createChargingSession(Account account, ChargingPoint chargingPoint) {

        if (account == null || chargingPoint == null) {
            throw new IllegalArgumentException("Account and Charging Point must exist");
        }

        // Acceptance Criteria: Check for sufficient credit
        if (account.getCredit().getAmount() <= 0) {
            throw new IllegalStateException("Insufficient credit to start session");
        }

        // Acceptance Criteria: Check Operating Status
        if (chargingPoint.getOperatingStatus() != OperatingStatus.IN_OPERATION_FREE) {
            throw new IllegalStateException("Charging Point is not available.");
        }

        // Acceptance Criteria: Physical connection
        if (!chargingPoint.isPhysicallyConnected()) {
            throw new IllegalStateException("Vehicle is not physically connected.");
        }

        ChargingSession session = new ChargingSession(
                "CS-" + (++ChargingSessionCounter),
                LocalDateTime.now(),
                chargingPoint
        );

        session.setAccount(account);
        session.setChargingPoint(chargingPoint);
        chargingPoint.setOperatingStatus(OperatingStatus.OCCUPIED);
        chargingSessions.add(session);

    }

    public ChargingSession createChargingSessionWithId(
            String sessionId,
            Account account,
            ChargingPoint chargingPoint,
            LocalDateTime startTime
    ) {
        ChargingSession session = new ChargingSession(
                sessionId,
                startTime,
                chargingPoint
        );

        session.setAccount(account);
        chargingPoint.setOperatingStatus(OperatingStatus.OCCUPIED);
        chargingPoint.connectVehicle();

        chargingSessions.add(session);
        return session;
    }



    public void endChargingSession(String sessionId, double energyUsed, int duration) {
        ChargingSession session = chargingSessions.stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        ChargingPoint cp = session.getChargingPoint();
        Location location = locationManager.getLocationByChargingPoint(session.getChargingPointID());

        // Determine price based on Mode
        double pricePerKwh = (cp.getMode() == ChargingMode.AC)
                ? location.getAcPrice()
                : location.getDcPrice();

        double price = energyUsed * pricePerKwh;

        //SINGLE LINE that ends the session
        session.endSession(energyUsed, duration, price);

        // External effects
        cp.setOperatingStatus(OperatingStatus.IN_OPERATION_FREE);
        cp.disconnectVehicle();

        session.getAccount().getCredit().subtractCredit(price);

        InvoiceManager.getInstance().createInvoiceFromSession(session);

    }

    public ChargingSession getChargingSessionById(String sessionId) {
        return chargingSessions.stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .orElse(null);
    }
}