package org.nina_iseri.steam_and_heat.content.alternator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import org.nina_iseri.steam_and_heat.ModCommonConfig;
import org.nina_iseri.steam_and_heat.oldcreatestuff.IGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Optional;

/**
 * Adapted from: Create: Crafts & Additions under the MIT License
 * @author MRH0
 * @author yuesha-yc
 */
public class DynamoBlockEntity extends KineticBlockEntity implements IGoggleInformation {

    protected final InternalEnergyStorage energy;
    private boolean redstoneLocked = false;
    boolean working;
    
    public static final int MAX_FE_OUT = ModCommonConfig.COMMON.dynamoFeMaxOut.get(); // FE Output
    public static final int FE_CAPACITY = ModCommonConfig.COMMON.dynamoFeCapacity.get(); // FE Storage
    public static final int IMPACT = ModCommonConfig.COMMON.dynamoImpact.get(); // Impact on network
    public static final double EFFICIENCY = ModCommonConfig.COMMON.dynamoEfficiency.get(); // Efficiency

    public static final ICapabilityProvider<DynamoBlockEntity, Direction, IEnergyStorage> ENERGY_CAP = (DynamoBlockEntity te, Direction side) -> {
        if (side==te.getBlockState().getValue(DynamoBlock.FACING)) {
            return te.energy;
        }
        return null;
    };

    public DynamoBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energy = new InternalEnergyStorage(FE_CAPACITY, 0, MAX_FE_OUT);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (this.getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED)) {
            tooltip.add(Component.literal(spacing).append(Component.translatable("tooltip.steam_and_heat.dynamo.locked").withStyle(ChatFormatting.RED)));
            return true;
        }
		tooltip.add(Component.literal(spacing).append(Component.translatable("tooltip.steam_and_heat.energy.production").withStyle(ChatFormatting.GRAY)));
		tooltip.add(Component.literal(spacing).append(Component.literal(" " + format(getEnergyProductionRate((int) (isSpeedRequirementFulfilled() ? getSpeed() : 0))) + "fe/t ") // fix
		        .withStyle(ChatFormatting.AQUA)).append(CreateLang.translate("gui.goggles.at_current_speed").style(ChatFormatting.DARK_GRAY).component()));
		return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    private static String format(int n) {
        if (n > 1000000)
            return Math.round(n / 100000d) / 10d + "M";
        if (n > 1000)
            return Math.round(n / 100d) / 10d + "K";
        return n + "";
    }

    @Override
    public float calculateStressApplied() {
        if (getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED)) {
            this.lastStressApplied = 0;
            return 0;
        }
		this.lastStressApplied = IMPACT;
		return IMPACT;
    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        energy.read(compound);
        redstoneLocked = compound.getBoolean("redstonelocked");
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        energy.write(compound);
        compound.putBoolean("redstonelocked", redstoneLocked);
    }


    @Override
    public void tick() {
        super.tick();
        if (level != null && level.isClientSide())
            return;
        if (this.getBlockState().getValue(DynamoBlock.REDSTONE_LOCKED)) {
            if(working)
                if(this.hasNetwork())
                    this.getOrCreateNetwork().updateStressFor(this,this.calculateStressApplied());
            working=false;
            return;
        }
        if(!working)
            if(this.hasNetwork())
                this.getOrCreateNetwork().updateStressFor(this,this.calculateStressApplied());
        working=true;
        if (Math.abs(getSpeed()) > 0 && isSpeedRequirementFulfilled())
            energy.internalProduceEnergy(getEnergyProductionRate((int) getSpeed()));
    	Direction side=this.getBlockState().getValue(DynamoBlock.FACING);
        BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
        if (te != null) {
            Optional.ofNullable(level.getCapability(Capabilities.EnergyStorage.BLOCK, te.getBlockPos(), side.getOpposite()))
	            .ifPresent(ies->ies.receiveEnergy(energy.extractEnergy(ies.receiveEnergy(MAX_FE_OUT, true), false), false));
        }
    }

    public static int getEnergyProductionRate(int rpm) {
        rpm = Math.abs(rpm);
        return (int) (Math.abs(rpm) * EFFICIENCY);
    }

    public Level getWorld() {
        return getLevel();
    }
}
