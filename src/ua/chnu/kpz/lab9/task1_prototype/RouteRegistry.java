package ua.chnu.kpz.lab9.task1_prototype;

import ua.chnu.kpz.lab9.common.EventLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Реєстр прототипів (Prototype Registry / Client).
 * Зберігає еталонні маршрути і видає лише їхні копії — сам еталон назовні не віддається.
 */
public class RouteRegistry {

    private static final String SRC = "Реєстр маршрутів";
    private final Map<String, DeliveryRoute> etalons = new HashMap<>();

    public void register(String key, DeliveryRoute etalon) {
        etalons.put(key, etalon);
        EventLog.log(SRC, "Еталон «" + key + "» зареєстровано");
    }

    public DeliveryRoute createDaily(String key, String date, String courier) {
        DeliveryRoute etalon = etalons.get(key);
        if (etalon == null) {
            throw new IllegalArgumentException("Немає еталона: " + key);
        }
        DeliveryRoute daily = etalon.copy();
        daily.setDate(date);
        daily.setCourier(courier);
        EventLog.log(SRC, "Маршрут на " + date + " отримано копіюванням еталона (без побудови), кур'єр " + courier);
        return daily;
    }

    /** Лише для демонстрації порівняння «поруч»; повертає копію, щоб еталон не можна було зіпсувати. */
    public DeliveryRoute etalonSnapshot(String key) {
        return etalons.get(key).copy();
    }
}
