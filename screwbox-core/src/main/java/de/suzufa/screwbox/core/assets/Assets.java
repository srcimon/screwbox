package de.suzufa.screwbox.core.assets;

import java.util.List;

//TODO: doc
public interface Assets {

    List<AssetLocation<?>> preparePackage(String packageName);

    Assets preparePackageAsync(String packageName);

    List<AssetLocation<?>> listAssetLocationsInPackage(String packageName);

    Assets enableLogging();

    Assets disableLogging();

}
