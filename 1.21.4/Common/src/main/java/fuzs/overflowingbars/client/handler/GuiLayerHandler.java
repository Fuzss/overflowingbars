package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.gui.BarOverlayRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class GuiLayerHandler {

    public static EventResult onRenderPlayerHealth(Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.IconRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).health;
        if (!gui.minecraft.options.hideGui && gui.minecraft.getCameraEntity() instanceof Player && gui.minecraft.gameMode.canHurtPlayer() && config.allowLayers) {
            int guiLeftHeight = ClientAbstractions.INSTANCE.getGuiLeftHeight(gui) + config.manualRowShift();
            BarOverlayRenderer.renderHealthLevelBars(gui.minecraft, guiGraphics, guiLeftHeight, config.allowCount);
            BarOverlayRenderer.resetRenderState();
            ClientAbstractions.INSTANCE.addGuiLeftHeight(gui,
                    ChatOffsetHelper.twoHealthRows(gui.minecraft.player) ? 20 : 10 + config.manualRowShift()
            );
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static EventResult onRenderArmorLevel(Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.AbstractArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        if (!gui.minecraft.options.hideGui && gui.minecraft.getCameraEntity() instanceof Player && gui.minecraft.gameMode.canHurtPlayer() && config.allowLayers) {
            RenderSystem.enableBlend();
            int guiLeftHeight = ClientAbstractions.INSTANCE.getGuiLeftHeight(gui) + config.manualRowShift();
            BarOverlayRenderer.renderArmorLevelBar(gui.minecraft, guiGraphics, guiLeftHeight, config.allowCount, false);
            RenderSystem.disableBlend();
            BarOverlayRenderer.resetRenderState();
            if (ChatOffsetHelper.armorRow(gui.minecraft.player)) {
                ClientAbstractions.INSTANCE.addGuiLeftHeight(gui, 10 + config.manualRowShift());
            }
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static EventResult onRenderToughnessLevel(Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        if (!gui.minecraft.options.hideGui && gui.minecraft.getCameraEntity() instanceof Player && gui.minecraft.gameMode.canHurtPlayer() &&
                config.armorToughnessBar) {
            RenderSystem.enableBlend();
            int guiHeight;
            if (config.leftSide) {
                guiHeight = ClientAbstractions.INSTANCE.getGuiLeftHeight(gui);
            } else {
                guiHeight = ClientAbstractions.INSTANCE.getGuiRightHeight(gui);
            }
            guiHeight += config.manualRowShift();
            BarOverlayRenderer.renderToughnessLevelBar(gui.minecraft, guiGraphics, guiHeight, config.allowCount,
                    config.leftSide, !config.allowLayers
            );
            RenderSystem.disableBlend();
            if (ChatOffsetHelper.toughnessRow(gui.minecraft.player)) {
                if (config.leftSide) {
                    ClientAbstractions.INSTANCE.addGuiLeftHeight(gui, 10 + config.manualRowShift());
                } else {
                    ClientAbstractions.INSTANCE.addGuiRightHeight(gui, 10 + config.manualRowShift());
                }
            }
        }

        return EventResult.PASS;
    }

    public static void onRenderChatPanel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, MutableInt posX, MutableInt posY) {
        if (!OverflowingBars.CONFIG.get(ClientConfig.class).armor.moveChatAboveArmor) return;
        posY.mapInt(value -> value - ChatOffsetHelper.getChatOffsetY());
    }
}
