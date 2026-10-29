package ua.chnu.kpz.lab9.common;

/**
 * Консольний журнал подій у форматі: [час] [джерело] подія.
 * Час умовний: модельний годинник починається з 08:00:00
 * і просувається на кожну подію та на «дорогі» операції.
 */
public final class EventLog {

    private static int simSeconds = 8 * 3600; // 08:00:00

    private EventLog() {
    }

    public static void log(String source, String event) {
        simSeconds += 1;
        System.out.printf("[%s] [%s] %s%n", time(), source, event);
    }

    /** Емуляція дорогої операції: повідомлення + реальна затримка + просування модельного часу. */
    public static void expensive(String source, String event, int simMinutes) {
        log(source, event + " ... (триває ~" + simMinutes + " хв)");
        try {
            Thread.sleep(300L * Math.min(simMinutes, 5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        simSeconds += simMinutes * 60;
    }

    public static void section(String title) {
        System.out.println();
        System.out.println("==================== " + title + " ====================");
    }

    public static void resetClock() {
        simSeconds = 8 * 3600;
    }

    private static String time() {
        int h = (simSeconds / 3600) % 24;
        int m = (simSeconds / 60) % 60;
        int s = simSeconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
