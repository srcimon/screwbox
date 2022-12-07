package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.suzufa.screwbox.core.graphics.internal.DefaultFrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;

class EngineFactory {

    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    public FrameAdapter frameAdapter() {
        return new DefaultFrameAdapter();
    }

}
