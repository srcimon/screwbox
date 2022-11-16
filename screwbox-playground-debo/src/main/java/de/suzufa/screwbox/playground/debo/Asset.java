package de.suzufa.screwbox.playground.debo;

import java.util.function.Supplier;

import de.suzufa.screwbox.core.graphics.Sprite;

public class Asset<T> {

    public static <T> Asset<T> asset(Supplier<T> supplier) {
        // TODO Auto-generated method stub
        return new Asset<T>();
    }

    public static Asset<Sprite> spriteAsset(String name) {
        Sprite.fromFile(name);
        return new Asset<Sprite>();
    }
}
