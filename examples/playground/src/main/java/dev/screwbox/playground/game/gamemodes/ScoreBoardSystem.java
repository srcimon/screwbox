package dev.screwbox.playground.game.gamemodes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.playground.game.teams.TeamInfoComponent;

import java.util.List;

@ExecutionOrder(Order.PRESENTATION_UI)
public class ScoreBoardSystem implements EntitySystem {

    private static final Archetype TEAM_INFOS = Archetype.of(TeamInfoComponent.class);

    @Override
    public void update(Engine engine) {
        final List<Entity> teamInfoEntities = engine.environment().fetchAll(TEAM_INFOS);
        int index = 0;
        for (final var teamInfoEntity : teamInfoEntities) {
            var teamInfo = teamInfoEntity.get(TeamInfoComponent.class);
            Offset position = calculateTeamPosition(index, teamInfoEntities.size(), engine.graphics().canvas().width(), 120, 100, 20);
            engine.graphics().canvas().drawText(position, teamInfo.name + ": " + teamInfo.score, TextDrawOptions.font(FontBundle.BOLDZILLA).scale(2));
            index++;
        }
    }

    public static Offset calculateTeamPosition(int teamIndex, int teamCount, int screenWidth, int cardWidth, int gap, int topMargin) {
        if (teamCount < 1 || teamCount > 4) {
            throw new IllegalArgumentException("Team count must be between 1 and 4");
        }
        if (teamIndex < 0 || teamIndex >= teamCount) {
            throw new IllegalArgumentException("Team index must be between 0 and " + (teamCount - 1));
        }

        int totalWidth = (teamCount * cardWidth) + ((teamCount - 1) * gap);
        int startX = (screenWidth - totalWidth) / 2;
        int x = startX + teamIndex * (cardWidth + gap);

        return Offset.at(x, topMargin);
    }

}
