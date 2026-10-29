package ua.chnu.kpz.lab9.task1_prototype;

/**
 * Prototype (інтерфейс прототипу).
 * Власний інтерфейс замість Cloneable: явний контракт глибокої копії,
 * без неперевірених винятків і поверхневого Object.clone().
 */
public interface RoutePrototype {
    DeliveryRoute copy();
}
