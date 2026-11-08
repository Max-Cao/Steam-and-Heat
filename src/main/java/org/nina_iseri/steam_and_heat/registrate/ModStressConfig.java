package org.nina_iseri.steam_and_heat.registrate;

import com.simibubi.create.api.stress.BlockStressValues;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.DoubleSupplier;

public class ModStressConfig {

	public static DoubleSupplier getCapacity(Block arg0) {
		ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(arg0);
		String mat = registryName.getPath().split("_")[0];
		switch (mat) {
			case "bronze":
				return ModCommonConfig.COMMON.bronzeFlywheelCapacity::get;
			case "cast":
				return ModCommonConfig.COMMON.castIronFlywheelCapacity::get;
			case "steel":
				return ModCommonConfig.COMMON.steelFlywheelCapacity::get;
		}
		return null;
	}

	public static DoubleSupplier getImpact(Block arg0) {
		ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(arg0);
		String[] mat = registryName.getPath().split("_");
		if (mat[mat.length - 1].equals("cogwheel")) {
			switch (mat[0]) {
				case "bronze":
					return ModCommonConfig.COMMON.bronzeCogwheelImpact::get;
				case "cast":
					return ModCommonConfig.COMMON.castIronCogwheelImpact::get;
				case "steel":
					return ModCommonConfig.COMMON.steelCogwheelImpact::get;
			}
		}
		return null;
	}

	public static void register() {
		BlockStressValues.IMPACTS.registerProvider(ModStressConfig::getImpact);
		BlockStressValues.CAPACITIES.registerProvider(ModStressConfig::getCapacity);
	}

}
