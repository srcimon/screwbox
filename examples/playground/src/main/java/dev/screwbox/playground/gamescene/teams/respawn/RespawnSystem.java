package dev.screwbox.playground.gamescene.teams.respawn;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.HumanComponent;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;
import dev.screwbox.playground.gamescene.teams.health.HealthComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ExecutionOrder(Order.PRESENTATION_UI_BACKGROUND)
public class RespawnSystem implements EntitySystem {

    private static final Archetype RESPAWN_AREAS = Archetype.ofSpacial(RespawnAreaComponent.class);

    @Override
    public void update(Engine engine) {
        final var respawnConfig = engine.environment().fetchSingletonComponent(RespawnConfigComponent.class);

        final var respawnAreas = engine.environment().fetchAll(RESPAWN_AREAS);
        HeavenComponent heaven = engine.environment().fetchSingletonComponent(HeavenComponent.class);
        final List<Entity> revivedPlayer = new ArrayList<>();
        for (final var player : heaven.deadPlayers) {
            var config = player.get(PlayerComponent.class);
            Time respawnTime = respawnConfig.respawnTimeout.addTo(config.deathTime);
            if (respawnTime.isBefore(engine.loop().time())) {
                var availableAreas = respawnAreas.stream().filter(area -> area.get(RespawnAreaComponent.class).team.equals(config.team)).toList();
                player.moveTo(ListUtil.pickRandom(availableAreas).bounds().randomPosition());
                engine.environment().addEntity(player);
                player.get(HealthComponent.class).health = Percent.max();
                revivedPlayer.add(player);
            } else {
                var human = player.get(HumanComponent.class);
                if (Objects.nonNull(human)) {
                    engine.graphics().viewport(human.viewportId).ifPresent(viewport -> viewport.canvas().fillWith(Color.BLACK.opacity(0.8))
                        .drawText(viewport.canvas().center(), String.valueOf(Duration.between(respawnTime, engine.loop().time()).seconds() + 1), TextDrawOptions.font(FontBundle.BOLDZILLA).scale(10).alignCenter()));
                }
            }
        }
        heaven.deadPlayers.removeAll(revivedPlayer);
    }

}
