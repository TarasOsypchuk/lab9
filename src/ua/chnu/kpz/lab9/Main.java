package ua.chnu.kpz.lab9;

import ua.chnu.kpz.lab9.common.EventLog;
import ua.chnu.kpz.lab9.domain.Shipment;
import ua.chnu.kpz.lab9.task1_prototype.DeliveryRoute;
import ua.chnu.kpz.lab9.task1_prototype.RouteRegistry;
import ua.chnu.kpz.lab9.task1_prototype.Stop;
import ua.chnu.kpz.lab9.task2_pool.Courier;
import ua.chnu.kpz.lab9.task2_pool.VanPool;
import ua.chnu.kpz.lab9.task3_singleton.RegistrationPoint;
import ua.chnu.kpz.lab9.task3_singleton.WaybillNumberingService;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Лабораторна робота №9. Варіант 3 — служба доставки «Посилка+».
 * Окремий метод-сценарій для кожної ситуації.
 */
public class Main {

    public static void main(String[] args) {
        String which = args.length > 0 ? args[0] : "all";
        if (which.equals("all") || which.equals("1")) {
            task1DailyRoutesPrototype();
        }
        if (which.equals("all") || which.equals("2")) {
            task2VanPool();
        }
        if (which.equals("all") || which.equals("3")) {
            task3WaybillNumberingSingleton();
        }
    }

    // ------------------------------------------------------------------ Завдання 1
    static void task1DailyRoutesPrototype() {
        EventLog.section("Завдання 1. Щоденні маршрути доставки — PROTOTYPE");
        EventLog.resetClock();
        String key = "Чернівці – Хотин – Кельменці";

        RouteRegistry registry = new RouteRegistry();
        DeliveryRoute etalon = new DeliveryRoute(key, List.of("Чернівці", "Хотин", "Кельменці"));
        registry.register(key, etalon);

        DeliveryRoute day1 = registry.createDaily(key, "03.11.2026", "Іваненко");
        DeliveryRoute day2 = registry.createDaily(key, "04.11.2026", "Петренко");
        DeliveryRoute day3 = registry.createDaily(key, "05.11.2026", "Сидоренко");

        EventLog.log("Диспетчер", "Змінюємо маршрут на " + day2.getDate() + ":");
        day2.addStop(2, new Stop("Новоселиця", "10:30"));
        EventLog.log("Диспетчер", "  додано зупинку «Новоселиця»");
        day2.getWeightLimit().setMaxTotalKg(550);
        EventLog.log("Диспетчер", "  обмеження ваги змінено на 550 кг");
        day2.setCourier("Коваль");
        EventLog.log("Диспетчер", "  кур'єра замінено на Коваль");
        day2.getStops().get(0).setArrivalTime("08:15");
        EventLog.log("Диспетчер", "  час відправлення з Чернівців змінено на 08:15");

        printSideBySide("ЕТАЛОН", registry.etalonSnapshot(key).describe(),
                "МАРШРУТ " + day2.getDate(), day2.describe());

        EventLog.log("Перевірка", "Еталон і копія — різні об'єкти: " + (etalon != day2));
        EventLog.log("Перевірка", "Списки зупинок різні: " + (etalon.getStops() != day2.getStops()));
        EventLog.log("Перевірка", "Обмеження ваги — різні об'єкти: "
                + (etalon.getWeightLimit() != day2.getWeightLimit()));
        EventLog.log("Перевірка", "Інші дні не змінилися: " + day1.getStops().size() + " і "
                + day3.getStops().size() + " зупинки");
    }

    // ------------------------------------------------------------------ Завдання 2
    static void task2VanPool() {
        EventLog.section("Завдання 2. Службові фургони — OBJECT POOL");
        EventLog.resetClock();
        VanPool pool = new VanPool(3);

        List<Courier> couriers = List.of(
                new Courier("Іваненко", parcels("5900000101", "5900000102")),
                new Courier("Петренко", parcels("5900000103")),
                new Courier("Сидоренко", parcels("5900000104", "5900000105")),
                new Courier("Коваль", parcels("5900000106")),
                new Courier("Мельник", parcels("5900000107", "5900000108")));

        EventLog.log("Відділення", "5 кур'єрів одночасно запитують фургон");
        for (Courier c : couriers) {
            pool.acquire(c);
        }

        EventLog.log("Кур'єр Іваненко", "Повернувся з рейсу");
        pool.release(couriers.get(0)); // F-1 → Коваль без повної підготовки

        EventLog.log("Кур'єр Петренко", "Повернувся з рейсу");
        pool.release(couriers.get(1)); // F-2 → Мельник

        EventLog.log("Кур'єр Сидоренко", "Повернувся з рейсу");
        pool.release(couriers.get(2)); // черга порожня → F-3 у вільні

        EventLog.log("Кур'єр Іваненко", "Другий виїзд з новими відправленнями");
        Courier ivanenkoAgain = new Courier("Іваненко", parcels("5900000109"));
        pool.acquire(ivanenkoAgain);

        pool.release(couriers.get(3));
        pool.release(couriers.get(4));
        pool.release(ivanenkoAgain);

        pool.printStatistics();
    }

