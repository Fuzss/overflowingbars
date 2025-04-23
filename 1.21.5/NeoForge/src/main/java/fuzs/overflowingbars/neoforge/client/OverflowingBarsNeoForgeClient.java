package fuzs.overflowingbars.neoforge.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.OverflowingBarsClient;
import fuzs.overflowingbars.client.handler.GuiLayerHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = OverflowingBars.MOD_ID, dist = Dist.CLIENT)
public class OverflowingBarsNeoForgeClient {

    public OverflowingBarsNeoForgeClient(ModContainer modContainer) {
        ClientModConstructor.construct(OverflowingBars.MOD_ID, OverflowingBarsClient::new);
        registerLoadingHandlers(modContainer.getEventBus());
        registerModIntegrations();
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final RegisterGuiLayersEvent evt) -> {
            evt.registerAbove(VanillaGuiLayers.VEHICLE_HEALTH, OverflowingBars.id("toughness_level"),
                    (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                        GuiLayerHandler.onRenderToughnessLevel(Minecraft.getInstance().gui, guiGraphics, deltaTracker);
                    }
            );
        });
    }

    private static void registerModIntegrations() {
        if (ModLoaderEnvironment.INSTANCE.isModLoaded("appleskin")) {
            // just disable this, it's not too useful anyway and would be annoying to get to work properly with the stacked rendering
            ResourceLocation resourceLocation = ResourceLocationHelper.parse("appleskin:health_restored");
            NeoForge.EVENT_BUS.addListener((final RenderGuiLayerEvent.Pre evt) -> {
                if (evt.getName().equals(resourceLocation)) {
                    evt.setCanceled(true);
                }
            });
        }
    }
}
