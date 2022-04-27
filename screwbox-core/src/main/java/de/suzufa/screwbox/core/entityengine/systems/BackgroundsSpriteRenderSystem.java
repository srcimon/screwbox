package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.BackgroundSpriteComponent;

public class BackgroundsSpriteRenderSystem extends SpriteRenderSystem {

	public BackgroundsSpriteRenderSystem() {
		super(BackgroundSpriteComponent.class);
	}
	
	@Override
	public UpdatePriority updatePriority() {
		return UpdatePriority.PRESENTATION_WORLD_BACKGROUND;
	}
}
