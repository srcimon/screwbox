package de.suzufa.screwbox.playground.debo;

import de.suzufa.screwbox.playground.debo.collectables.CherriesConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboBConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboDConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboEConverter;
import de.suzufa.screwbox.playground.debo.collectables.DeboOConverter;
import de.suzufa.screwbox.playground.debo.effects.BackgroundConverter;
import de.suzufa.screwbox.playground.debo.effects.FadeInConverter;
import de.suzufa.screwbox.playground.debo.enemies.MovingSpikesConverter;
import de.suzufa.screwbox.playground.debo.enemies.slime.SlimeConverter;
import de.suzufa.screwbox.playground.debo.enemies.tracer.TracerConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapBottomConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapLeftConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapRightConverter;
import de.suzufa.screwbox.playground.debo.map.CloseMapTopConverter;
import de.suzufa.screwbox.playground.debo.map.MapGravityConverter;
import de.suzufa.screwbox.playground.debo.map.WorldBoundsConverter;
import de.suzufa.screwbox.playground.debo.props.BoxConverter;
import de.suzufa.screwbox.playground.debo.props.DiggableConverter;
import de.suzufa.screwbox.playground.debo.props.PlatfomConverter;
import de.suzufa.screwbox.playground.debo.props.VanishingBlockConverter;
import de.suzufa.screwbox.playground.debo.specials.CameraConverter;
import de.suzufa.screwbox.playground.debo.specials.CatConverter;
import de.suzufa.screwbox.playground.debo.specials.WaypointConverter;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerConverter;
import de.suzufa.screwbox.playground.debo.tiles.NonSolidConverter;
import de.suzufa.screwbox.playground.debo.tiles.OneWayConverter;
import de.suzufa.screwbox.playground.debo.tiles.SolidConverter;
import de.suzufa.screwbox.playground.debo.zones.ChangeMapZoneConverter;
import de.suzufa.screwbox.playground.debo.zones.KillZoneConverter;
import de.suzufa.screwbox.playground.debo.zones.ShowLabelZoneConverter;
import de.suzufa.screwbox.tiled.MapConverter;

public class DeboMapConverter extends MapConverter {

    public DeboMapConverter() {
        addMapConverter(new CloseMapLeftConverter());
        addMapConverter(new CloseMapRightConverter());
        addMapConverter(new CloseMapBottomConverter());
        addMapConverter(new CloseMapTopConverter());
        addMapConverter(new MapGravityConverter());
        addMapConverter(new WorldBoundsConverter());

        addLayerConverter(new BackgroundConverter());

        addTileConverter(new NonSolidConverter());
        addTileConverter(new SolidConverter());
        addTileConverter(new OneWayConverter());

        addObjectConverter(new CatConverter());
        addObjectConverter(new MovingSpikesConverter());
        addObjectConverter(new VanishingBlockConverter());
        addObjectConverter(new SlimeConverter());
        addObjectConverter(new PlatfomConverter());
        addObjectConverter(new WaypointConverter());
        addObjectConverter(new CameraConverter());
        addObjectConverter(new PlayerConverter());
        addObjectConverter(new DeboDConverter());
        addObjectConverter(new DeboEConverter());
        addObjectConverter(new DeboBConverter());
        addObjectConverter(new DeboOConverter());
        addObjectConverter(new CherriesConverter());
        addObjectConverter(new KillZoneConverter());
        addObjectConverter(new BoxConverter());
        addObjectConverter(new DiggableConverter());
        addObjectConverter(new ChangeMapZoneConverter());
        addObjectConverter(new ShowLabelZoneConverter());
        addObjectConverter(new FadeInConverter());
        addObjectConverter(new TracerConverter());
    }

}