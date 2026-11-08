package org.nina_iseri.steam_and_heat.network.ponder;

import com.simibubi.create.foundation.ponder.PonderWorldBlockEntityFix;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import org.nina_iseri.steam_and_heat.SteamAndHeat;
import org.nina_iseri.steam_and_heat.registrate.ModBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;

public class SPPonderPlugin implements PonderPlugin {

    public static final ResourceLocation STEAM = SteamAndHeat.rl("steam");

    @Override
    public String getModId() {
        return SteamAndHeat.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(ModBlocks.BRONZE_STEAM_ENGINE, ModBlocks.CAST_IRON_STEAM_ENGINE, ModBlocks.STEEL_STEAM_ENGINE)
                .addStoryBoard("steam_engine", SPScenes::steamEngine, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(ModBlocks.BRONZE_STEAM_ENGINE, ModBlocks.CAST_IRON_STEAM_ENGINE, ModBlocks.STEEL_STEAM_ENGINE)
                .addStoryBoard("boiler", SPScenes::steamBoiler, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(ModBlocks.BRONZE_BOILER, ModBlocks.BRONZE_BURNER, ModBlocks.CAST_IRON_BURNER, ModBlocks.CAST_IRON_BOILER, ModBlocks.STEEL_BURNER, ModBlocks.STEEL_BOILER)
                .addStoryBoard("boiler", SPScenes::steamBoiler, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

        HELPER.forComponents(ModBlocks.BRONZE_FLYWHEEL, ModBlocks.CAST_IRON_FLYWHEEL, ModBlocks.STEEL_FLYWHEEL)
                .addStoryBoard("steam_engine", SPScenes::steamFlywheel, AllCreatePonderTags.KINETIC_SOURCES, STEAM);

    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.registerTag(STEAM)
                .addToIndex()
                .item(ModBlocks.BRONZE_STEAM_ENGINE.get(), true, false)
                .title("Steam")
                .description("Components related to steam production and usage")
                .register();
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {

    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {
        PonderWorldBlockEntityFix.fixControllerBlockEntities(ponderLevel);
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {

    }
}
