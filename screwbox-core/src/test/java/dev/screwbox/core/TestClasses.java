package dev.screwbox.core;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;

public class TestClasses {

    public static class DefectAssetBundle implements AssetBundle<String> {

        @Override
        public Asset<String> asset() {
            return null;
        }
    }
}
