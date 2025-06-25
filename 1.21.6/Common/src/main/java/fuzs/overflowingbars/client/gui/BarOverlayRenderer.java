package fuzs.overflowingbars.client.gui;

import fuzs.overflowingbars.OverflowingBars;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class BarOverlayRenderer {
    static final ResourceLocation OVERFLOWING_ICONS_LOCATION = OverflowingBars.id("textures/gui/icons.png");

    public static void renderHealthLevelBars(GuiGraphics guiGraphics, Player player, int leftHeight, boolean rowCount) {
        int posX = guiGraphics.guiWidth() / 2 - 91;
        int posY = guiGraphics.guiHeight() - leftHeight;
        HealthBarRenderer.INSTANCE.renderPlayerHealth(guiGraphics, posX, posY, player);
        if (rowCount) {
            int allHearts = Mth.ceil(player.getHealth());
            RowCountRenderer.drawBarRowCount(guiGraphics, posX - 2, posY, allHearts, true);
            int maxAbsorption = (20 - Mth.ceil(Math.min(20, allHearts) / 2.0F)) * 2;
            RowCountRenderer.drawBarRowCount(guiGraphics,
                    posX - 2,
                    posY - 10,
                    Mth.ceil(player.getAbsorptionAmount()),
                    true,
                    maxAbsorption);
        }
    }

    public static void renderArmorLevelBar(GuiGraphics guiGraphics, Player player, int leftHeight, boolean rowCount) {
        int posX = guiGraphics.guiWidth() / 2 - 91;
        int posY = guiGraphics.guiHeight() - leftHeight;
        ArmorBarRenderer.renderArmorBar(guiGraphics, posX, posY, player);
        if (rowCount) {
            RowCountRenderer.drawBarRowCount(guiGraphics, posX - 2, posY, player.getArmorValue(), true);
        }
    }

    public static void renderToughnessLevelBar(GuiGraphics guiGraphics, Player player, int guiHeight, boolean rowCount, boolean leftSide, boolean vanillaLike) {
        int posX = guiGraphics.guiWidth() / 2 + (leftSide ? -91 : 91);
        int posY = guiGraphics.guiHeight() - guiHeight;
        ArmorBarRenderer.renderToughnessBar(guiGraphics, posX, posY, player, leftSide, vanillaLike);
        if (rowCount && !vanillaLike) {
            int toughnessValue = Mth.floor(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
            RowCountRenderer.drawBarRowCount(guiGraphics, posX - 2, posY, toughnessValue, leftSide);
        }
    }
}
