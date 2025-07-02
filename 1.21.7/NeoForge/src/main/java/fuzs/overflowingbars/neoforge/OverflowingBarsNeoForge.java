package fuzs.overflowingbars.neoforge;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(OverflowingBars.MOD_ID)
public class OverflowingBarsNeoForge {

    public OverflowingBarsNeoForge() {
        ModConstructor.construct(OverflowingBars.MOD_ID, OverflowingBars::new);
    }
}
