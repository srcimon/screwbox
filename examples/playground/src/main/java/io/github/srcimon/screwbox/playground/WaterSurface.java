package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

public class WaterSurface {

    private final List<WaterNode> nodes = new ArrayList<>();

    public WaterSurface(Line waterLine, int nodeCount) {
        Validate.isTrue(() -> waterLine.from().y() == waterLine.to().y(), "water line must be horizontal");
        Validate.positive(nodeCount, "node count must be positive");
        double x = 0;
        double distance = waterLine.length() / nodeCount;

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new WaterNode(waterLine.from().addX(x)));
            x += distance;
        }
    }

    public List<WaterNode> nodes() {
        return nodes;
    }
}
