package de.suzufa.screwbox.core.assets;

import java.util.List;

public interface Assets {

    // TODO: return progress rather than progress in service?
    Assets startLoadingFromPackage(String packageName); // TODO: accept class

    // TODO: i dont want that: needed for gamescene tests / avoidable?
    Assets loadFromPackage(String packageName);

    List<AssetLocation<?>> scanPackageForAssetLocations(String packageName);// TODO: accept class

    boolean isLoadingInProgress();

    LoadingProgress loadingProgress();
}
