package dev.screwbox.tiled.internal;

import java.awt.*;
import java.util.List;

public record ObjectEntity(
    boolean ellipse,
    int gid,
    double height,
    int id,
    String name,
    boolean point,
    List<Point> polygon,
    List<Point> polyline,
    List<PropertyEntity> properties,
    double rotation,
    String template,
    String text,
    String type,
    boolean visible,
    double width,
    double x,
    double y) {

}
