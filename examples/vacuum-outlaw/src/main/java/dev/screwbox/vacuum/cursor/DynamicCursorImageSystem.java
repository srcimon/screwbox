package dev.screwbox.vacuum.cursor;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.vacuum.player.attack.PlayerAttackControlComponent;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class DynamicCursorImageSystem implements EntitySystem {

    private static final Archetype CURSOR = Archetype.of(DynamicCursorImageComponent.class, RenderComponent.class);
    private static final Asset<Sprite> IDLE = spriteAssetFromJson("tilesets/objects/cursor.json", "idle");
    private static final Asset<Sprite> RELOADING = spriteAssetFromJson("tilesets/objects/cursor.json", "reloading");

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingletonComponent(PlayerAttackControlComponent.class).ifPresent(attackControl ->
                engine.environment().tryFetchSingleton(CURSOR).ifPresent(cursor -> {
                    boolean isReloading = attackControl.reloadDuration.addTo(attackControl.lastShotFired).isAfter(engine.loop().time());
                    var dynamicCursorImageComponent = cursor.get(DynamicCursorImageComponent.class);
                    boolean imageHasChanged = isReloading != dynamicCursorImageComponent.isShowingReloading;
                    if (imageHasChanged) {
                        var assetToUse = isReloading ? RELOADING : IDLE;
                        dynamicCursorImageComponent.isShowingReloading = isReloading;
                        cursor.get(RenderComponent.class).sprite = assetToUse.get().freshInstance();
                    }
                }));
    }
}
