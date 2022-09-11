package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Component;

public class LightEmitterComponent implements Component {

	private static final long serialVersionUID = 1L;

	public double range;
	public Percentage shadowOpacity;
	public boolean fixedOpacity = false;
	public Archetype blockedBy = Archetype.of(TransformComponent.class, ColliderComponent.class);
	
	public LightEmitterComponent(final double range,Percentage shadowOpacity) {
		this.range = range;
		this.shadowOpacity = shadowOpacity;
	}
}
