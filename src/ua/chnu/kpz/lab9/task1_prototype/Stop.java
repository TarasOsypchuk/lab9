package ua.chnu.kpz.lab9.task1_prototype;

/** Зупинка маршруту (вкладений змінюваний об'єкт — його теж треба копіювати). */
public class Stop {

    private String place;
    private String arrivalTime;

    public Stop(String place, String arrivalTime) {
        this.place = place;
        this.arrivalTime = arrivalTime;
    }

    /** Копіювальний конструктор. */
    public Stop(Stop other) {
        this(other.place, other.arrivalTime);
    }

    public String getPlace() {
        return place;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return place + " " + arrivalTime;
    }
}
