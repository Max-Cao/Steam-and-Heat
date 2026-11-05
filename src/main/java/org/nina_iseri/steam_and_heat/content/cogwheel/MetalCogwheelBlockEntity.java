package org.nina_iseri.steam_and_heat.content.cogwheel;


import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MetalCogwheelBlockEntity extends SimpleKineticBlockEntity {
    public MetalCogwheelBlockEntity(BlockEntityType<? extends MetalCogwheelBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
