package com.quetoquenana.pedalpal.common.application.port;

/**
 * Dispatches background work without coupling to a specific execution strategy.
 */
public interface BackgroundJobDispatcher {

    /**
     * Dispatches a runnable for background processing.
     */
    void dispatch(Runnable task);
}

