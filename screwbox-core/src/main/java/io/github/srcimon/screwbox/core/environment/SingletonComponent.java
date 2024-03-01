package io.github.srcimon.screwbox.core.environment;

/**
 * {@link Component} that should only have only one instance in the {@link Environment}.
 *
 * @see Component
 * @see Environment#hasSingleton(Class)
 * @see Environment#tryFetchSingletonEntity(Class)
 * @see Environment#tryFetchSingleton(Class)
 */
public interface SingletonComponent extends Component {
}
