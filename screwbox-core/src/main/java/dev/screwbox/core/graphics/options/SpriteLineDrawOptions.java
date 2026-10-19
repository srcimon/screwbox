package dev.screwbox.core.graphics.options;

//TODO finish up
public record SpriteLineDrawOptions(int drawOrder, double scale) {//TODO shader
    //TODO scale

    public SpriteLineDrawOptions scale(final double scale) {
        return new SpriteLineDrawOptions(drawOrder, scale);
    }
}
