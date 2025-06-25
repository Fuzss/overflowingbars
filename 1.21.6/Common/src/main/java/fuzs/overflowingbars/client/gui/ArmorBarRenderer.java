package fuzs.overflowingbars.client.gui;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ArmorBarRenderer {

    public static void renderArmorBar(GuiGraphics guiGraphics, int posX, int posY, Player player) {
        Profiler.get().push(OverflowingBars.id("armor").toString());
        ClientConfig.AbstractArmorRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).armor;
        int armorPoints = player.getArmorValue();
        renderArmorBar(guiGraphics, posX, posY, 18, armorPoints, true, false, config);
        Profiler.get().pop();
    }

    public static void renderToughnessBar(GuiGraphics guiGraphics, int posX, int posY, Player player, boolean left, boolean vanillaLike) {
        Profiler.get().push(OverflowingBars.id("toughness").toString());
        ClientConfig.ToughnessRowConfig config = OverflowingBars.CONFIG.get(ClientConfig.class).toughness;
        int armorPoints = Mth.floor(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        renderArmorBar(guiGraphics, posX, posY, left ? 9 : 0, armorPoints, left, vanillaLike, config);
        Profiler.get().pop();
    }

    public static void renderArmorBar(GuiGraphics guiGraphics, int posX, int posY, int vOffset, int armorPoints, boolean left, boolean vanillaLike, ClientConfig.AbstractArmorRowConfig config) {
        if (armorPoints <= 0) return;
        boolean inverse = !vanillaLike && config.inverseColoring;
        boolean skip = !vanillaLike && config.skipEmptyArmorPoints;
        int lastRowArmorPoints = 0;
        if (!vanillaLike) {
            if (config.colorizeFirstRow || armorPoints > 20) {
                lastRowArmorPoints = (armorPoints - 1) % 20 + 1;
            }
        }
        for (int currentArmorPoint = 0; currentArmorPoint < 10; ++currentArmorPoint) {
            int startX = posX + (left ? currentArmorPoint * 8 : -currentArmorPoint * 8 - 9);
            if (currentArmorPoint * 2 + 1 < lastRowArmorPoints) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                        BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION,
                        startX,
                        posY,
                        inverse ? 18 : 36,
                        vOffset,
                        9,
                        9,
                        256,
                        256);
            } else if (currentArmorPoint * 2 + 1 == lastRowArmorPoints) {
                if (armorPoints > 20) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                            BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION,
                            startX,
                            posY,
                            inverse ? 54 : 27,
                            vOffset,
                            9,
                            9,
                            256,
                            256);
                } else {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                            BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION,
                            startX,
                            posY,
                            inverse ? 9 : 45,
                            vOffset,
                            9,
                            9,
                            256,
                            256);
                }
            } else if (currentArmorPoint * 2 + 1 < armorPoints) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                        BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION,
                        startX,
                        posY,
                        inverse ? 36 : 18,
                        vOffset,
                        9,
                        9,
                        256,
                        256);
            } else if (currentArmorPoint * 2 + 1 == armorPoints) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                        BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION,
                        startX,
                        posY,
                        inverse ? 45 : 9,
                        vOffset,
                        9,
                        9,
                        256,
                        256);
            } else if (!skip && currentArmorPoint * 2 + 1 > armorPoints) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                        BarOverlayRenderer.OVERFLOWING_ICONS_LOCATION,
                        startX,
                        posY,
                        0,
                        vOffset,
                        9,
                        9,
                        256,
                        256);
            }
        }
    }
}
