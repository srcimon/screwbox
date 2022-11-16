package de.suzufa.screwbox.core.assets.internal;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.assets.Demo;
import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.log.Log;

public class DefaultAssets implements Assets {

    private Async async;
    private Log log;

    public DefaultAssets(Async async, Log log) {
        this.async = async;
        this.log = log;
    }

    @Override
    public Assets startAsyncLoadingAssetsIn(String packageName) {
        async.run(DefaultAssets.class, () -> {
            for (var clazz : new Demo().findAllClassesUsingClassLoader(packageName)) {
                for (var field : clazz.getDeclaredFields()) {
                    if (field.getType().isAssignableFrom(Asset.class)) {
                        try {
//                        field.setAccessible(true);
                            Asset object = (Asset) field.get(Asset.class);
                            Time time = Time.now();
                            object.load();
                            long milliseconds = Duration.since(time).milliseconds();
                            log.debug("loading asset " + clazz.getSimpleName() + "." + field.getName() + " took "
                                    + milliseconds + " ms");
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return this;
    }

}
