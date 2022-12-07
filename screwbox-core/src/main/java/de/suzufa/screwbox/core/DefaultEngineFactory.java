package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class EngineFactory {

    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

}
