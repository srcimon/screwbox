package de.suzufa.screwbox.core.assets.internal;

import static java.util.Objects.nonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.assets.Demo;
import de.suzufa.screwbox.core.log.Log;

public class DefaultAssets implements Assets {

    private final ExecutorService executor;
    private final Log log;

    private Future<?> loadingTask;

    public DefaultAssets(final ExecutorService executor, final Log log) {
        this.executor = executor;
        this.log = log;
    }

    @Override
    public Assets asyncLoadAssetsIn(String packageName) {
        if (nonNull(loadingTask)) {
            throw new IllegalStateException("loading assets is already in progress");
        }
        loadingTask = executor.submit(() -> {
            for (var clazz : new Demo().findAllClassesUsingClassLoader(packageName)) {
                for (var field : clazz.getDeclaredFields()) {
                    if (field.getType().isAssignableFrom(Asset.class)) {
                        Asset object;
                        try {
                            object = (Asset) field.get(Asset.class);

                            Time time = Time.now();
                            object.load();
                            long milliseconds = Duration.since(time).milliseconds();
                            log.debug("loading asset " + clazz.getSimpleName() + "." + field.getName() + " took "
                                    + milliseconds + " ms");
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            loadingTask = null;
        });
        return this;
    }

}
