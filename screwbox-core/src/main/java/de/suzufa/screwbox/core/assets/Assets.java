package de.suzufa.screwbox.core.assets;

import java.util.List;

public interface Assets {

    // TODO: return progress rather than progress in service?
    Assets preparePackage(String packageName); // TODO: accept class

    Assets preparePackageAsync(String packageName);

    List<AssetLocation<?>> listAssetLocationsInPackage(String packageName);// TODO: accept class

    Assets enableLogging();

    Assets disableLogging();

    // TODO: enableLogging(LogLevel...)
}
