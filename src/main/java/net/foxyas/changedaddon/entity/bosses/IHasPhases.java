package net.foxyas.changedaddon.entity.bosses;

public interface IHasPhases<T extends Enum<?>> {
    T getPhase();
    void setPhase(T phase);
}
