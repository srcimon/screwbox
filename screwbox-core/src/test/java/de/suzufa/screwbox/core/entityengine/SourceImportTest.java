package de.suzufa.screwbox.core.entityengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.test.extensions.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class SourceImportTest {

    @Test
    void importSource_(DefaultEntityEngine entityEngine) {
        entityEngine.importSource("Source of Inspiration")
                .as(source -> new Entity());

        assertThat(entityEngine.allEntities()).hasSize(1);
    }
//
//    @Test
//    void batchImport_converterConditionNotMet_doesntAddEntity() {
//        new SourceImport<String>("any input", entityEngine)
//                .convertIf(String::isEmpty, input -> new Entity());
//
//        verify(entityEngine, never()).add(any(Entity.class));
//    }
//
//    @Test
//    void batchImport_converterConditionMet_addsEntity() {
//        new SourceImport<String>("", entityEngine)
//                .convertIf(String::isEmpty, input -> new Entity());
//
//        verify(entityEngine).add(any(Entity.class));
//    }
//
//    @Test
//    void batchImport_extractionLoop_addsEntities() {
//        new SourceImport<String>("one two three", entityEngine)
//                .forEach(word())
//                .convert(input -> new Entity())
//                .convertIf(input -> input.contains("o"), input -> new Entity());
//
//        verify(entityEngine, times(5)).add(any(Entity.class));
//    }
//
//    @Test
//    void batchImport_multipleLoops_addsEntities() {
//        new SourceImport<String>("one two three \nfour five", entityEngine)
//                .forEach(word())
//                .convert(input -> new Entity())
//                .endLoop()
//                .forEach(line())
//                .convert(input -> new Entity());
//
//        verify(entityEngine, times(7)).add(any(Entity.class));
//    }

    private Function<String, List<String>> word() {
        return input -> List.of(input.split(" "));
    }

    private Function<String, List<String>> line() {
        return input -> List.of(input.split("\n"));
    }
}
