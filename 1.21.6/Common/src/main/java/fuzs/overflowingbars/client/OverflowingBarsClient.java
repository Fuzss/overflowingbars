package fuzs.overflowingbars.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.gui.HealthBarRenderer;
import fuzs.overflowingbars.client.handler.GuiLayerHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.CustomizeChatPanelCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

public class OverflowingBarsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTickEvents.START.register(HealthBarRenderer.INSTANCE::onStartTick);
        CustomizeChatPanelCallback.EVENT.register(GuiLayerHandler::onRenderChatPanel);
    }

    @Override
    public void onRegisterGuiLayers(GuiLayersContext context) {
        context.replaceGuiLayer(GuiLayersContext.PLAYER_HEALTH, (LayeredDraw.Layer layer) -> {
            return (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                if (GuiLayerHandler.onRenderPlayerHealth(Minecraft.getInstance().gui, guiGraphics, deltaTracker)
                        .isPass()) {
                    layer.render(guiGraphics, deltaTracker);
                }
            };
        });
        context.replaceGuiLayer(GuiLayersContext.ARMOR_LEVEL, (LayeredDraw.Layer layer) -> {
            return (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                if (GuiLayerHandler.onRenderArmorLevel(Minecraft.getInstance().gui, guiGraphics, deltaTracker)
                        .isPass()) {
                    layer.render(guiGraphics, deltaTracker);
                }
            };
        });
        context.registerGuiLayer(GuiLayersContext.VEHICLE_HEALTH,
                OverflowingBars.id("toughness_level"),
                (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                    GuiLayerHandler.onRenderToughnessLevel(Minecraft.getInstance().gui, guiGraphics, deltaTracker);
                });
    }
}
