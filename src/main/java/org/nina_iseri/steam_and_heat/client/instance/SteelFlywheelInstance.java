package org.nina_iseri.steam_and_heat.client.instance;

import org.nina_iseri.steam_and_heat.block.SPBlockPartials;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldFlywheelBlockEntity;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class SteelFlywheelInstance extends AbstractSPFlywheelInstance {
    public SteelFlywheelInstance(VisualizationContext modelManager, OldFlywheelBlockEntity tile, float d) {
        super(modelManager, tile, d);
    }

    @Override
    protected PartialModel getWheelModel() {
        return SPBlockPartials.STEEL_FLYWHEEL;
    }

    @Override
    protected PartialModel getUpperSlidingModel() {
        return SPBlockPartials.STEEL_FLYWHEEL_UPPER_SLIDING;
    }

    @Override
    protected PartialModel getLowerSlidingModel() {
        return SPBlockPartials.STEEL_FLYWHEEL_LOWER_SLIDING;
    }

    @Override
    protected PartialModel getUpperRotatingModel() {
        return SPBlockPartials.STEEL_FLYWHEEL_UPPER_ROTATING;
    }

    @Override
    protected PartialModel getLowerRotatingModel() {
        return SPBlockPartials.STEEL_FLYWHEEL_LOWER_ROTATING;
    }
}