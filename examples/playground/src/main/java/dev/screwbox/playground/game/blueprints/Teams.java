package dev.screwbox.playground.game.blueprints;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.importing.ComplexBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.navigation.AvoidanceComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.GameConfig;
import dev.screwbox.playground.game.teams.BotComponent;
import dev.screwbox.playground.game.teams.HeavenComponent;
import dev.screwbox.playground.game.teams.HumanComponent;
import dev.screwbox.playground.game.teams.PlayerComponent;
import dev.screwbox.playground.game.teams.TeamInfoComponent;
import dev.screwbox.playground.game.teams.ai.AttackComponent;
import dev.screwbox.playground.game.teams.health.HealthComponent;
import dev.screwbox.tiled.Map;

import java.util.ArrayList;
import java.util.List;

public record Teams(GameConfig gameConfig) implements ComplexBlueprint<Map> {

    //TODO move into gameconfig
    private static final List<String> NAMES = List.of("Russel", "Davi", "Sonora", "Scott", "Gwendel");


    @Override
    public List<Entity> assembleFrom(final Map source, final IdPool idPool) {
        List<Entity> entities = new ArrayList<>();
        final int playerDrawOrder = source.layerWithName("objects").orElseThrow().order();
        int viewportId = 0;
        HeavenComponent heaven = new HeavenComponent();
        int teamIndex = 0;
        for (final var team : gameConfig.teams()) {
            final var teamInfo = new TeamInfoComponent();
            teamInfo.color = team.color();
            teamInfo.name = team.name();
            teamInfo.index = teamIndex;

            int entityId = idPool.allocateId();
            source.addTranslatedObjectProperty("team", "team-id", teamInfo.index, entityId);
            source.addTranslatedObjectProperty("team", "team-color", teamInfo.index, teamInfo.color.hex());

            entities.add(new Entity(entityId).add(teamInfo));

            for (int i = 0; i < team.teamSize(); i++) {
                var player = new Entity(idPool.allocateId())
                    .add(new PlayerComponent(), config -> {
                        config.teamId = entityId;
                        config.name = ListUtil.pickRandom(NAMES);
                    })
                    .bounds(Bounds.$$(0, 0, 10, 10))
                    .add(new AvoidanceComponent())
                    .add(new OccluderComponent(false))
                    .add(new HealthComponent())
                    .add(new PathMovementComponent(70, 2000))
                    .add(new RenderComponent(Sprite.placeholder(team.color(), Size.square(10)), SpriteDrawOptions.originalSize().drawOrder(playerDrawOrder)))
                    .add(new PhysicsComponent(), config -> config.friction = 10)
                    .add(new AttackComponent());
                if (i == 0 && gameConfig.humanTeamName().equals(team.name())) {
                    player
                        .add(new HumanComponent(viewportId))
                        .add(new MovementControlComponent(), m -> m.maxSpeed = 70)
                        .add(new CameraLeadComponent())
                        .add(new CameraTargetComponent(viewportId, 2));
                    viewportId++;
                } else {
                    player.add(new BotComponent());
                }
                heaven.deadPlayers.add(player);
            }
            teamIndex++;
        }
        entities.add(new Entity().add(heaven));
        return entities;
    }
}
