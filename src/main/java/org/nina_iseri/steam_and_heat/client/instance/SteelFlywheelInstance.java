package org.nina_iseri.steam_and_heat.client.instance;

import org.nina_iseri.steam_and_heat.client.ModBlockPartials;
import org.nina_iseri.steam_and_heat.content.flywheel.CommonFlywheelBlockEntity;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class SteelFlywheelInstance extends AbstractSPFlywheelInstance {
    public SteelFlywheelInstance(VisualizationContext modelManager, CommonFlywheelBlockEntity tile, float d) {
        super(modelManager, tile, d);
    }

    @Override
    protected PartialModel getWheelModel() {
        return ModBlockPartials.STEEL_FLYWHEEL;
    }

    @Override
    protected PartialModel getUpperSlidingModel() {
        return ModBlockPartials.STEEL_FLYWHEEL_UPPER_SLIDING;
    }

    @Override
    protected PartialModel getLowerSlidingModel() {
        return ModBlockPartials.STEEL_FLYWHEEL_LOWER_SLIDING;
    }

    @Override
    protected PartialModel getUpperRotatingModel() {
        return ModBlockPartials.STEEL_FLYWHEEL_UPPER_ROTATING;
    }

    @Override
    protected PartialModel getLowerRotatingModel() {
        return ModBlockPartials.STEEL_FLYWHEEL_LOWER_ROTATING;
    }
}