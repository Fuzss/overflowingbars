package fuzs.overflowingbars.client.gui;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.client.gui.v2.GuiGraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.stream.IntStream;

public class RowCountRenderer {
    private static final ResourceLocation TINY_NUMBERS_LOCATION = OverflowingBars.id("textures/font/tiny_numbers.png");

    public static void drawBarRowCount(GuiGraphics guiGraphics, int posX, int posY, int barValue, boolean left) {
        drawBarRowCount(guiGraphics, posX, posY, barValue, left, 20);
    }

    public static void drawBarRowCount(GuiGraphics guiGraphics, int posX, int posY, int barValue, boolean left, int maxRowCount) {
        if (barValue <= 0 || maxRowCount <= 0) return;
        float rowCount = barValue / (float) maxRowCount;
        ClientConfig config = OverflowingBars.CONFIG.get(ClientConfig.class);
        if (!config.rowCount.alwaysRenderRowCount && rowCount <= 1.0F) return;
        int numberValue;
        if (config.rowCount.countFullRowsOnly) {
            numberValue = Mth.floor(rowCount);
        } else {
            numberValue = Mth.ceil(rowCount);
        }
        int textColor = ARGB.opaque(config.rowCount.rowCountColor.getColor());
        if (config.rowCount.forceFontRenderer) {
            Font font = Minecraft.getInstance().font;
            String text = String.valueOf(numberValue);
            if (config.rowCount.rowCountX) {
                text += "x";
            }
            if (left) {
                posX -= font.width(text);
            } else {
                posX += 5;
            }
            GuiGraphicsHelper.drawInBatch8xOutline(guiGraphics,
                    font,
                    Component.literal(text),
                    posX,
                    posY + 1,
                    textColor,
                    ARGB.opaque(0));
        } else {
            // this is in reverse order
            int[] numberDigitis = IntStream.iterate(numberValue, i -> i > 0, i -> i / 10).map(i -> i % 10).toArray();
            if (left) {
                posX -= config.rowCount.rowCountX ? 7 : 3;
            } else {
                posX += 4 * numberDigitis.length;
            }
            for (int i = 0; i < numberDigitis.length; i++) {
                drawBorderedSprite(guiGraphics, 3, 5, posX - 4 * i, posY + 2, 5 * numberDigitis[i], 0, textColor);
            }
            if (config.rowCount.rowCountX) {
                drawBorderedSprite(guiGraphics, 3, 5, posX + 4, posY + 2, 0, 7, textColor);
            }
        }
    }

    private static void drawBorderedSprite(GuiGraphics guiGraphics, int width, int height, int posX, int posY, int textureX, int textureY, int textColor) {
        // drop shadow on all sides
        int backgroundColor = ARGB.opaque(0);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TINY_NUMBERS_LOCATION,
                posX - 1,
                posY,
                textureX,
                textureY,
                width,
                height,
                256,
                256,
                backgroundColor);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TINY_NUMBERS_LOCATION,
                posX + 1,
                posY,
                textureX,
                textureY,
                width,
                height,
                256,
                256,
                backgroundColor);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TINY_NUMBERS_LOCATION,
                posX,
                posY - 1,
                textureX,
                textureY,
                width,
                height,
                256,
                256,
                backgroundColor);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TINY_NUMBERS_LOCATION,
                posX,
                posY + 1,
                textureX,
                textureY,
                width,
                height,
                256,
                256,
                backgroundColor);
        // actual number
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TINY_NUMBERS_LOCATION,
                posX,
                posY,
                textureX,
                textureY,
                width,
                height,
                256,
                256,
                textColor);
    }
}
