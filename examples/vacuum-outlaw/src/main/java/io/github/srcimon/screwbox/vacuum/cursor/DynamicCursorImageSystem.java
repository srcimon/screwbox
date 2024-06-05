package io.github.srcimon.screwbox.vacuum.cursor;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.vacuum.player.attack.PlayerAttackControl;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DynamicCursorImageSystem implements EntitySystem {

    private static final Archetype CURSOR = Archetype.of(DynamicCursorImageComponent.class, RenderComponent.class);
    private static final Asset<Sprite> IDLE = spriteAssetFromJson("tilesets/objects/cursor.json", "idle");
    private static final Asset<Sprite> RELOADING = spriteAssetFromJson("tilesets/objects/cursor.json", "reloading");

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingletonComponent(PlayerAttackControl.class).ifPresent(attackControl ->
                engine.environment().tryFetchSingleton(CURSOR).ifPresent(cursor -> {
                    boolean isReloading = attackControl.reloadDuration.addTo(attackControl.lastShotFired).isAfter(engine.loop().lastUpdate());
                    var dynamicCursorImageComponent = cursor.get(DynamicCursorImageComponent.class);
                    boolean imageHasChanged = isReloading != dynamicCursorImageComponent.isShowingReloading;
                    if (imageHasChanged) {
                        var assetToUse = isReloading ? RELOADING : IDLE;
                        dynamicCursorImageComponent.isShowingReloading = isReloading;
                        cursor.get(RenderComponent.class).sprite = assetToUse.get().freshInstance();
                    }
                }));
        //TODO: Sprite.lockLastFrame()
    }
}
