package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.BackgroundSpriteComponent;

public class BackgroundsSpriteRenderSystem extends SpriteRenderSystem {

	public BackgroundsSpriteRenderSystem() {
		super(BackgroundSpriteComponent.class);
	}
	
	@Override
	public UpdatePriority updatePriority() {
		return UpdatePriority.PRESENTATION_WORLD_BACKGROUND;
	}
}
