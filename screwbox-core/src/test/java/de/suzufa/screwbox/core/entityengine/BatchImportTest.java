package de.suzufa.screwbox.core.entityengine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.entityengine.BatchImport.Extractor;

@ExtendWith(MockitoExtension.class)
class BatchImportTest {

    @Mock
    EntityEngine entityEngine;

    @Test
    void batchImport_oneConverter_addsEntity() {
        new BatchImport<String>("any input", entityEngine)
                .add(input -> new Entity());

        verify(entityEngine).add(any(Entity.class));
    }

    @Test
    void batchImport_converterConditionNotMet_doesntAddEntity() {
        new BatchImport<String>("any input", entityEngine)
                .addIf(String::isEmpty, input -> new Entity());

        verify(entityEngine, never()).add(any(Entity.class));
    }

    @Test
    void batchImport_converterConditionMet_addsEntity() {
        new BatchImport<String>("", entityEngine)
                .addIf(String::isEmpty, input -> new Entity());

        verify(entityEngine).add(any(Entity.class));
    }

    @Test
    void batchImport_extractionLoop_addsEntities() {
        new BatchImport<String>("one two three", entityEngine)
                .forEach(word())
                .add(input -> new Entity())
                .addIf(input -> input.contains("o"), input -> new Entity());

        verify(entityEngine, times(5)).add(any(Entity.class));
    }

    @Test
    void batchImport_multipleLoops_addsEntities() {
        new BatchImport<String>("one two three \nfour five", entityEngine)
                .forEach(word())
                .add(input -> new Entity())
                .endLoop()
                .forEach(line())
                .add(input -> new Entity());

        verify(entityEngine, times(7)).add(any(Entity.class));
    }

    private Extractor<String, String> word() {
        return input -> List.of(input.split(" "));
    }

    private Extractor<String, String> line() {
        return input -> List.of(input.split("\n"));
    }
}
