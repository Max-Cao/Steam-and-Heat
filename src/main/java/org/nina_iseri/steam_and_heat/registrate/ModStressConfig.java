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
		return switch (mat) {
			case "bronze" -> ModCommonConfig.COMMON.bronzeFlywheelCapacity::get;
			case "cast" -> ModCommonConfig.COMMON.castIronFlywheelCapacity::get;
			case "steel" -> ModCommonConfig.COMMON.steelFlywheelCapacity::get;
			default -> null;
		};
	}

	public static DoubleSupplier getImpact(Block arg0) {
		ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(arg0);
		String[] mat = registryName.getPath().split("_");
		if (!mat[mat.length - 1].equals("cogwheel")) {
			return null;
		}
		return switch (mat[0]) {
			case "bronze" -> ModCommonConfig.COMMON.bronzeCogwheelImpact::get;
			case "cast" -> ModCommonConfig.COMMON.castIronCogwheelImpact::get;
			case "steel" -> ModCommonConfig.COMMON.steelCogwheelImpact::get;
			default -> null;
		};
	}

	public static void register() {
		BlockStressValues.IMPACTS.registerProvider(ModStressConfig::getImpact);
		BlockStressValues.CAPACITIES.registerProvider(ModStressConfig::getCapacity);
	}
}
