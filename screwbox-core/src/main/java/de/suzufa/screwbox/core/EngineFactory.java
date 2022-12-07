package de.suzufa.screwbox.core;

import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.WindowFrame;

interface EngineFactory {

    ExecutorService executorService();

    FrameAdapter frameAdapter();

    WindowFrame windowFrame();

}