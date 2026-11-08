package org.nina_iseri.steam_and_heat.client;

import org.nina_iseri.steam_and_heat.SteamAndHeat;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModBlockPartials {
    public static final PartialModel BRONZE_FLYWHEEL = source("bronze_flywheel/wheel");
    public static final PartialModel BRONZE_FLYWHEEL_UPPER_ROTATING = source("bronze_flywheel/upper_rotating_connector");
    public static final PartialModel BRONZE_FLYWHEEL_LOWER_ROTATING = source("bronze_flywheel/lower_rotating_connector");
    public static final PartialModel BRONZE_FLYWHEEL_UPPER_SLIDING = source("bronze_flywheel/upper_sliding_connector");
    public static final PartialModel BRONZE_FLYWHEEL_LOWER_SLIDING = source("bronze_flywheel/lower_sliding_connector");

    public static final PartialModel CAST_IRON_FLYWHEEL = source("cast_iron_flywheel/wheel");
    public static final PartialModel CAST_IRON_FLYWHEEL_UPPER_ROTATING = source("cast_iron_flywheel/upper_rotating_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_LOWER_ROTATING = source("cast_iron_flywheel/lower_rotating_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_UPPER_SLIDING = source("cast_iron_flywheel/upper_sliding_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_LOWER_SLIDING = source("cast_iron_flywheel/lower_sliding_connector");

    public static final PartialModel STEEL_FLYWHEEL = source("steel_flywheel/wheel");
    public static final PartialModel STEEL_FLYWHEEL_UPPER_ROTATING = source("steel_flywheel/upper_rotating_connector");
    public static final PartialModel STEEL_FLYWHEEL_LOWER_ROTATING = source("steel_flywheel/lower_rotating_connector");
    public static final PartialModel STEEL_FLYWHEEL_UPPER_SLIDING = source("steel_flywheel/upper_sliding_connector");
    public static final PartialModel STEEL_FLYWHEEL_LOWER_SLIDING = source("steel_flywheel/lower_sliding_connector");

    public static final PartialModel BRASS_FLYWHEEL = source("brass_flywheel/wheel");
    public static final PartialModel BRASS_FLYWHEEL_UPPER_ROTATING = source("brass_flywheel/upper_rotating_connector");
    public static final PartialModel BRASS_FLYWHEEL_LOWER_ROTATING = source("brass_flywheel/lower_rotating_connector");
    public static final PartialModel BRASS_FLYWHEEL_UPPER_SLIDING = source("brass_flywheel/upper_sliding_connector");
    public static final PartialModel BRASS_FLYWHEEL_LOWER_SLIDING = source("brass_flywheel/lower_sliding_connector");

    public static final PartialModel FURNACE_GENERATOR_FRAME = source("furnace_engine/frame");

    private static PartialModel source(String path) {
        return PartialModel.of(SteamAndHeat.resource("block/"+path));
    }

    public static void clientInit() {
    }
}
