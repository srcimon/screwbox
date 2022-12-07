package de.suzufa.screwbox.core;

import de.suzufa.screwbox.core.graphics.internal.FrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.FakeFrameAdapter;

class HeadlessEngineFactory extends EngineFactory {

    @Override
    public FrameAdapter frameAdapter() {
        return new FakeFrameAdapter();
    }
}
