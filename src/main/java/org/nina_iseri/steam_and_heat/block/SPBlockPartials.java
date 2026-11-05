package org.nina_iseri.steam_and_heat.block;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class SPBlockPartials {
    public static final PartialModel BRONZE_FLYWHEEL = get("bronze_flywheel/wheel");
    public static final PartialModel BRONZE_FLYWHEEL_UPPER_ROTATING = get("bronze_flywheel/upper_rotating_connector");
    public static final PartialModel BRONZE_FLYWHEEL_LOWER_ROTATING = get("bronze_flywheel/lower_rotating_connector");
    public static final PartialModel BRONZE_FLYWHEEL_UPPER_SLIDING = get("bronze_flywheel/upper_sliding_connector");
    public static final PartialModel BRONZE_FLYWHEEL_LOWER_SLIDING = get("bronze_flywheel/lower_sliding_connector");

    public static final PartialModel CAST_IRON_FLYWHEEL = get("cast_iron_flywheel/wheel");
    public static final PartialModel CAST_IRON_FLYWHEEL_UPPER_ROTATING = get("cast_iron_flywheel/upper_rotating_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_LOWER_ROTATING = get("cast_iron_flywheel/lower_rotating_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_UPPER_SLIDING = get("cast_iron_flywheel/upper_sliding_connector");
    public static final PartialModel CAST_IRON_FLYWHEEL_LOWER_SLIDING = get("cast_iron_flywheel/lower_sliding_connector");

    public static final PartialModel STEEL_FLYWHEEL = get("steel_flywheel/wheel");
    public static final PartialModel STEEL_FLYWHEEL_UPPER_ROTATING = get("steel_flywheel/upper_rotating_connector");
    public static final PartialModel STEEL_FLYWHEEL_LOWER_ROTATING = get("steel_flywheel/lower_rotating_connector");
    public static final PartialModel STEEL_FLYWHEEL_UPPER_SLIDING = get("steel_flywheel/upper_sliding_connector");
    public static final PartialModel STEEL_FLYWHEEL_LOWER_SLIDING = get("steel_flywheel/lower_sliding_connector");

    public static final PartialModel BRASS_FLYWHEEL = get("brass_flywheel/wheel");
    public static final PartialModel BRASS_FLYWHEEL_UPPER_ROTATING = get("brass_flywheel/upper_rotating_connector");
    public static final PartialModel BRASS_FLYWHEEL_LOWER_ROTATING = get("brass_flywheel/lower_rotating_connector");
    public static final PartialModel BRASS_FLYWHEEL_UPPER_SLIDING = get("brass_flywheel/upper_sliding_connector");
    public static final PartialModel BRASS_FLYWHEEL_LOWER_SLIDING = get("brass_flywheel/lower_sliding_connector");

    public static final PartialModel DYNAMO_SHAFT = get("dynamo/shaft");

    public static final PartialModel FURNACE_GENERATOR_FRAME = get("furnace_engine/frame");

    private static PartialModel get(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath("steam_and_heat", "block/" + path));
    }

    public static void clientInit() {
    }
}
