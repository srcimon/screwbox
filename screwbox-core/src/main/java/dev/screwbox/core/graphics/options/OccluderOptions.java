package dev.screwbox.core.graphics.options;

//TODO document, test, changelog
public record OccluderOptions(double backdropDistance, boolean isRounded, boolean isFloating) {

    public static OccluderOptions floating() {
        return new OccluderOptions(true);
    }

    public static OccluderOptions connected() {
        return new OccluderOptions(false);
    }

    private OccluderOptions(boolean isLoose) {
        this(1, false, isLoose);
    }

    public OccluderOptions distance(double distance) {
        return new OccluderOptions(distance, isRounded, isFloating);
    }

    public OccluderOptions roundend() {
        return new OccluderOptions(backdropDistance, true, isFloating);
    }
}
