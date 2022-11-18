package de.suzufa.screwbox.core.assets;

import java.util.List;

public interface Assets {

    // TODO: return progress rather than progress in service?
    Assets preparePackage(String packageName); // TODO: accept class

    Assets preparePackageAsync(String packageName);

    List<AssetLocation<?>> scanPackageForAssetLocations(String packageName);// TODO: accept class
    // TODO: scanPackageForAssetLocations? why

    // TODO: enableLogging(LogLevel...)
}
