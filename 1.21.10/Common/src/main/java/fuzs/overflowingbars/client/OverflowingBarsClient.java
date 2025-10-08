package fuzs.overflowingbars.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.gui.HealthBarRenderer;
import fuzs.overflowingbars.client.handler.GuiLayerHandler;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.CustomizeChatPanelCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

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
        if (OverflowingBars.CONFIG.get(ClientConfig.class).health.allowHealthLayers) {
            context.replaceGuiLayer(GuiLayersContext.PLAYER_HEALTH, (GuiLayersContext.Layer layer) -> {
                return GuiLayerHandler::onRenderPlayerHealth;
            });
            context.addLeftStatusBarHeightProvider(GuiLayersContext.PLAYER_HEALTH, (Player player) -> {
                return (ChatOffsetHelper.twoHealthRows(player) ? 20 : 10)
                        + OverflowingBars.CONFIG.get(ClientConfig.class).health.manualRowShift();
            });
        }
        if (OverflowingBars.CONFIG.get(ClientConfig.class).armor.allowArmorLayers) {
            context.replaceGuiLayer(GuiLayersContext.ARMOR_LEVEL, (GuiLayersContext.Layer layer) -> {
                return GuiLayerHandler::onRenderArmorLevel;
            });
            context.addLeftStatusBarHeightProvider(GuiLayersContext.ARMOR_LEVEL, (Player player) -> {
                if (ChatOffsetHelper.armorRow(player)) {
                    return 10 + OverflowingBars.CONFIG.get(ClientConfig.class).armor.manualRowShift();
                } else {
                    return 0;
                }
            });
        }
        context.registerGuiLayer(GuiLayersContext.ARMOR_LEVEL,
                GuiLayerHandler.TOUGHNESS_LEVEL_LEFT_LOCATION,
                (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                    GuiLayerHandler.onRenderToughnessLevel(guiGraphics,
                            deltaTracker,
                            GuiLayerHandler.TOUGHNESS_LEVEL_LEFT_LOCATION,
                            true);
                });
        context.registerGuiLayer(GuiLayersContext.FOOD_LEVEL,
                GuiLayerHandler.TOUGHNESS_LEVEL_RIGHT_LOCATION,
                (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                    Gui gui = Minecraft.getInstance().gui;
                    int vehicleMaxHearts = gui.getVehicleMaxHearts(gui.getPlayerVehicleWithHealth());
                    if (vehicleMaxHearts == 0) {
                        GuiLayerHandler.onRenderToughnessLevel(guiGraphics,
                                deltaTracker,
                                GuiLayerHandler.TOUGHNESS_LEVEL_RIGHT_LOCATION,
                                false);
                    }
                });
        context.registerGuiLayer(GuiLayersContext.VEHICLE_HEALTH,
                GuiLayerHandler.TOUGHNESS_LEVEL_RIGHT_MOUNTED_LOCATION,
                (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                    Gui gui = Minecraft.getInstance().gui;
                    int vehicleMaxHearts = gui.getVehicleMaxHearts(gui.getPlayerVehicleWithHealth());
                    if (vehicleMaxHearts != 0) {
                        GuiLayerHandler.onRenderToughnessLevel(guiGraphics,
                                deltaTracker,
                                GuiLayerHandler.TOUGHNESS_LEVEL_RIGHT_MOUNTED_LOCATION,
                                false);
                    }
                });
        context.addLeftStatusBarHeightProvider(GuiLayerHandler.TOUGHNESS_LEVEL_LEFT_LOCATION, (Player player) -> {
            if (ChatOffsetHelper.toughnessRow(player)
                    && OverflowingBars.CONFIG.get(ClientConfig.class).toughness.leftSide) {
                return 10 + OverflowingBars.CONFIG.get(ClientConfig.class).toughness.manualRowShift();
            } else {
                return 0;
            }
        });
        context.addRightStatusBarHeightProvider(GuiLayerHandler.TOUGHNESS_LEVEL_RIGHT_LOCATION, (Player player) -> {
            Gui gui = Minecraft.getInstance().gui;
            int vehicleMaxHearts = gui.getVehicleMaxHearts(gui.getPlayerVehicleWithHealth());
            if (vehicleMaxHearts == 0 && ChatOffsetHelper.toughnessRow(player) && !OverflowingBars.CONFIG.get(
                    ClientConfig.class).toughness.leftSide) {
                return 10 + OverflowingBars.CONFIG.get(ClientConfig.class).toughness.manualRowShift();
            } else {
                return 0;
            }
        });
        context.addRightStatusBarHeightProvider(GuiLayerHandler.TOUGHNESS_LEVEL_RIGHT_MOUNTED_LOCATION,
                (Player player) -> {
                    Gui gui = Minecraft.getInstance().gui;
                    int vehicleMaxHearts = gui.getVehicleMaxHearts(gui.getPlayerVehicleWithHealth());
                    if (vehicleMaxHearts != 0 && ChatOffsetHelper.toughnessRow(player) && !OverflowingBars.CONFIG.get(
                            ClientConfig.class).toughness.leftSide) {
                        return 10 + OverflowingBars.CONFIG.get(ClientConfig.class).toughness.manualRowShift();
                    } else {
                        return 0;
                    }
                });
    }
}
