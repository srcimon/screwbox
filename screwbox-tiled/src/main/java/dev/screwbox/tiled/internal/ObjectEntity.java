package dev.screwbox.tiled.internal;

import java.awt.*;
import java.util.List;

import static dev.screwbox.core.utils.ListUtil.emptyWhenNull;

//TODO Changelog made all Tiled Entity Objects immutable
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

    public List<Point> polygon() {
        return emptyWhenNull(polygon);
    }

    public List<Point> polyline() {
        return emptyWhenNull(polyline);
    }

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }
}
