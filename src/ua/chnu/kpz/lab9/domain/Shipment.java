package ua.chnu.kpz.lab9.domain;

/**
 * Відправлення (сутність домену «Посилка+»):
 * номер накладної, відправник, отримувач, вага, статус.
 * Номер присвоює служба нумерації (завдання 3), у фургон вантажиться пулом (завдання 2).
 */
public class Shipment {

    public enum Status { CREATED, REGISTERED, IN_TRANSIT, DELIVERED }

    private String waybillNumber;
    private final String sender;
    private final String recipient;
    private final double weightKg;
    private Status status = Status.CREATED;

    public Shipment(String sender, String recipient, double weightKg) {
        this.sender = sender;
        this.recipient = recipient;
        this.weightKg = weightKg;
    }

    public void assignWaybillNumber(String number) {
        if (waybillNumber != null) {
            throw new IllegalStateException("Номер накладної вже присвоєно: " + waybillNumber);
        }
        this.waybillNumber = number;
        this.status = Status.REGISTERED;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public Status getStatus() {
        return status;
    }

    public String shortInfo() {
        return sender + " → " + recipient + ", " + weightKg + " кг";
    }

    @Override
    public String toString() {
        return waybillNumber != null ? "№" + waybillNumber : shortInfo();
    }
}
