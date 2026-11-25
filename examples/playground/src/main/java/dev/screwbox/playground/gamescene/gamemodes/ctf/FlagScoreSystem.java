package dev.screwbox.playground.gamescene.gamemodes.ctf;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.light.ShockwaveOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.playground.gamescene.teams.PlayerComponent;

import java.util.List;

import static java.util.Objects.nonNull;

public class FlagScoreSystem implements EntitySystem {

    private static final Archetype FLAGS = Archetype.ofSpacial(FlagComponent.class);
    private static final Archetype CAPTURE_ZONES = Archetype.ofSpacial(CaptureZoneComponent.class);
    private static final Archetype PLAYERS = Archetype.ofSpacial(PlayerComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var c : engine.environment().fetchAll(CAPTURE_ZONES)) {
            engine.graphics().world().drawRectangle(c.bounds(), RectangleDrawOptions.filled(c.get(CaptureZoneComponent.class).team.opacity(0.5)).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
        }
        final List<Entity> players = engine.environment().fetchAll(PLAYERS);
        for (final var flagEntity : engine.environment().fetchAll(FLAGS)) {
            final var flag = flagEntity.get(FlagComponent.class);
            if (nonNull(flag.carriedById)) {
               handleCarriedFlag(engine, flagEntity, flag);
            }
            if (flag.isStolen) {
                checkAndHandleFlagRetrieval(engine, flagEntity, players, flag);
                checkAndHandleFlagScore(engine, flagEntity, flag);
            } else {
                checkAndHandleFlagSteal(flagEntity, players, flag);
            }
        }
    }

    private static void checkAndHandleFlagScore(Engine engine, Entity flagEntity, FlagComponent flag) {
        for(final var captureZone : engine.environment().fetchAll(CAPTURE_ZONES)) {
            if(captureZone.intersects(flagEntity) && !captureZone.get(CaptureZoneComponent.class).team.equals(flag.team)) {
                engine.graphics().postProcessing().triggerShockwave(flagEntity.position(), ShockwaveOptions.radius(20));//TODO REMOVE
                engine.environment().tryFetchById(flag.carriedById).ifPresent(carrier -> carrier.remove(FlagCarrierComponent.class));
                retrieveFlag(engine, flagEntity, flag);
            }
        }
    }

    private static void checkAndHandleFlagSteal(Entity flagEntity, List<Entity> players, FlagComponent flag) {
        for (final var player : players) {
            if (player.touches(flagEntity)) {
                if (!player.hasComponent(FlagCarrierComponent.class) && !player.get(PlayerComponent.class).team.equals(flag.team)) {
                    flag.carriedById = player.forceId();
                    flag.isStolen = true;
                    player.add(new FlagCarrierComponent());
                }
            }
        }
    }

    private static void checkAndHandleFlagRetrieval(Engine engine, Entity flagEntity, List<Entity> players, FlagComponent flag) {
        for (final var player : players) {
            if (player.touches(flagEntity) && player.get(PlayerComponent.class).team.equals(flag.team)) {
                retrieveFlag(engine, flagEntity, flag);
            }
        }
    }

    private static void retrieveFlag(Engine engine, Entity flagEntity, FlagComponent flag) {
        flag.isStolen = false;
        flag.carriedById = null;//TODO carriedById redudant to stolen?
        Entity homeZone = engine.environment().fetchById(flag.homeZoneId);
        flagEntity.moveTo(homeZone.position());
    }

    private static void handleCarriedFlag(Engine engine, Entity flagEntity, FlagComponent flag) {
        engine.environment().tryFetchById(flag.carriedById).ifPresentOrElse(
            carrier -> flagEntity.moveTo(carrier.position()),
            () -> flagEntity.get(FlagComponent.class).carriedById = null);
    }
}
