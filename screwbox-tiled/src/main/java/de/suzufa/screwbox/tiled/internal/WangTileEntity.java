package de.suzufa.screwbox.tiled.internal;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import java.util.List;

public record WangTileEntity(int tileid, List<Character> wangid) {

    public List<Character> wangid() {
        return emptyWhenNull(wangid);
    }
}
