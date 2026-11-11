package org.nina_iseri.steam_and_heat.content.engine.steam_engine;

import com.simibubi.create.foundation.item.TooltipHelper;

import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.ClientUtils;
import org.nina_iseri.steam_and_heat.client.ModBlockPartials;
import org.nina_iseri.steam_and_heat.content.engine.CommonEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModFluids;
import org.nina_iseri.steam_and_heat.registrate.ModItems;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 蒸汽引擎方块的公共父类。
 *
 * <p>蒸汽引擎与斯特林引擎的区别在于：它不需要“热源”，而是从正后方的锅炉抽取蒸汽，
 * 因此这里只负责处理方块状态、物品提示和蒸汽容器交互；具体的蒸汽结算在
 * {@link SteamEngineBlockEntity} 中完成。</p>
 */
public abstract class SteamEngineBlock extends CommonEngineBlock {

    /** 引擎是否正在工作（仅用于渲染和粒子效果，不代表内部蒸汽量）。 */
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected SteamEngineBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT));
    }

    @Override
    protected boolean isValidBase(BlockState baseState, Level level, BlockPos basePos) {
        // 蒸汽引擎只要求背后有方块即可；能否抽到蒸汽由 SteamEngineBlockEntity 在每个 tick 判断。
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
            TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.engine.brief")
                    .style(ChatFormatting.GOLD).addTo(tooltip);
            if (ClientUtils.hasGoggles()) {
                new LangBuilder(SteamAndHeat.MODID)
                        .translate("tooltip.engine.steamconsume", getSteamConsumptionPerTick())
                        .style(ChatFormatting.GOLD).addTo(tooltip);
            }
        } else {
            tooltip.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY, false));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }

    /**
     * 该等级引擎每 tick 消耗的蒸汽量，用于物品提示。
     */
    protected abstract int getSteamConsumptionPerTick();

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() != ModItems.PRESSURIZED_STEAM_CONTAINER.get()) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SteamEngineBlockEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (fluidHandler == null) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        fluidHandler.fill(new FluidStack(ModFluids.STEAM.get(), 1000), IFluidHandler.FluidAction.EXECUTE);
        player.getItemInHand(hand).shrink(1);

        ItemStack emptyContainer = new ItemStack(ModItems.PRESSURIZED_GAS_CONTAINER.get());
        if (!player.addItem(emptyContainer)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), emptyContainer));
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public PartialModel getFrameModel() {
        return ModBlockPartials.FURNACE_GENERATOR_FRAME;
    }
}
