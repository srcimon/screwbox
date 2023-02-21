package io.github.simonbas.screwbox.tiled.internal;

import java.util.List;

import static io.github.simonbas.screwbox.core.utils.ListUtil.emptyWhenNull;

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
