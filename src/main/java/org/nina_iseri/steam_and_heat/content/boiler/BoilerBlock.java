package org.nina_iseri.steam_and_heat.content.boiler;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import org.nina_iseri.steam_and_heat.client.ClientUtils;
import net.createmod.catnip.lang.FontHelper;
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
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection()))
			return ItemInteractionResult.SUCCESS;
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hitResult) {
		if (FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, level, pos, hitResult.getDirection()))
			return InteractionResult.SUCCESS;
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
		return true;
	}

	public BoilerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getAnalogOutputSignal(BlockState b, Level w, BlockPos p) {
		BlockEntity te = w.getBlockEntity(p);
		if (te instanceof BoilerBlockEntity) {
			BoilerBlockEntity boiler = (BoilerBlockEntity) te;
			return boiler.output.getFluidAmount() * 15 / boiler.output.getCapacity();
		}
		return b.getAnalogOutputSignal(w, p);
	}

	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public abstract int getHuConsume();

	@Override
	public void appendHoverText(ItemStack i, Item.TooltipContext w, List<Component> t, TooltipFlag f) {
		if (Screen.hasShiftDown()) {
			t.add(Component.translatable("tooltip.steam_and_heat.boiler.brief").withStyle(ChatFormatting.GOLD));
			if (ClientUtils.hasGoggles()) {
				t.add(Component.translatable("tooltip.steam_and_heat.boiler.danger").withStyle(ChatFormatting.RED));
				t.add(Component.translatable("tooltip.steam_and_heat.boiler.huconsume", this.getHuConsume())
						.withStyle(ChatFormatting.GOLD));
				t.add(Component.translatable("tooltip.steam_and_heat.boiler.waterconsume",
						((int) Math.ceil(this.getHuConsume() / 120.0))).withStyle(ChatFormatting.AQUA));
				t.add(Component.translatable("tooltip.steam_and_heat.boiler.steamproduce", this.getHuConsume() / 10)
						.withStyle(ChatFormatting.GOLD));
			}
		} else {
			t.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY, false));
		}
		if (Screen.hasControlDown()) {
			t.add(Component.translatable("tooltip.steam_and_heat.boiler.redstone").withStyle(ChatFormatting.RED));
		} else {
			t.add(CreateLang
					.translate("tooltip.holdForControls",
							CreateLang.translate("tooltip.keyCtrl").style(ChatFormatting.GRAY))
					.style(ChatFormatting.DARK_GRAY).component());
		}
		super.appendHoverText(i, w, t, f);
	}

	// 踩上去会受伤
	public void stepOn(Level level, BlockPos pos, Entity entity) {
		BlockEntity block_entity = level.getBlockEntity(pos);
		if (block_entity instanceof BoilerBlockEntity boiler &&
				(boiler.heat_last > 0 || !boiler.output.isEmpty()) &&
				entity instanceof LivingEntity) {
			entity.hurt(level.damageSources().hotFloor(), 2);
		}
	}
}
