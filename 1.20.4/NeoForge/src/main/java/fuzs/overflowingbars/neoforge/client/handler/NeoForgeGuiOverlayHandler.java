package fuzs.overflowingbars.neoforge.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.handler.BarOverlayRenderer;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;

public class NeoForgeGuiOverlayHandler {
    public static final IGuiOverlay TOUGHNESS_LEVEL = (ExtendedGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) -> {
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        if (!config.armorToughnessBar) return;
        Minecraft minecraft = gui.getMinecraft();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
            RenderSystem.enableBlend();
            BarOverlayRenderer.renderToughnessLevelBar(guiGraphics, screenWidth, screenHeight, minecraft, config.leftSide ? gui.leftHeight : gui.rightHeight, config.allowCount, config.leftSide, !config.allowLayers);
            RenderSystem.disableBlend();
            if (ChatOffsetHelper.toughnessRow(minecraft.player)) {
                if (config.leftSide) {
                    gui.leftHeight += 10;
                } else {
                    gui.rightHeight += 10;
                }
            }
        }
    };

    public static void onBeforeRenderGuiOverlay(RenderGuiOverlayEvent.Pre evt) {
        handlePlayerHealthOverlay(evt);
        handleArmorLevelOverlay(evt);
    }

    private static void handlePlayerHealthOverlay(RenderGuiOverlayEvent.Pre evt) {
        if (evt.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() && OverflowingBars.CONFIG.get(ClientConfig.class).health.allowLayers) {
            Minecraft minecraft = Minecraft.getInstance();
            ExtendedGui gui = ((ExtendedGui) minecraft.gui);
            if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                RenderSystem.enableBlend();
                BarOverlayRenderer.renderHealthLevelBars(evt.getGuiGraphics(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight(), minecraft, gui.leftHeight, OverflowingBars.CONFIG.get(ClientConfig.class).health.allowCount);
                RenderSystem.disableBlend();
                gui.leftHeight += ChatOffsetHelper.twoHealthRows(minecraft.player) ? 20 : 10;
            }
            evt.setCanceled(true);
        }
    }

    private static void handleArmorLevelOverlay(RenderGuiOverlayEvent.Pre evt) {
        if (evt.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type() && OverflowingBars.CONFIG.get(ClientConfig.class).armor.allowLayers) {
            Minecraft minecraft = Minecraft.getInstance();
            ExtendedGui gui = ((ExtendedGui) minecraft.gui);
            if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                RenderSystem.enableBlend();
                BarOverlayRenderer.renderArmorLevelBar(evt.getGuiGraphics(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight(), minecraft, gui.leftHeight, OverflowingBars.CONFIG.get(ClientConfig.class).armor.allowCount, false);
                RenderSystem.disableBlend();
                if (ChatOffsetHelper.armorRow(minecraft.player)) {
                    gui.leftHeight += 10;
                }
            }
            evt.setCanceled(true);
        }
    }
}
