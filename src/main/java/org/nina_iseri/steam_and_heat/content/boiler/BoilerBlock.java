package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.ClientUtils;
import org.nina_iseri.steam_and_heat.content.recipe.BoilerRecipe;
import org.nina_iseri.steam_and_heat.content.recipe.ModRecipes;
import org.nina_iseri.steam_and_heat.registrate.ModCapabilities;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;

import java.util.List;

public abstract class BoilerBlock extends Block {

    public BoilerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, level, pos,
                hitResult.getDirection())) {
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BoilerBlockEntity boiler) {
            return boiler.output.getFluidAmount() * 15 / boiler.output.getCapacity();
        }
        return state.getAnalogOutputSignal(level, pos);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
            TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.boiler.brief").style(ChatFormatting.GOLD)
                    .addTo(tooltip);
            if (ClientUtils.hasGoggles()) {
                BoilerRecipe recipe = context.level() == null ? null
                        : ModRecipes.findBoiler(context.level(), new ItemStack(this)).orElse(null);
                if (recipe != null) {
                    new LangBuilder(SteamAndHeat.MODID).translate("tooltip.boiler.huconsume", recipe.huPerTick())
                            .style(ChatFormatting.GOLD).addTo(tooltip);
                    new LangBuilder(SteamAndHeat.MODID)
                            .translate("tooltip.boiler.waterconsume", recipe.waterPerTick())
                            .style(ChatFormatting.AQUA).addTo(tooltip);
                    new LangBuilder(SteamAndHeat.MODID)
                            .translate("tooltip.boiler.steamproduce", recipe.steamPerTick())
                            .style(ChatFormatting.GOLD).addTo(tooltip);
                }
                new LangBuilder(SteamAndHeat.MODID).translate("tooltip.boiler.danger")
                        .style(ChatFormatting.RED).addTo(tooltip);
            }
        } else {
            tooltip.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY, false));
        }
        if (Screen.hasControlDown()) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.boiler.redstone").style(ChatFormatting.RED)
                    .addTo(tooltip);
        } else {
            tooltip.add(CreateLang
                    .translate("tooltip.holdForControls",
                            CreateLang.translate("tooltip.keyCtrl").style(ChatFormatting.GRAY))
                    .style(ChatFormatting.DARK_GRAY).component());
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }

    // 踩上去会受伤。
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BoilerBlockEntity boiler
                && (level.getCapability(ModCapabilities.Heat.BLOCK, pos).noHeat() || !boiler.output.isEmpty())
                && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().hotFloor(), 2);
        }
    }
}
