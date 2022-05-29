package de.suzufa.screwbox.playground.debo.tiles;

import java.util.Optional;

import de.suzufa.screwbox.core.resources.EntityConverter;
import de.suzufa.screwbox.tiled.Tile;

public abstract class BaseTileConverter implements EntityConverter<Tile> {

    private final String typeName;

    protected BaseTileConverter(final String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean accepts(final Tile tile) {
        final Optional<String> type = tile.layer().properties().get("type");
        if (type.isPresent()) {
            return typeName.equals(type.get());
        }
        final Optional<String> tileType = tile.properties().get("type");
        return tileType.isPresent() && typeName.equals(tileType.get());
    }
}
