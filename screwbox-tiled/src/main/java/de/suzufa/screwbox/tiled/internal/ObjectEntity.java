package de.suzufa.screwbox.tiled.internal;

import java.awt.Point;
import java.util.List;

public class ObjectEntity { // cannot be replaced by record: not all properties are final

    private boolean ellipse;
    private int gid;
    private double height;
    private int id;
    private String name;
    private boolean point;
    private List<Point> polygon;
    private List<Point> polyline;
    private List<PropertyEntity> properties;
    double rotation;
    private String template;
    private String text;
    private String type;
    private boolean visible;
    private double width;
    private double x;
    private double y;

    public boolean isEllipse() {
        return ellipse;
    }

    public void setEllipse(boolean ellipse) {
        this.ellipse = ellipse;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPoint() {
        return point;
    }

    public void setPoint(boolean point) {
        this.point = point;
    }

    public List<Point> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Point> polygon) {
        this.polygon = polygon;
    }

    public List<Point> getPolyline() {
        return polyline;
    }

    public void setPolyline(List<Point> polyline) {
        this.polyline = polyline;
    }

    public List<PropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyEntity> properties) {
        this.properties = properties;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
