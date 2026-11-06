package org.nina_iseri.steam_and_heat.content.engine;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldBlockPartials;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import org.nina_iseri.steam_and_heat.registrate.ModItems;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class SteamEngineBlock extends OldEngineBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<SteamEngineBlock> CODEC = simpleCodec(SteamEngineBlock::new);

    public SteamEngineBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState()
                .setValue(FACING,
                        facing.getAxis().isVertical() ? context.getHorizontalDirection().getOpposite() : facing)
                .setValue(LIT, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT));
    }

    // @Override
    // public Class<SteamEngineTileEntity> getTileEntityClass() {
    // return SteamEngineTileEntity.class;
    // }
    //
    // @Override
    // public BlockEntityType<? extends SteamEngineTileEntity> getTileEntityType() {
    // return SPTiles.BRONZE_STEAM_ENGINE.get();
    // }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.FURNACE_ENGINE.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    public PartialModel getFrameModel() {
        return OldBlockPartials.FURNACE_GENERATOR_FRAME;
    }

    @Override
    protected boolean isValidBaseBlock(BlockState baseBlock, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.getItemInHand(hand).getItem() == ModItems.PRESSURIZED_STEAM_CONTAINER.get()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof SteamEngineTileEntity) {
                SteamEngineTileEntity steamEngine = (SteamEngineTileEntity) te;
                IFluidHandler cap = world.getCapability(Capabilities.FluidHandler.BLOCK, steamEngine.getBlockPos(),
                        null);
                cap.fill(new FluidStack(ModFluids.STEAM.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                player.getItemInHand(hand).shrink(1);
                ItemStack ret = new ItemStack(ModItems.PRESSURIZED_GAS_CONTAINER.get());
                if (!player.addItem(ret))
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ret));
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hitResult);
    }

}
