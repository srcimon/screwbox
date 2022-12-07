package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.suzufa.screwbox.core.graphics.internal.DefaultFrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.WindowFrame;

class DefaultEngineFactory implements EngineFactory {

    private final WindowFrame frame = new WindowFrame();// TODO: remove

    @Override
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public FrameAdapter frameAdapter() {
        return new DefaultFrameAdapter(frame);
    }

    @Override
    public WindowFrame windowFrame() {
        return frame;
    }

}
