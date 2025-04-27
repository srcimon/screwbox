package dev.screwbox.tiled.internal;

import java.util.List;

import static dev.screwbox.core.utils.ListUtil.emptyWhenNull;

public record WangColorEntity(String color, String name, double probability, List<PropertyEntity> properties,
                              int tile) {

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }
}
