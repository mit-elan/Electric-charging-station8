package org.example.managers;

import org.example.entities.*;
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


    public void createChargingSession(Account account, ChargingPoint chargingPoint, LocalDateTime dateTime) {
        Location location = locationManager.getLocationByChargingPoint(
                chargingPoint.getChargingPointID()
        );

        Tariff applicableTariff = location.readTariffAt(
                dateTime,
                chargingPoint.getMode()
        );

        if (applicableTariff == null) {
            throw new IllegalStateException("No valid tariff found for charging point");
        }

        if (account == null) {
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
                dateTime,
                chargingPoint
        );

        session.setAccount(account);
        session.setChargingPoint(chargingPoint);
        chargingPoint.updateOperatingStatus(OperatingStatus.OCCUPIED);
        session.setTariffAtStart(applicableTariff);
        chargingSessions.add(session);



    }

    public ChargingSession createChargingSessionWithId(
            String sessionId,
            Account account,
            ChargingPoint chargingPoint,
            LocalDateTime startTime
    )
    {
        Tariff applicableTariff = chargingPoint.getLocation().readTariffAt(
                LocalDateTime.now(),
                chargingPoint.getMode()
        );
        if (applicableTariff == null) {
            throw new IllegalStateException("No valid tariff found for charging point");
        }

        if (account == null) {
            throw new IllegalArgumentException("Account and Charging Point must exist");
        }

        // Acceptance Criteria: Check for sufficient credit
        if (account.getCredit() == null || account.getCredit().getAmount() <= 0) {
            throw new IllegalStateException("Insufficient credit to start session");
        }

        // Acceptance Criteria: Check Operating Status
        if (chargingPoint.getOperatingStatus() != OperatingStatus.IN_OPERATION_FREE) {
            throw new IllegalStateException("Charging Point is not available.");
        }

        ChargingSession session = new ChargingSession(
                sessionId,
                startTime,
                chargingPoint
        );

        session.setAccount(account);
        chargingPoint.updateOperatingStatus(OperatingStatus.OCCUPIED);
        chargingPoint.connectVehicle();
        session.setTariffAtStart(applicableTariff);

        chargingSessions.add(session);
        return session;
    }



    public void endChargingSession(String sessionId, double energyUsed, int duration) {
        ChargingSession session = chargingSessions.stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        ChargingPoint cp = session.getChargingPoint();
        // 🔹 SINGLE SOURCE OF TRUTH
        session.endSession(energyUsed, duration);

        // External effects
        cp.updateOperatingStatus(OperatingStatus.IN_OPERATION_FREE);
        cp.disconnectVehicle();

        // 🔹 subtract EXACT session price
        session.getAccount()
                .getCredit()
                .subtractCredit(session.getPrice());

        // 🔹 Invoice uses session price
        InvoiceManager.getInstance().createInvoiceFromSession(session);
    }


    public ChargingSession getChargingSessionById(String sessionId) {
        return chargingSessions.stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .orElse(null);
    }
}