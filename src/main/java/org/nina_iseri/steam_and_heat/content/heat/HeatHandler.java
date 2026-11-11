package org.nina_iseri.steam_and_heat.content.heat;


import org.nina_iseri.steam_and_heat.registrate.ModCapabilities;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class HeatHandler implements IHeatHandler, INBTSerializable<Tag> {
    protected double energy;
    protected double capacity;
    protected double maxReceive;
    protected double maxExtract;

    public HeatHandler(double capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public HeatHandler(double capacity, double maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public HeatHandler(double capacity, double maxReceive, double maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public HeatHandler(double capacity, double maxReceive, double maxExtract, double energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    @Override
    public double receiveInner(double amount, boolean isSimulate) {
        double energyReceived = Math.clamp(this.capacity - this.energy, 0, Math.min(this.maxReceive == 0 ? Double.MAX_VALUE : this.maxReceive, amount));
        if (!isSimulate)
            this.energy += energyReceived;
        return energyReceived;
    }

    @Override
    public double exactInner(double amount, boolean isSimulate) {
        double energyExtracted = Math.min(this.energy, Math.min(this.maxExtract == 0 ? Double.MAX_VALUE : this.maxExtract, amount));
        if (!isSimulate)
            this.energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public double getHeatAmount() {
        return this.energy;
    }

    @Override
    public double getHeatCapacity() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        return DoubleTag.valueOf(this.getHeatAmount());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
        if (!(nbt instanceof DoubleTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }

    public static IHeatHandler of(BlazeBurnerBlockEntity block, Level level) {
        BlockState state = level.getBlockState(block.getBlockPos());
        return switch (BlazeBurnerBlock.getHeatLevelOf(state)) {
            case HeatLevel.SEETHING, HeatLevel.KINDLED -> IHeatHandler.infinite();
            default -> IHeatHandler.never();
        };
    }

    public static IHeatHandler of(AbstractFurnaceBlockEntity block, Level level) {
        BlockState state = level.getBlockState(block.getBlockPos());
        // 这个接口的方块怎么会不lit呢？
        // if (!state.hasProperty(AbstractFurnaceBlock.LIT)) {
        //     return HeatSource.EMPTY;
        // }
        if (state.getValue(AbstractFurnaceBlock.LIT)) {
            return IHeatHandler.infinite();
        } else {
            return IHeatHandler.never();
        }
    }

    public static <T extends BlockEntity> IHeatHandler of(T block, Level level) {
        if (block == null || level == null) return null;
        if (block instanceof AbstractFurnaceBlockEntity f) {
            return of(f, level);
        }
        if (block instanceof BlazeBurnerBlockEntity b) {
            return of(b, level);
        }
        return level.getCapability(ModCapabilities.Heat.BLOCK, block.getBlockPos());
    }

}