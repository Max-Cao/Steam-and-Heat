package org.nina_iseri.steam_and_heat.client.instance;

import org.nina_iseri.steam_and_heat.client.ModBlockPartials;
import org.nina_iseri.steam_and_heat.content.flywheel.CommonFlywheelBlockEntity;

import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class BronzeFlywheelInstance extends AbstractSPFlywheelInstance {
    public BronzeFlywheelInstance(VisualizationContext modelManager, CommonFlywheelBlockEntity tile, float d) {
        super(modelManager, tile, d);
    }

    @Override
    protected PartialModel getWheelModel() {
        return ModBlockPartials.BRONZE_FLYWHEEL;
    }

    @Override
    protected PartialModel getUpperSlidingModel() {
        return ModBlockPartials.BRONZE_FLYWHEEL_UPPER_SLIDING;
    }

    @Override
    protected PartialModel getLowerSlidingModel() {
        return ModBlockPartials.BRONZE_FLYWHEEL_LOWER_SLIDING;
    }

    @Override
    protected PartialModel getUpperRotatingModel() {
        return ModBlockPartials.BRONZE_FLYWHEEL_UPPER_ROTATING;
    }

    @Override
    protected PartialModel getLowerRotatingModel() {
        return ModBlockPartials.BRONZE_FLYWHEEL_LOWER_ROTATING;
    }
}
