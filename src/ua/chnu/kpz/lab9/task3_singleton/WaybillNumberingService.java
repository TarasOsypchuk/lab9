package ua.chnu.kpz.lab9.task3_singleton;

import ua.chnu.kpz.lab9.common.EventLog;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton — єдина на всю компанію служба нумерації накладних.
 *
 * - private-конструктор: new ззовні неможливий;
 * - Initialization-on-demand holder: лінива і потокобезпечна ініціалізація без synchronized
 *   (JVM гарантує, що клас Holder ініціалізується рівно один раз);
 * - захист від рефлексії: повторний виклик конструктора кидає виняток;
 * - AtomicLong: номери не повторюються навіть при одночасних запитах.
 */
public final class WaybillNumberingService {

    private static final String SRC = "Служба нумерації";
    private static boolean created = false;

    private final String serviceId;
    private final AtomicLong counter = new AtomicLong(0);

    private WaybillNumberingService() {
        synchronized (WaybillNumberingService.class) {
            if (created) {
                throw new IllegalStateException("Служба нумерації вже існує — друга копія заборонена");
            }
            created = true;
        }
        this.serviceId = "NUM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        EventLog.expensive(SRC, "Підключення до реєстру накладних компанії", 2);
        EventLog.log(SRC, "Службу нумерації ініціалізовано (id " + serviceId + ")");
    }

    private static final class Holder {
        private static final WaybillNumberingService INSTANCE = new WaybillNumberingService();
    }

    public static WaybillNumberingService getInstance() {
        return Holder.INSTANCE;
    }

    public String nextNumber() {
        return String.format("59%08d", counter.incrementAndGet());
    }

    public String getServiceId() {
        return serviceId;
    }
}
