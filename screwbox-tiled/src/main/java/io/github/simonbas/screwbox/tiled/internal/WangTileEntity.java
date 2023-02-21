package io.github.simonbas.screwbox.tiled.internal;

import java.util.List;

import static io.github.simonbas.screwbox.core.utils.ListUtil.emptyWhenNull;

public record WangTileEntity(int tileid, List<Character> wangid) {

    public List<Character> wangid() {
        return emptyWhenNull(wangid);
    }
}
