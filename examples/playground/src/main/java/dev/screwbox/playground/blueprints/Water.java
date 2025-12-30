package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.utils.TileMap;

public class Water implements Blueprint<TileMap.Block<Character>> {

    @Override
    public Entity assembleFrom(final TileMap.Block<Character> source) {
        return new Entity()
                .bounds(source.bounds())
                .add(new FluidComponent(20))
                .add(new FluidRenderComponent())
                .add(new FluidTurbulenceComponent(), t -> t.strength = 700);
    }
}
