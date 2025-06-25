package fuzs.overflowingbars.client.handler;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.gui.BarOverlayRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.client.gui.v2.GuiHeightHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class GuiLayerHandler {

    public static EventResult onRenderPlayerHealth(Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.IconRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).health;
        if (!gui.minecraft.options.hideGui && gui.minecraft.getCameraEntity() instanceof Player &&
                gui.minecraft.gameMode.canHurtPlayer() && config.allowLayers) {
            int guiLeftHeight = GuiHeightHelper.getLeftHeight(gui) + config.manualRowShift();
            BarOverlayRenderer.renderHealthLevelBars(gui.minecraft, guiGraphics, guiLeftHeight, config.allowCount);
            GuiHeightHelper.addLeftHeight(gui,
                    ChatOffsetHelper.twoHealthRows(gui.minecraft.player) ? 20 : 10 + config.manualRowShift());
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static EventResult onRenderArmorLevel(Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.AbstractArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        if (!gui.minecraft.options.hideGui && gui.minecraft.getCameraEntity() instanceof Player &&
                gui.minecraft.gameMode.canHurtPlayer() && config.allowLayers) {
            int guiLeftHeight = GuiHeightHelper.getLeftHeight(gui) + config.manualRowShift();
            BarOverlayRenderer.renderArmorLevelBar(gui.minecraft, guiGraphics, guiLeftHeight, config.allowCount, false);
            if (ChatOffsetHelper.armorRow(gui.minecraft.player)) {
                GuiHeightHelper.addLeftHeight(gui, 10 + config.manualRowShift());
            }
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static void onRenderToughnessLevel(Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        if (!gui.minecraft.options.hideGui && gui.minecraft.getCameraEntity() instanceof Player &&
                gui.minecraft.gameMode.canHurtPlayer() && config.armorToughnessBar) {
            int guiHeight;
            if (config.leftSide) {
                guiHeight = GuiHeightHelper.getLeftHeight(gui);
            } else {
                guiHeight = GuiHeightHelper.getRightHeight(gui);
            }
            guiHeight += config.manualRowShift();
            BarOverlayRenderer.renderToughnessLevelBar(gui.minecraft,
                    guiGraphics,
                    guiHeight,
                    config.allowCount,
                    config.leftSide,
                    !config.allowLayers);
            if (ChatOffsetHelper.toughnessRow(gui.minecraft.player)) {
                if (config.leftSide) {
                    GuiHeightHelper.addLeftHeight(gui, 10 + config.manualRowShift());
                } else {
                    GuiHeightHelper.addRightHeight(gui, 10 + config.manualRowShift());
                }
            }
        }
    }

    public static void onRenderChatPanel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, MutableInt posX, MutableInt posY) {
        if (!OverflowingBars.CONFIG.get(ClientConfig.class).armor.moveChatAboveArmor) return;
        posY.mapInt(value -> value - ChatOffsetHelper.getChatOffsetY());
    }
}
