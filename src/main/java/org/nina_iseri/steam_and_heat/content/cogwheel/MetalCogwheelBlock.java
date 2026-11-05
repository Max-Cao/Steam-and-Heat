package org.nina_iseri.steam_and_heat.content.cogwheel;

import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.SPBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MetalCogwheelBlock extends CogWheelBlock {
    protected MetalCogwheelBlock(boolean large, Properties properties) {
        super(large, properties);
    }

    @Override
    public BlockEntityType<? extends SimpleKineticBlockEntity> getBlockEntityType() {
        return SPBlockEntities.METAL_COGWHEEL.get();
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return InteractionResult.PASS;
    }

        public static MetalCogwheelBlock small(Properties properties) {
        return new MetalCogwheelBlock(false, properties);
    }

    public static MetalCogwheelBlock large(Properties properties) {
        return new MetalCogwheelBlock(true, properties);
    }
}
