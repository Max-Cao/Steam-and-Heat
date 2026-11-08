package org.nina_iseri.steam_and_heat.content.engine;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CommonEngineInstance extends AbstractBlockEntityVisual<CommonEngineBlockEntity> {

    protected TransformedInstance frame;

    public CommonEngineInstance(VisualizationContext modelManager, CommonEngineBlockEntity tile, float d) {
        super(modelManager, tile, d);

        Block block = blockState
                .getBlock();
        if (!(block instanceof CommonEngineBlock engineBlock))
            return;

        PartialModel frame = engineBlock.getFrameModel();

        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        this.frame = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(frame)).createInstance();

        float angle = AngleHelper.rad(AngleHelper.horizontalAngle(facing));

        this.frame
                .translate(getVisualPosition())
                .nudge(pos.hashCode())
                .center()
                .rotate(angle, Direction.UP)
                .uncenter()
                .translate(0, 0, -1);
    }

    @Override
    public void _delete() {
        frame.delete();
    }

    @Override
    public void updateLight(float v) {
        relight(pos, frame);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }
}
