package ua.chnu.kpz.lab9.task2_pool;

import ua.chnu.kpz.lab9.common.EventLog;
import ua.chnu.kpz.lab9.domain.Shipment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * Object Pool — парк фургонів відділення.
 *
 * Стратегія при вичерпанні: FIFO-черга очікування кур'єрів.
 * Кур'єр, що прийшов першим, першим отримує фургон, який повернувся;
 * відправлення лишаються закріпленими за ним і не губляться
 * (на відміну від перенесення, що зсуває доставку на наступний виїзд).
 */
public class VanPool {

    private static final String SRC = "Парк фургонів";

    private final int maxSize;
    private final Deque<Van> available = new ArrayDeque<>();
    private final List<Van> inUse = new ArrayList<>();
    private final List<Van> all = new ArrayList<>();
    private final Deque<Courier> waiting = new ArrayDeque<>();

    public VanPool(int maxSize) {
        this.maxSize = maxSize;
        EventLog.log(SRC, "Відділення має " + maxSize + " фургони (створюються при першій потребі)");
    }

    /** Видати фургон або поставити кур'єра в чергу. */
    public synchronized Optional<Van> acquire(Courier courier) {
        Van van;
        if (!available.isEmpty()) {
            van = available.poll();
            EventLog.log(SRC, "Вільний фургон " + van.getId() + " видається без повної підготовки");
        } else if (all.size() < maxSize) {
            van = new Van("F-" + (all.size() + 1));
            all.add(van);
        } else {
            waiting.add(courier);
            EventLog.log(SRC, "Вільних фургонів немає → кур'єр " + courier.getName()
                    + " у черзі (позиція " + waiting.size() + ")");
            return Optional.empty();
        }
        handOver(van, courier);
        return Optional.of(van);
    }

    /** Повернути фургон: розвантажити, скинути стан і віддати першому в черзі. */
    public synchronized void release(Courier courier) {
        Van van = courier.getVan();
        if (van == null || !inUse.remove(van)) {
            throw new IllegalStateException("Фургон не належить пулу або вже повернений");
        }
        courier.setVan(null);
        van.getCargo().forEach(s -> s.setStatus(Shipment.Status.DELIVERED));
        van.reset();
        EventLog.log(SRC, van.getId() + " повернено, розвантажено (стан: " + van.state() + ")");

        Courier next = waiting.poll();
        if (next != null) {
            EventLog.log(SRC, "Перший у черзі — кур'єр " + next.getName());
            handOver(van, next);
        } else {
            available.add(van);
        }
    }

    private void handOver(Van van, Courier courier) {
        van.assignTo(courier.getName(), courier.getParcels());
        courier.setVan(van);
        inUse.add(van);
        EventLog.log(SRC, "Фургон " + van.getId() + " → кур'єр " + courier.getName()
                + " (відправлення " + courier.getParcels() + ")");
    }

    public void printStatistics() {
        EventLog.log(SRC, "Статистика рейсів:");
        for (Van v : all) {
            EventLog.log(SRC, "  " + v.getId() + ": " + v.getTripCount() + " рейс(и)");
        }
        EventLog.log(SRC, "Усього створено фургонів: " + all.size()
                + " (ліміт " + maxSize + "), у черзі: " + waiting.size());
    }
}
