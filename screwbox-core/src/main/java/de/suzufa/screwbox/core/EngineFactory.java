package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;

interface EngineFactory {

    ExecutorService executorService();
}
