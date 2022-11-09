package de.suzufa.screwbox.core.graphics.internal;

import java.util.List;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

public record PointLight(Offset position, int radius, List<Offset> area, Color color) {

}
