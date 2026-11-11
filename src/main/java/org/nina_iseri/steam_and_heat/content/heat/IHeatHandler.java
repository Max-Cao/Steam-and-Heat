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

    public static IHeatHandler constant(double v) {
        return new IHeatHandler() {
            @Override
            public double getHeatAmount() {
                return v;
            }

            @Override
            public double getHeatCapacity() {
                return Double.MAX_VALUE;
            }

            @Override
            public double exactInner(double amount, boolean isSimulate) {
                return v;
            }

            @Override
            public double receiveInner(double amount, boolean isSimulate) {
                return v;
            }

            @Override
            public boolean canExtract() {
                return v > 0;
            }

            @Override
            public boolean canReceive() {
                return v > 0;
            }
        };
    }

    public static IHeatHandler infinite() {
        return constant(Double.MAX_VALUE);
    }

    public static IHeatHandler never() {
        return constant(0);
    }
}
