package org.nina_iseri.steam_and_heat.content.flywheel;

import org.nina_iseri.steam_and_heat.utils.InterpolatedChasingValue;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CommonFlywheelBlockEntity extends GeneratingKineticBlockEntity {
    private float generatedCapacity;
    private float generatedSpeed;
    private int stoppingCooldown;
    public InterpolatedChasingValue visualSpeed = new InterpolatedChasingValue();
    public float angle;

    public CommonFlywheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setRotation(float speed, float capacity) {
        if (this.generatedSpeed != speed || this.generatedCapacity != capacity) {
            if (speed == 0.0F) {
                if (this.stoppingCooldown == 0) {
                    this.stoppingCooldown = 40;
                }

                return;
            }

            this.stoppingCooldown = 0;
            this.generatedSpeed = speed;
            this.generatedCapacity = capacity;
            this.updateGeneratedRotation();
        }

    }

    @Override
    public float getGeneratedSpeed() {
        return convertToDirection(this.generatedSpeed, this.getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING));
    }

    @Override
    public float calculateAddedStressCapacity() {
        return this.lastCapacityProvided = this.generatedCapacity;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2.0);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putFloat("GeneratedSpeed", this.generatedSpeed);
        compound.putFloat("GeneratedCapacity", this.generatedCapacity);
        compound.putInt("Cooldown", this.stoppingCooldown);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        this.generatedSpeed = compound.getFloat("GeneratedSpeed");
        this.generatedCapacity = compound.getFloat("GeneratedCapacity");
        this.stoppingCooldown = compound.getInt("Cooldown");
        super.read(compound, registries, clientPacket);
        if (clientPacket) {
            this.visualSpeed.withSpeed(0.03125F).target(this.getGeneratedSpeed());
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            float targetSpeed = this.isVirtual() ? this.speed : this.getGeneratedSpeed();
            this.visualSpeed.target(targetSpeed);
            this.visualSpeed.tick();
            this.angle += this.visualSpeed.value * 3.0F / 10.0F;
            this.angle %= 360.0F;
        } else {
            if (this.getGeneratedSpeed() != 0.0F && this.getSpeed() == 0.0F) {
                this.updateGeneratedRotation();
            }

            if (this.stoppingCooldown != 0) {
                --this.stoppingCooldown;
                if (this.stoppingCooldown == 0) {
                    this.generatedCapacity = 0.0F;
                    this.generatedSpeed = 0.0F;
                    this.updateGeneratedRotation();
                }

            }
        }
    }
}