    // ------------------------------------------------------------------ Завдання 3
    static void task3WaybillNumberingSingleton() {
        EventLog.section("Завдання 3. Нумерація накладних — SINGLETON");
        EventLog.resetClock();

        RegistrationPoint branch1 = new RegistrationPoint("Відділення №1");
        RegistrationPoint branch2 = new RegistrationPoint("Відділення №2");
        RegistrationPoint online = new RegistrationPoint("Онлайн-кабінет");

        List<String> all = new ArrayList<>();
        all.addAll(branch1.registerShipments(
                new Shipment("Бойко", "Ткачук", 1.2),
                new Shipment("Лисенко", "Гнатюк", 3.5),
                new Shipment("Олійник", "Кравець", 0.4)));
        all.addAll(branch2.registerShipments(
                new Shipment("Шевчук", "Мороз", 0.2),
                new Shipment("Поліщук", "Савчук", 2.0),
                new Shipment("Руденко", "Клименко", 2.8)));
        all.addAll(online.registerShipments(
                new Shipment("Марченко", "Бондар", 1.1),
                new Shipment("Ковальчук", "Остапчук", 4.7),
                new Shipment("Павлюк", "Захарчук", 0.9)));

        Set<String> unique = new HashSet<>(all);
        EventLog.log("Перевірка", "Видано номерів: " + all.size() + ", унікальних: " + unique.size());
        EventLog.log("Перевірка", "Номери послідовні: " + isSequential(all));
        EventLog.log("Перевірка", "id з усіх місць: " + branch1.getNumbering().getServiceId() + " / "
                + branch2.getNumbering().getServiceId() + " / " + online.getNumbering().getServiceId());
        EventLog.log("Перевірка", "Один і той самий об'єкт: "
                + (branch1.getNumbering() == branch2.getNumbering()
                && branch2.getNumbering() == online.getNumbering()));

        // Спроба обійти механізм: new неможливий (private), пробуємо рефлексією
        try {
            Constructor<WaybillNumberingService> ctor =
                    WaybillNumberingService.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ctor.newInstance();
            EventLog.log("Зловмисне відділення", "Створено другу службу (так бути не повинно!)");
        } catch (ReflectiveOperationException e) {
            EventLog.log("Зловмисне відділення", "Відмова: " + e.getCause().getMessage());
        }
    }

    // ------------------------------------------------------------------ helpers
    /** Вже зареєстровані відправлення для завантаження у фургон. */
    private static List<Shipment> parcels(String... numbers) {
        List<Shipment> list = new ArrayList<>();
        for (String n : numbers) {
            Shipment s = new Shipment("Відділення №1", "Отримувач", 2.0);
            s.assignWaybillNumber(n);
            list.add(s);
        }
        return list;
    }
    private static boolean isSequential(List<String> numbers) {
        for (int i = 1; i < numbers.size(); i++) {
            if (Long.parseLong(numbers.get(i)) != Long.parseLong(numbers.get(i - 1)) + 1) {
                return false;
            }
        }
        return true;
    }

    private static void printSideBySide(String leftTitle, List<String> left,
                                        String rightTitle, List<String> right) {
        int width = 42;
        System.out.println();
        System.out.printf("%-" + width + "s | %s%n", leftTitle, rightTitle);
        System.out.println("-".repeat(width) + "-+-" + "-".repeat(width));
        int rows = Math.max(left.size(), right.size());
        for (int i = 0; i < rows; i++) {
            String l = i < left.size() ? left.get(i) : "";
            String r = i < right.size() ? right.get(i) : "";
            System.out.printf("%-" + width + "s | %s%n", l, r);
        }
        System.out.println();
    }
}
