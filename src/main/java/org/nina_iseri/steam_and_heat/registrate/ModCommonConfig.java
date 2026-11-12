package org.nina_iseri.steam_and_heat.registrate;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModCommonConfig {
    public static class Common {

        public final ModConfigSpec.IntValue dynamoFeMaxOut;
        public final ModConfigSpec.IntValue dynamoFeCapacity;
        public final ModConfigSpec.IntValue dynamoImpact;
        public final ModConfigSpec.DoubleValue dynamoEfficiency;

        public final ModConfigSpec.DoubleValue bronzeCogwheelImpact;
        public final ModConfigSpec.DoubleValue castIronCogwheelImpact;
        public final ModConfigSpec.DoubleValue steelCogwheelImpact;

        Common(ModConfigSpec.Builder builder) {
            // 燃烧室、锅炉、引擎的数值现在由数据包配方驱动，不再放在配置里。
            builder.push("dynamo")
                    .comment("If dynamo is disabled in the server config, the following will be ignored!");
            {
                dynamoFeMaxOut = builder.defineInRange("dynamoFeMaxOut", 256, 0, 8192);
                dynamoFeCapacity = builder.defineInRange("dynamoFeCapacity", 2048, 0, 8192);
                dynamoImpact = builder.defineInRange("dynamoImpact", 16, 0, 8192);
                dynamoEfficiency = builder.defineInRange("dynamoEfficiency", 0.75D, 0, 1);
            }
            builder.pop();

            builder.push("cogwheel").comment(
                    "For those who want to make the game more challenging, you can add stress impact to cogwheels!");
            {
                bronzeCogwheelImpact = builder.defineInRange("bronzeCogwheelImpact", 0.0D, 0, 1);
                castIronCogwheelImpact = builder.defineInRange("castIronCogwheelImpact", 0.0D, 0, 1);
                steelCogwheelImpact = builder.defineInRange("steelCogwheelImpact", 0.0D, 0, 1);
            }
            builder.pop();
        }
    }

    public static class Server {

        Server(ModConfigSpec.Builder builder) {
        }
    }

    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec SERVER_CONFIG;
    public static final Common COMMON;
    public static final Server SERVER;

    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        ModConfigSpec.Builder serverBuilder = new ModConfigSpec.Builder();
        COMMON = new Common(commonBuilder);
        SERVER = new Server(serverBuilder);
        SERVER_CONFIG = serverBuilder.build();
        COMMON_CONFIG = commonBuilder.build();
    }

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        modContainer.registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }
}
