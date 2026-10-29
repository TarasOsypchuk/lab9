package ua.chnu.kpz.lab9.task2_pool;

import ua.chnu.kpz.lab9.common.EventLog;
import ua.chnu.kpz.lab9.domain.Shipment;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable (об'єкт пулу) — службовий фургон.
 * Повна підготовка виконується один раз у конструкторі,
 * перед повторною видачею — лише швидке скидання стану reset().
 */
public class Van {

    private final String id;
    private String courier;
    private String waybillSheet;
    private final List<Shipment> cargo = new ArrayList<>();
    private int tripCount;

    /** Видимий лише в пакеті: створювати фургони може тільки пул. */
    Van(String id) {
        this.id = id;
        EventLog.expensive("Фургон " + id, "Повна підготовка: огляд, заправка, шляховий лист", 5);
        EventLog.log("Фургон " + id, "Готовий до виїзду");
    }

    void assignTo(String courier, List<Shipment> parcels) {
        this.courier = courier;
        this.waybillSheet = "ШЛ-" + id + "-" + (tripCount + 1);
        this.cargo.addAll(parcels);
        parcels.forEach(p -> p.setStatus(Shipment.Status.IN_TRANSIT));
        this.tripCount++;
    }

    /** Скидання стану перед поверненням у пул. */
    void reset() {
        courier = null;
        waybillSheet = null;
        cargo.clear();
    }

    public String getId() {
        return id;
    }

    public String getCourier() {
        return courier;
    }

    public List<Shipment> getCargo() {
        return cargo;
    }

    public int getTripCount() {
        return tripCount;
    }

    public String state() {
        return id + " {кур'єр=" + (courier == null ? "—" : courier)
                + ", шлях. лист=" + (waybillSheet == null ? "—" : waybillSheet)
                + ", вантаж=" + cargo + "}";
    }
}
