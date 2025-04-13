package dev.screwbox.tiles.internal;

import java.util.List;

import static dev.screwbox.core.utils.ListUtil.emptyWhenNull;

public record WangTileEntity(int tileid, List<Character> wangid) {

    public List<Character> wangid() {
        return emptyWhenNull(wangid);
    }
}
