package ua.chnu.kpz.lab9.task1_prototype;

import ua.chnu.kpz.lab9.common.EventLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ConcretePrototype — маршрут доставки.
 * copy() виконує ГЛИБОКЕ копіювання: нові списки, нова мапа і нові вкладені об'єкти.
 */
public class DeliveryRoute implements RoutePrototype {

    private final String name;
    private String date;
    private String courier;
    private final List<Stop> stops;
    private final Map<String, Double> segmentTariffs; // ділянка -> тариф, грн
    private final WeightLimit weightLimit;
    private final List<String> warehouseContacts;   // String незмінний — копіювати список достатньо

    /** «Дорога» побудова еталона з нуля. */
    public DeliveryRoute(String name, List<String> points) {
        String src = "Маршрут " + name;
        this.name = name;
        this.date = "—";
        this.courier = "—";
        this.stops = new ArrayList<>();
        this.segmentTariffs = new LinkedHashMap<>();
        this.warehouseContacts = new ArrayList<>();

        EventLog.expensive(src, "Побудова послідовності точок і графіка", 4);
        int hour = 9;
        for (String p : points) {
            stops.add(new Stop(p, String.format("%02d:00", hour++)));
        }
        EventLog.expensive(src, "Розрахунок тарифів на ділянках", 3);
        for (int i = 0; i < points.size() - 1; i++) {
            segmentTariffs.put(points.get(i) + "→" + points.get(i + 1), 120.0 + 35 * i);
        }
        EventLog.expensive(src, "Завантаження обмежень за вагою і контактів складів", 3);
        this.weightLimit = new WeightLimit(800, 30);
        warehouseContacts.add("Склад Чернівці: +380 37 200-00-01");
        warehouseContacts.add("Склад Хотин: +380 37 312-00-02");
        EventLog.log(src, "Еталонний маршрут побудовано");
    }

    /** Копіювальний конструктор — основа copy(). Нічого не завантажує. */
    private DeliveryRoute(DeliveryRoute other) {
        this.name = other.name;
        this.date = other.date;
        this.courier = other.courier;
        this.stops = new ArrayList<>();
        for (Stop s : other.stops) {
            this.stops.add(new Stop(s));
        }
        this.segmentTariffs = new LinkedHashMap<>(other.segmentTariffs);
        this.weightLimit = new WeightLimit(other.weightLimit);
        this.warehouseContacts = new ArrayList<>(other.warehouseContacts);
    }

    @Override
    public DeliveryRoute copy() {
        return new DeliveryRoute(this);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public void addStop(int index, Stop stop) {
        stops.add(index, stop);
    }

    public WeightLimit getWeightLimit() {
        return weightLimit;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public List<String> describe() {
        List<String> lines = new ArrayList<>();
        lines.add("Напрямок: " + name);
        lines.add("Дата: " + date);
        lines.add("Кур'єр: " + courier);
        lines.add("Зупинок: " + stops.size());
        for (Stop s : stops) {
            lines.add("  • " + s);
        }
        lines.add("Вага: " + weightLimit);
        lines.add("Ділянок з тарифом: " + segmentTariffs.size());
        return lines;
    }
}
