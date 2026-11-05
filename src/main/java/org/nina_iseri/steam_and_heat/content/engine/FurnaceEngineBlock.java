package org.nina_iseri.steam_and_heat.content.engine;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import org.nina_iseri.steam_and_heat.block.SPBlockPartials;
import org.nina_iseri.steam_and_heat.oldcreatestuff.FurnaceEngineInteractions;
import org.nina_iseri.steam_and_heat.oldcreatestuff.OldEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.SPBlockEntities;
import org.nina_iseri.steam_and_heat.registrate.SPBlocks;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.levelWrappers.WrappedLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class FurnaceEngineBlock extends OldEngineBlock implements IBE<FurnaceEngineTileEntity> {

    public static final MapCodec<FurnaceEngineBlock> CODEC = simpleCodec(FurnaceEngineBlock::new);

    public FurnaceEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isValidBaseBlock(BlockState baseBlock, BlockGetter world, BlockPos pos) {
        return FurnaceEngineInteractions.getHandler(baseBlock).getHeatSource(baseBlock).isValid();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.FURNACE_ENGINE.get(state.getValue(FACING));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public PartialModel getFrameModel() {
        return SPBlockPartials.FURNACE_GENERATOR_FRAME;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (worldIn instanceof WrappedLevel)
            return;
        if (worldIn.isClientSide)
            return;

        if (fromPos.equals(getBaseBlockPos(state, pos)))
            if (canSurvive(state, worldIn, pos))
                withBlockEntityDo(worldIn, pos, FurnaceEngineTileEntity::updateFurnace);
    }

    @SubscribeEvent
    public static void usingFurnaceEngineOnFurnacePreventsGUI(PlayerInteractEvent.RightClickBlock event) {
        ItemStack item = event.getItemStack();
        if (!(item.getItem() instanceof BlockItem blockItem))
            return;
        if (blockItem.getBlock() != SPBlocks.FURNACE_ENGINE.get())
            return;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (event.getFace().getAxis().isVertical())
            return;
        if (state.getBlock() instanceof AbstractFurnaceBlock)
            event.setUseBlock(TriState.FALSE); //TODO: Verify
    }

    @Override
    public Class<FurnaceEngineTileEntity> getBlockEntityClass() {
        return FurnaceEngineTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends FurnaceEngineTileEntity> getBlockEntityType() {
        return SPBlockEntities.FURNACE_ENGINE.get();
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }


}
