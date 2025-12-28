package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.imports.ContextBlueprint;
import dev.screwbox.core.environment.imports.ImportContext;
import dev.screwbox.core.utils.TileMap;

public class Water implements ContextBlueprint<TileMap.Block<Character>> {

    @Override
    public Entity create(final TileMap.Block<Character> source, final ImportContext context) {
        return new Entity(context.allocateId())
                .bounds(source.bounds())
                .add(new FluidComponent(20))
                .add(new FluidRenderComponent())
                .add(new FluidTurbulenceComponent(), t -> t.strength = 700);
    }
}
