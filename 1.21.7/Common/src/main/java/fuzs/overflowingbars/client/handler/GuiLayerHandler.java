package fuzs.overflowingbars.client.handler;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.gui.BarOverlayRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.gui.v2.ScreenHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class GuiLayerHandler {
    public static final ResourceLocation TOUGHNESS_LEVEL_LEFT_LOCATION = OverflowingBars.id("toughness_level/left");
    public static final ResourceLocation TOUGHNESS_LEVEL_RIGHT_LOCATION = OverflowingBars.id("toughness_level/right");
    public static final ResourceLocation TOUGHNESS_LEVEL_RIGHT_MOUNTED_LOCATION = OverflowingBars.id(
            "toughness_level/right/mounted");

    public static EventResult onRenderPlayerHealth(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.IconRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).health;
        Player player = getCameraPlayer();
        if (config.allowLayers && player != null) {
            int guiHeight = ScreenHelper.getLeftStatusBarHeight(GuiLayersContext.PLAYER_HEALTH);
            guiHeight += config.manualRowShift();
            BarOverlayRenderer.renderHealthLevelBars(guiGraphics, player, guiHeight, config.allowCount);
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static EventResult onRenderArmorLevel(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ClientConfig.AbstractArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        Player player = getCameraPlayer();
        if (config.allowLayers && player != null) {
            int guiHeight = ScreenHelper.getLeftStatusBarHeight(GuiLayersContext.ARMOR_LEVEL);
            guiHeight += config.manualRowShift();
            BarOverlayRenderer.renderArmorLevelBar(guiGraphics, player, guiHeight, config.allowCount);
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static void onRenderToughnessLevel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, ResourceLocation heightProviderLocation, boolean leftSide) {
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        Player player = getCameraPlayer();
        if (config.leftSide == leftSide && config.armorToughnessBar && player != null) {
            int guiHeight;
            if (leftSide) {
                guiHeight = ScreenHelper.getLeftStatusBarHeight(heightProviderLocation);
            } else {
                guiHeight = ScreenHelper.getRightStatusBarHeight(heightProviderLocation);
            }
            guiHeight += config.manualRowShift();
            BarOverlayRenderer.renderToughnessLevelBar(guiGraphics,
                    player,
                    guiHeight,
                    config.allowCount,
                    leftSide,
                    !config.allowLayers);
        }
    }

    @Nullable
    private static Player getCameraPlayer() {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && minecraft.gameMode.canHurtPlayer()) {
            return minecraft.gui.getCameraPlayer();
        } else {
            return null;
        }
    }

    public static void onRenderChatPanel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, MutableInt posX, MutableInt posY) {
        if (!OverflowingBars.CONFIG.get(ClientConfig.class).armor.moveChatAboveArmor) return;
        posY.mapInt(value -> value - ChatOffsetHelper.getChatOffsetY());
    }
}
