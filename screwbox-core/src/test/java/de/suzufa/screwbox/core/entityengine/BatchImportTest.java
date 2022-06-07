package de.suzufa.screwbox.core.entityengine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SourceImportTest {

    @Mock
    EntityEngine entityEngine;

    @Test
    void batchImport_oneConverter_addsEntity() {
        new SourceImport<String>("any input", entityEngine)
                .convert(input -> new Entity());

        verify(entityEngine).add(any(Entity.class));
    }

    @Test
    void batchImport_converterConditionNotMet_doesntAddEntity() {
        new SourceImport<String>("any input", entityEngine)
                .convertIf(String::isEmpty, input -> new Entity());

        verify(entityEngine, never()).add(any(Entity.class));
    }

    @Test
    void batchImport_converterConditionMet_addsEntity() {
        new SourceImport<String>("", entityEngine)
                .convertIf(String::isEmpty, input -> new Entity());

        verify(entityEngine).add(any(Entity.class));
    }

    @Test
    void batchImport_extractionLoop_addsEntities() {
        new SourceImport<String>("one two three", entityEngine)
                .forEach(word())
                .convert(input -> new Entity())
                .convertIf(input -> input.contains("o"), input -> new Entity());

        verify(entityEngine, times(5)).add(any(Entity.class));
    }

    @Test
    void batchImport_multipleLoops_addsEntities() {
        new SourceImport<String>("one two three \nfour five", entityEngine)
                .forEach(word())
                .convert(input -> new Entity())
                .endLoop()
                .forEach(line())
                .convert(input -> new Entity());

        verify(entityEngine, times(7)).add(any(Entity.class));
    }

    private Function<String, List<String>> word() {
        return input -> List.of(input.split(" "));
    }

    private Function<String, List<String>> line() {
        return input -> List.of(input.split("\n"));
    }
}
