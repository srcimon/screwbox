package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;

public class LightBlockingComponent implements Component {

	private static final long serialVersionUID = 1L;

	public double sizeModifier = 0;
	
	public LightBlockingComponent() {
		this(0);
	}
	public LightBlockingComponent(final double sizeModifier) {
		this.sizeModifier  = sizeModifier;
	}
}
