package de.suzufa.screwbox.tiled.internal.entity;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import java.util.List;

public record WangTileEntity(int tileid, List<Character> wangid) {

    public List<Character> wangid() {
        return emptyWhenNull(wangid);
    }
}
