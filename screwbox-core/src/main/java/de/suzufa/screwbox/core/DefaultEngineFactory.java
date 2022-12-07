package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.suzufa.screwbox.core.graphics.internal.DefaultFrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.WindowFrame;

class EngineFactory {

    private final WindowFrame frame = new WindowFrame();// TODO: remove

    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    public FrameAdapter frameAdapter() {
        return new DefaultFrameAdapter(frame);
    }

    public WindowFrame windowFrame() {
        return frame;
    }

}
