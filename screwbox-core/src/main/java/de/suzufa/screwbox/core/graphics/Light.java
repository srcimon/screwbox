package de.suzufa.screwbox.core.graphics;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;

//TODO: javadoc and tests
public interface Light {

    // TODO: addPointLight(position, options, animation) Animation.apply(options ->
    // new options)
    Light addPointLight(Vector position, LightOptions options);

    Light addSpotLight(Vector position, LightOptions options);

    Light updateObstacles(List<Bounds> obstacles);

    Light setAmbientLight(Percentage ambientLight);

    Percentage ambientLight();

    List<Bounds> obstacles();

    Light render();

    Light seal();

    boolean isSealed();

}
