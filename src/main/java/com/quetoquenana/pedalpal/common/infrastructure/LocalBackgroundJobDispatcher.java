package com.quetoquenana.pedalpal.common.infrastructure;

import com.quetoquenana.pedalpal.common.application.port.BackgroundJobDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Local in-process background job dispatcher using Spring's task executor.
 */
@Slf4j
@Component
public class LocalBackgroundJobDispatcher implements BackgroundJobDispatcher {

    private final TaskExecutor taskExecutor;

    public LocalBackgroundJobDispatcher(@Qualifier("applicationTaskExecutor") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * Dispatches a background task using the local task executor.
     */
    @Override
    public void dispatch(Runnable task) {
        if (task == null) {
            return;
        }
        taskExecutor.execute(() -> {
            try {
                task.run();
            } catch (Exception ex) {
                log.error("Background job failed", ex);
            }
        });
    }
}
