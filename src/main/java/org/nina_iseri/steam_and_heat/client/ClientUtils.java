package org.nina_iseri.steam_and_heat.client;

import com.simibubi.create.AllItems;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    public static Level getClientWorld() {
        return mc().level;
    }
    public static boolean hasGoggles() {
    	return AllItems.GOGGLES.isIn(mc().player.getItemBySlot(EquipmentSlot.HEAD));
    }
}
