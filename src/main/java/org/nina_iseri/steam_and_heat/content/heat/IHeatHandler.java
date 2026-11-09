package org.nina_iseri.steam_and_heat.content.heat;

public interface IHeatHandler {
    double getHeatAmount();

    double getHeatCapacity();

    default double exact(double amount, boolean isSimulate) {
        if (!canExtract()) {
            return 0;
        }
        return exactInner(amount, isSimulate);
    }

    default double receive(double amount, boolean isSimulate) {
        if (!canReceive()) {
            return 0;
        }
        return receiveInner(amount, isSimulate);

    }

    double exactInner(double amount, boolean isSimulate);

    double receiveInner(double amount, boolean isSimulate);

    boolean canExtract();

    boolean canReceive();

    public default boolean noHeat() {
        return getHeatAmount() == 0;
    }
}
