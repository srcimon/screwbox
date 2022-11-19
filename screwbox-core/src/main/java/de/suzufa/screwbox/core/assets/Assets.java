package de.suzufa.screwbox.core.assets;

import java.util.List;

//TODO: doc
public interface Assets {

    List<Asset<?>> preparePackage(String packageName);

    Assets preparePackageAsync(String packageName);

    List<Asset<?>> listAssetsInPackage(String packageName);

    Assets enableLogging();

    Assets disableLogging();

}
