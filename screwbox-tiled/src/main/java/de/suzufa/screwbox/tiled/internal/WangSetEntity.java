package de.suzufa.screwbox.tiled.internal;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import java.util.List;

public record WangSetEntity(
        List<WangColorEntity> colors,
        String name,
        List<PropertyEntity> properties,
        int tile,
        String type,
        List<WangTileEntity> wangtiles) {

    public List<WangColorEntity> colors() {
        return emptyWhenNull(colors);
    }

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }

    public List<WangTileEntity> wangtiles() {
        return emptyWhenNull(wangtiles);
    }
}
