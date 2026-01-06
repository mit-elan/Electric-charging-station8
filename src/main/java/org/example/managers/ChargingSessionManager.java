package org.example.managers;

import org.example.entities.Account;
import org.example.entities.ChargingPoint;
import org.example.entities.ChargingSession;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.enums.OperatingStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChargingSessionManager {
    private static final ChargingSessionManager INSTANCE = new ChargingSessionManager();
    private final LocationManager locationManager = LocationManager.getInstance();

    private final List<ChargingSession> chargingSessions = new ArrayList<>();

    private ChargingSessionManager() {
    }

    public static ChargingSessionManager getInstance() {
        return INSTANCE;
    }

    public void clearChargingSessions() {
        chargingSessions.clear();
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
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                chargingPoint
        );

        session.setAccount(account);
        session.setChargingPoint(chargingPoint);
        chargingPoint.setOperatingStatus(OperatingStatus.OCCUPIED);
        chargingSessions.add(session);

    }

    public ChargingSession createChargingSessionWithId(String sessionId, Account account, ChargingPoint chargingPoint) {
        ChargingSession session = new ChargingSession(
                sessionId,
                LocalDateTime.now(),
                chargingPoint
        );

        session.setAccount(account);
        session.setChargingPoint(chargingPoint);
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
        double pricePerKwh = (cp.getMode() == Mode.AC)
                ? location.getAcPrice()
                : location.getDcPrice();

        // Set the final values
        session.setEnergyUsed(energyUsed);
        if (energyUsed <= 0) {
            throw new IllegalArgumentException("Energy used must be greater than 0");
        }

        session.setDuration(duration);
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }

        session.setPrice(energyUsed * pricePerKwh); // Use the Location's pricing!


        // Update status and credit
        cp.setOperatingStatus(OperatingStatus.IN_OPERATION_FREE);
        session.getAccount().getCredit().subtractCredit(session.getPrice());
        cp.disconnectVehicle();

        //Create Invoice
        InvoiceManager.getInstance().createInvoiceFromSession(session);
    }

    public ChargingSession getChargingSessionById(String sessionId) {
        return chargingSessions.stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .orElse(null);
    }
}