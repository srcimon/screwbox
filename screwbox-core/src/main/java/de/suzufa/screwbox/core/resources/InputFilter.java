package de.suzufa.screwbox.core.resources;

public interface InputFilter<T> {

    boolean matches(T input);
}
