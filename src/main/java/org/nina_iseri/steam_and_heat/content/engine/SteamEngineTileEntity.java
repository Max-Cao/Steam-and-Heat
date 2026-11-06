package org.nina_iseri.steam_and_heat.content.engine;

import org.nina_iseri.steam_and_heat.ModTags;
import org.nina_iseri.steam_and_heat.client.ModParticles;
import org.nina_iseri.steam_and_heat.content.boiler.BoilerBlockEntity;
import org.nina_iseri.steam_and_heat.content.flywheel.SteamFlywheelBlock;
import org.nina_iseri.steam_and_heat.content.flywheel.SteamFlywheelTileEntity;
import org.nina_iseri.steam_and_heat.oldcreatestuff.IGoggleInformation;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldEngineBlock;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldEngineBlockEntity;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class SteamEngineTileEntity extends OldEngineBlockEntity implements IGoggleInformation {

	private FluidTank tank;
	private IFluidHandler handler=new IFluidHandler() {

		@Override
		public int getTanks() {
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(int itank) {
			return tank.getFluid();
		}

		@Override
		public int getTankCapacity(int itank) {
			return tank.getCapacity();
		}

		@Override
		public boolean isFluidValid(int itank, FluidStack stack) {
			return tank.isFluidValid(stack);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			int filled=tank.fill(resource, action);
			if(filled>0&&action==FluidAction.EXECUTE) {
				setChanged();
				level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
			}
			return filled;
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return FluidStack.EMPTY;
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return FluidStack.EMPTY;
		}

	};

	private int heatup = 0;

	protected SteamFlywheelTileEntity poweredWheel;

	public static final ICapabilityProvider<SteamEngineTileEntity, Direction, IFluidHandler> FLUID_CAP = (SteamEngineTileEntity te, Direction side) -> {
		return te.handler;
	};

	public SteamEngineTileEntity(BlockEntityType<? extends SteamEngineTileEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.tank = new FluidTank(this.getSteamStorage(), fluidStack -> {
			if (ModTags.STEAM != null)
				return fluidStack.getFluid().is(ModTags.STEAM);
			else
				return fluidStack.getFluid() == ModFluids.STEAM.get();
		});
	}

	@Override
	public void tick() {
		super.tick();
		if(level == null)return;
		if (!level.isClientSide) {
			BlockState state = this.level.getBlockState(this.worldPosition);
			if(this.poweredWheel==null)attachWheel();
			if (this.poweredWheel == null || this.poweredWheel.isRemoved()) {
				if(this.appliedSpeed!=0||this.appliedCapacity!=0) {
					this.appliedCapacity = 0;
					this.appliedSpeed = 0;
					this.refreshWheelSpeed();
				}
				heatup = 0;
				if(!tank.drain(this.getSteamConsumptionPerTick(), IFluidHandler.FluidAction.EXECUTE).isEmpty()) {
					this.setChanged();
					level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
				}
				if(state.getValue(SteamEngineBlock.LIT))
					this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, false));
			} else {
				Direction engineFacing = this.getBlockState().getValue(OldEngineBlock.FACING);
				BlockPos boilerPos = this.worldPosition.relative(engineFacing, -1);
				FluidTank tank=this.tank;
				if(this.tank.isEmpty()) {
					BlockEntity te=this.getLevel().getBlockEntity(boilerPos);
					if(te instanceof BoilerBlockEntity) {
						tank=((BoilerBlockEntity)te).output;
					}
				}

				if(heatup==0&&tank.getFluidAmount()/this.getSteamConsumptionPerTick()<40)
					return;

				if (!tank.isEmpty() && tank.drain(this.getSteamConsumptionPerTick(), IFluidHandler.FluidAction.EXECUTE)
						.getAmount() >= this.getSteamConsumptionPerTick()) {

					if (heatup >= 60) {
						float spd=this.getGeneratingSpeed();
						float cap=this.getGeneratingCapacity();
						if(this.tank!=tank)
							cap = Mth.ceil(cap*this.getSuckEfficiency());
						if(this.appliedSpeed!=spd||this.appliedCapacity!=cap) {
							this.appliedSpeed = spd;
							this.appliedCapacity = cap;
							this.refreshWheelSpeed();
						}
					} else {
						heatup++;
						this.setChanged();
						level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
						if(!state.getValue(SteamEngineBlock.LIT))
							this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, true));
					}
				} else {
					if (heatup > 0) {
						heatup--;
						this.setChanged();
						level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
					}
					if(this.appliedSpeed!=0||this.appliedCapacity!=0) {
						this.appliedCapacity = 0;
						this.appliedSpeed = 0;
						this.refreshWheelSpeed();
					}
					if(state.getValue(SteamEngineBlock.LIT))
						this.level.setBlockAndUpdate(this.worldPosition, state.setValue(SteamEngineBlock.LIT, false));
				}
			}
		}else {
			if (this.getBlockState().getValue(SteamEngineBlock.LIT)) {
				paticleInterval++;
				double d0 = this.worldPosition.getX() + 0.5D;
				double d1 = this.worldPosition.getY();
				double d2 = this.worldPosition.getZ() + 0.5D;
				if (paticleInterval >= 40) {
					paticleInterval = 0;
					this.level.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25f, 0.25F, false);
					Direction direction = this.getBlockState().getValue(SteamEngineBlock.FACING);
					Direction.Axis direction$axis = direction.getAxis();
					double d4 = this.level.random.nextDouble() * 0.6D - 0.3D;
					double d5 = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52D : d4;
					double d6 = this.level.random.nextDouble() * 9.0D / 16.0D;
					double d7 = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : d4;
					this.level.addParticle(ModParticles.STEAM.get(), d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
				}
			} else paticleInterval = 0;
		}
	}

	private int paticleInterval;

	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!this.getBlockState().getValue(SteamEngineBlock.LIT)) {
			tooltip.add(componentSpacing.plainCopy()
					.append(Component.translatable("tooltip.steam_and_heat.steam_engine.not_enough_steam")
							.withStyle(ChatFormatting.RED)));
		} else if (heatup < 60) {
			tooltip.add(componentSpacing.plainCopy()
					.append(Component.translatable("tooltip.steam_and_heat.steam_engine.heating")
							.withStyle(ChatFormatting.YELLOW)));
		} else {
			tooltip.add(componentSpacing.plainCopy()
					.append(Component.translatable("tooltip.steam_and_heat.steam_engine.running")
							.withStyle(ChatFormatting.GREEN)));
		}
		return this.containedFluidTooltip(tooltip, isPlayerSneaking, handler);
	}

	@Override
	protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(compound, registries, clientPacket);
		tank.readFromNBT(registries, compound.getCompound("TankContent"));
		heatup = compound.getInt("heatup");
	}

	@Override
	public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(compound, registries, clientPacket);
		compound.put("TankContent", tank.writeToNBT(registries, new CompoundTag()));
		compound.putInt("heatup", heatup);
		
	}

	public void attachWheel() {
		Direction engineFacing = (Direction) this.getBlockState().getValue(SteamEngineBlock.FACING);
		BlockPos wheelPos = this.worldPosition.relative(engineFacing, 2);
		BlockState wheelState = this.level.getBlockState(wheelPos);
		if (this.getFlywheel() == wheelState.getBlock()) {
			Direction wheelFacing = (Direction) wheelState.getValue(SteamFlywheelBlock.HORIZONTAL_FACING);
			if (wheelFacing.getAxis() == engineFacing.getClockWise().getAxis()) {
				if (!SteamFlywheelBlock.isConnected(wheelState)
						|| SteamFlywheelBlock.getConnection(wheelState) == engineFacing.getOpposite()) {
					BlockEntity te = this.level.getBlockEntity(wheelPos);
					if (!te.isRemoved()) {
						if (te instanceof SteamFlywheelTileEntity) {
							if (!SteamFlywheelBlock.isConnected(wheelState)) {
								SteamFlywheelBlock.setConnection(this.level, te.getBlockPos(), te.getBlockState(),
										engineFacing.getOpposite());
							}

							this.poweredWheel = (SteamFlywheelTileEntity) te;
							this.refreshWheelSpeed();
							return;
						}

					}
				}
			}
		}
		if(poweredWheel!=null&&!poweredWheel.isRemoved()) {
			this.poweredWheel.setRotation(0, 0);
			this.poweredWheel =null;
		}
	}

	public void detachWheel() {
		super.detachWheel();
	}

	public abstract Block getFlywheel();

	public abstract float getGeneratingCapacity();

	public abstract float getGeneratingSpeed();

	public abstract int getSteamConsumptionPerTick();

	public abstract int getSteamStorage();

	public abstract double getSuckEfficiency();

	@Override
	public void lazyTick() {
		super.lazyTick();
		this.attachWheel();
	}

	@Override
	public void remove(){
		super.remove();
		detachWheel();
	}

	protected void refreshWheelSpeed() {
		if (this.poweredWheel != null) {
			this.poweredWheel.setRotation(this.appliedSpeed, this.appliedCapacity);
		}
	}
}
