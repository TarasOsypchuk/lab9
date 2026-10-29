package ua.chnu.kpz.lab9.task1_prototype;

/** Обмеження за вагою (вкладений змінюваний об'єкт). */
public class WeightLimit {

    private int maxTotalKg;
    private int maxParcelKg;

    public WeightLimit(int maxTotalKg, int maxParcelKg) {
        this.maxTotalKg = maxTotalKg;
        this.maxParcelKg = maxParcelKg;
    }

    public WeightLimit(WeightLimit other) {
        this(other.maxTotalKg, other.maxParcelKg);
    }

    public void setMaxTotalKg(int maxTotalKg) {
        this.maxTotalKg = maxTotalKg;
    }

    @Override
    public String toString() {
        return "до " + maxTotalKg + " кг (посилка ≤ " + maxParcelKg + " кг)";
    }
}
