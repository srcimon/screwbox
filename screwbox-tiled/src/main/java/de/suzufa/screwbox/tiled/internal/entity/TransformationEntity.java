package de.suzufa.screwbox.tiled.internal.entity;

public class TransformationEntity {

    private boolean hflip;
    private boolean vflip;
    private boolean rotate;
    private boolean preferuntransformed;

    public boolean isHflip() {
        return hflip;
    }

    public void setHflip(boolean hflip) {
        this.hflip = hflip;
    }

    public boolean isVflip() {
        return vflip;
    }

    public void setVflip(boolean vflip) {
        this.vflip = vflip;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public boolean isPreferuntransformed() {
        return preferuntransformed;
    }

    public void setPreferuntransformed(boolean preferuntransformed) {
        this.preferuntransformed = preferuntransformed;
    }

}
