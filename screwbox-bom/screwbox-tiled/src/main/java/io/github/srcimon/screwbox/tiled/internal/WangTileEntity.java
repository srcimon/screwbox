package io.github.srcimon.screwbox.tiled.internal;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.emptyWhenNull;

public record WangTileEntity(int tileid, List<Character> wangid) {

    public List<Character> wangid() {
        return emptyWhenNull(wangid);
    }
}
