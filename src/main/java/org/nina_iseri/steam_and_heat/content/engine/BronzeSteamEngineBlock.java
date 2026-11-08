package org.nina_iseri.steam_and_heat.content.engine;

import java.util.List;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.TooltipHelper;
import org.nina_iseri.steam_and_heat.ModCommonConfig;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.client.ClientUtils;
import org.nina_iseri.steam_and_heat.registrate.ModBlockEntities;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class BronzeSteamEngineBlock extends SteamEngineBlock implements IBE<BronzeSteamEngineTileEntity> {
    public BronzeSteamEngineBlock(Properties builder) {
        super(builder);
    }

    @Override
    public BlockEntityType<? extends BronzeSteamEngineTileEntity> getBlockEntityType() {
        return ModBlockEntities.BRONZE_STEAM_ENGINE.get();
    }

    @Override
    public Class<BronzeSteamEngineTileEntity> getBlockEntityClass() {
        return BronzeSteamEngineTileEntity.class;
    }

    @Override
    public void appendHoverText(ItemStack i, Item.TooltipContext w, List<Component> t,
            TooltipFlag f) {
        if (Screen.hasShiftDown()) {
            new LangBuilder(SteamAndHeat.MODID).translate("tooltip.engine.brief").style(ChatFormatting.GOLD).addTo(t);
            if (ClientUtils.hasGoggles())
                new LangBuilder(SteamAndHeat.MODID)
                        .translate("tooltip.engine.steamconsume",
                                ModCommonConfig.COMMON.bronzeFlywheelSteamConsumptionPerTick.get())
                        .style(ChatFormatting.GOLD).addTo(t);
        } else {
            t.add(TooltipHelper.holdShift(FontHelper.Palette.GRAY, false));
        }
        super.appendHoverText(i, w, t, f);
    }
}
