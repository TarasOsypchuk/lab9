package ua.chnu.kpz.lab9.task2_pool;

import ua.chnu.kpz.lab9.domain.Shipment;

import java.util.List;

/** Клієнт пулу — кур'єр зі своїми відправленнями. */
public class Courier {

    private final String name;
    private final List<Shipment> parcels;
    private Van van;

    public Courier(String name, List<Shipment> parcels) {
        this.name = name;
        this.parcels = parcels;
    }

    public String getName() {
        return name;
    }

    public List<Shipment> getParcels() {
        return parcels;
    }

    public Van getVan() {
        return van;
    }

    void setVan(Van van) {
        this.van = van;
    }
}
