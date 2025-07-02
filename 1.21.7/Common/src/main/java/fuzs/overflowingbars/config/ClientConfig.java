package fuzs.overflowingbars.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import net.minecraft.ChatFormatting;

public class ClientConfig implements ConfigCore {
    @Config
    public IconRowConfig health = new IconRowConfig();
    @Config
    public ArmorRowConfig armor = new ArmorRowConfig();
    @Config
    public ToughnessRowConfig toughness = new ToughnessRowConfig();
    @Config
    public RowCountConfig rowCount = new RowCountConfig();

    public static class IconRowConfig implements ConfigCore {
        @Config(description = "Add layers to this bar. When disabled any modifications to the bar from this mod will be turned off.")
        public boolean allowLayers = true;
        @Config(description = "Render row count to indicate total amount of rows since not all may be visible at once due to the stacked rendering.")
        public boolean allowCount = true;
        @Config(description = "Show colorful icons on the front row, not just on all subsequent rows.")
        public boolean colorizeFirstRow = false;
        @Config(description = "Use vanilla's icons on all front rows, use custom colored icons on the background row.")
        public boolean inverseColoring = false;
        @Config(description = "Shift the bar up or down by specified number of icon rows. Allows for better mod compatibility.")
        @Config.IntRange(min = -5, max = 5)
        public int manualRowShift = 0;

        public int manualRowShift() {
            return this.manualRowShift * 10;
        }
    }

    public static abstract class AbstractArmorRowConfig extends IconRowConfig {
        @Config(description = "Don't draw empty armor points, this will make the armor bar potentially shorter.")
        public boolean skipEmptyArmorPoints = true;
    }

    public static class ArmorRowConfig extends AbstractArmorRowConfig {
        @Config(description = "Move chat messages above armor / absorption bar.")
        public boolean moveChatAboveArmor = true;
    }

    public static class ToughnessRowConfig extends AbstractArmorRowConfig {
        @Config(
                description = {
                        "Render a separate armor bar for the armor toughness attribute (from diamond and netherite armor).",
                        "Having only this option active will make the toughness bar behave just like vanilla's armor bar without any colorful stacking or so."
                }
        )
        public boolean armorToughnessBar = true;
        @Config(description = "Render the toughness bar on the left side above the hotbar (where health and armor is rendered).")
        public boolean leftSide = false;
    }

    public static class RowCountConfig implements ConfigCore {
        @Config(description = "Color of row count, use any chat formatting color value.")
        @Config.AllowedValues(
                values = {
                        "BLACK",
                        "DARK_BLUE",
                        "DARK_GREEN",
                        "DARK_AQUA",
                        "DARK_RED",
                        "DARK_PURPLE",
                        "GOLD",
                        "GRAY",
                        "DARK_GRAY",
                        "BLUE",
                        "GREEN",
                        "AQUA",
                        "RED",
                        "LIGHT_PURPLE",
                        "YELLOW",
                        "WHITE"
                }
        )
        public ChatFormatting rowCountColor = ChatFormatting.WHITE;
        @Config(description = "Force drawing row count using the font renderer, will make numbers display larger.")
        public boolean forceFontRenderer = false;
        @Config(description = "Only include completely filled rows for the row count.")
        public boolean countFullRowsOnly = false;
        @Config(description = "Show row count also when only one row is present.")
        public boolean alwaysRenderRowCount = false;
        @Config(description = "Render an 'x' together with the row count number.")
        public boolean rowCountX = true;
    }
}
