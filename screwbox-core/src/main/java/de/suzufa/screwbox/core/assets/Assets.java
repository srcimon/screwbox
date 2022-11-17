package de.suzufa.screwbox.core.assets;

import java.util.List;

public interface Assets {

    public Assets startLoadingFromPackage(String packageName); // TODO: accept class

    public List<AssetLocation<?>> scanPackageForAssetLocations(String packageName);// TODO: accept class

}
