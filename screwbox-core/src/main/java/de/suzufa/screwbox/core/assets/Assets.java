package de.suzufa.screwbox.core.assets;

import java.util.List;

public interface Assets {

    Assets startLoadingFromPackage(String packageName); // TODO: accept class

    List<AssetLocation<?>> scanPackageForAssetLocations(String packageName);// TODO: accept class

    boolean isLoadingInProgress();

    LoadingProgress loadingProgress();
}
