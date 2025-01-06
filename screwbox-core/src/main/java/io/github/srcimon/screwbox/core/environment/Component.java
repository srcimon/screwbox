package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.ScrewBox;

import java.io.Serializable;

/**
 * Components contain data that is processed by {@link EntitySystem entity systems} every single frame.
 * As a rule of thumb components are not supposed to contain any code. Components are part of the {@link Environment}
 * the {@link ScrewBox} own <a href="https://en.wikipedia.org/wiki/Entity_component_system">Entity Component System</a>.
 */
public interface Component extends Serializable {

}
