package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;
import io.github.srcimon.screwbox.core.log.Log;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.verify;

@MockitoSettings
class LoggingArchivementOnCompletionTest {

    @Mock
    Log log;

    @InjectMocks
    LoggingArchivementOnCompletion loggingArchivementOnCompletion;

    @Test
    void accept_archivementWithDescription_logsArchivementWithDescription() {
        Archivement archivement = new DefaultArchivement(() -> ArchivementDetails
                .title("click {goal} times")
                .description("click like a pro")
                .goal(5));

        loggingArchivementOnCompletion.accept(archivement);

        verify(log).info("completed archivement 'click 5 times' (click like a pro)");
    }

    @Test
    void accept_archivementWithoutDescription_logsArchivementWithoutDescription() {
        Archivement archivement = new DefaultArchivement(() -> ArchivementDetails
                .title("click {goal} times")
                .goal(5));

        loggingArchivementOnCompletion.accept(archivement);

        verify(log).info("completed archivement 'click 5 times' (no description)");
    }
}
