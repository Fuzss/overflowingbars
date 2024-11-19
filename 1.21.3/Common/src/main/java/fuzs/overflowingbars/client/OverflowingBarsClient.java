package fuzs.overflowingbars.client;

import fuzs.overflowingbars.client.gui.HealthBarRenderer;
import fuzs.overflowingbars.client.handler.GuiLayerHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.CustomizeChatPanelCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiLayerEvents;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;

public class OverflowingBarsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTickEvents.START.register(HealthBarRenderer.INSTANCE::onStartTick);
        CustomizeChatPanelCallback.EVENT.register(GuiLayerHandler::onRenderChatPanel);
        RenderGuiLayerEvents.before(RenderGuiLayerEvents.PLAYER_HEALTH).register(GuiLayerHandler::onRenderPlayerHealth);
        RenderGuiLayerEvents.before(RenderGuiLayerEvents.ARMOR_LEVEL).register(GuiLayerHandler::onRenderArmorLevel);
        // register this properly on NeoForge, so that other mods can interact with the layer
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isFabricLike()) {
            RenderGuiLayerEvents.after(RenderGuiLayerEvents.AIR_LEVEL).register(
                    GuiLayerHandler::onRenderToughnessLevel);
        }
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
//        context.addRepositorySource(PackResourcesHelper.buildClientPack(OverflowingBars.id("dynamic"),
//                DynamicallyCopiedPackResources.create(), false
//        ));
    }
}
