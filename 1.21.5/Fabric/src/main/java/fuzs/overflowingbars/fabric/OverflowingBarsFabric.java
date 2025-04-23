package fuzs.overflowingbars.fabric;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class OverflowingBarsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(OverflowingBars.MOD_ID, OverflowingBars::new);
    }
}
