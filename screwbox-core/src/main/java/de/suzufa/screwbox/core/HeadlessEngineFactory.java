package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.suzufa.screwbox.core.graphics.internal.FakeFrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.WindowFrame;

class HeadlessEngineFactory implements EngineFactory {

    @Override
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public FrameAdapter frameAdapter() {
        return new FakeFrameAdapter();
    }

    @Override
    public WindowFrame windowFrame() {
        return null;
    }
}
