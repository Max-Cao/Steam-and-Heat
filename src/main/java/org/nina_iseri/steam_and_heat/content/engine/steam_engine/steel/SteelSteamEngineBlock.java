package org.nina_iseri.steam_and_heat.content.engine.steam_engine.steel;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.TooltipHelper;

import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.ClientUtils;
import org.nina_iseri.steam_and_heat.content.engine.steam_engine.SteamEngineBlock;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import org.nina_iseri.steam_and_heat.registrate.ModCommonConfig;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;

public class SteelSteamEngineBlock extends SteamEngineBlock implements IBE<SteelSteamEngineTileEntity> {
    public SteelSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public BlockEntityType<? extends SteelSteamEngineTileEntity> getBlockEntityType() {
        return ModBlockEntities.STEEL_STEAM_ENGINE.get();
    }
    @Override
	public void appendHoverText(ItemStack i, Item.TooltipContext w, List<Component> t,
			TooltipFlag f) {
    	if(Screen.hasShiftDown()) {
    		new LangBuilder(SteamAndHeat.MODID).translate("tooltip.engine.brief").style(ChatFormatting.GOLD).addTo(t);
    		if(ClientUtils.hasGoggles()) 
    		new LangBuilder(SteamAndHeat.MODID).translate("tooltip.engine.steamconsume",ModCommonConfig.COMMON.steelFlywheelSteamConsumptionPerTick.get()).style(ChatFormatting.GOLD).addTo(t);
    	}else {
    		t.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY,false));
    	}
		super.appendHoverText(i,w,t,f);
	}
    @Override
    public Class<SteelSteamEngineTileEntity> getBlockEntityClass() {
        return SteelSteamEngineTileEntity.class;
    }
}
