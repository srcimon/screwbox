package de.suzufa.screwbox.core.audio;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.utils.ResourceLoader;

public class SoundPool {

    private int index = 0;

    private final List<Sound> sounds = new ArrayList<>();

    public static SoundPool fromFile(final String fileName) {
        return fromFile(fileName, 4);
    }

    public static SoundPool fromFile(final String fileName, final int size) {
        return new SoundPool(fileName, size);
    }

    private SoundPool(final String fileName, final int size) {
        byte[] content = ResourceLoader.loadResource(fileName);

        for (int i = 0; i < size; i++) {
            sounds.add(new Sound(content));
        }
    }

    public Sound next() {
        index++;
        if (index >= sounds.size()) {
            index = 0;
        }
        return sounds.get(index);
    }

    public int activeCount() {
        int active = 0;
        for (var sound : sounds) {
            if (sound.isActive()) {
                active++;
            }
        }
        return active;

    }
}
