package dev.screwbox.tiled.internal;

import java.util.List;

public record WangColorEntity(String color, String name, double probability, List<PropertyEntity> properties,
                              int tile) {

}
