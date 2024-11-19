package fuzs.overflowingbars;

import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverflowingBars implements ModConstructor {
    public static final String MOD_ID = "overflowingbars";
    public static final String MOD_NAME = "Overflowing Bars";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class);

    @Override
    public void onConstructMod() {
        setupDevelopmentEnvironment();
    }

    private static void setupDevelopmentEnvironment() {
        if (!ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment()) return;
        ((RangedAttribute) Attributes.ARMOR.value()).maxValue = 1024.0;
        ((RangedAttribute) Attributes.ARMOR_TOUGHNESS.value()).maxValue = 1024.0;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
