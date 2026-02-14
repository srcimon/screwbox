package dev.screwbox.core.graphics.options;

//TODO document, test, changelog
public record OccluderOptions(double distance, boolean isRounded, boolean isLoose) {

    public static OccluderOptions loose() {
        return new OccluderOptions(true);
    }

    public static OccluderOptions connected() {
        return new OccluderOptions(false);
    }

    private OccluderOptions(boolean isLoose) {
        this(1, false, isLoose);
    }

    public OccluderOptions distance(double distance) {
        return new OccluderOptions(distance, isRounded, isLoose);
    }

    public OccluderOptions roundend() {
        return new OccluderOptions(distance, true, isLoose);
    }
}
