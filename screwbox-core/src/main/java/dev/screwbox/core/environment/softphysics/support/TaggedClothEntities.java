package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

class TaggedClothEntities extends EntityStructure implements ClothEntities {

    @Override
    public List<Entity> outline() {
        return entitiesWithTag("outline");
    }

    @Override
    public List<Entity> outlineTop() {
        return entitiesWithTag("outline-top");
    }

    @Override
    public List<Entity> outlineBottom() {
        return entitiesWithTag("outline-bottom");
    }
}
