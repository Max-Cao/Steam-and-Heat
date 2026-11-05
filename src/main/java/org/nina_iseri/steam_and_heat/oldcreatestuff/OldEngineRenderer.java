package org.nina_iseri.steam_and_heat.oldcreatestuff;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

public class OldEngineRenderer<T extends OldEngineBlockEntity> extends SafeBlockEntityRenderer<T> {

    public OldEngineRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(T te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {

        if (VisualizationManager.supportsVisualization(te.getLevel())) return;

        Block block = te.getBlockState()
                .getBlock();
        if (block instanceof OldEngineBlock) {
            OldEngineBlock engineBlock = (OldEngineBlock) block;
            PartialModel frame = engineBlock.getFrameModel();
            if (frame != null) {
                Direction facing = te.getBlockState()
                        .getValue(OldEngineBlock.FACING);
                float angle = AngleHelper.rad(AngleHelper.horizontalAngle(facing));
                CachedBuffers.partial(frame, te.getBlockState())
                        .rotateCentered(angle, Direction.UP)
                        .translate(0, 0, -1)
                        .light(light)
                        .renderInto(ms, buffer.getBuffer(RenderType.solid()));
            }
        }
    }

}
