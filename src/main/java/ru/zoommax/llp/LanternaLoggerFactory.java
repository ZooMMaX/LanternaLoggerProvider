package ru.zoommax.llp;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Logger factory for LanternaLoggerView
 */
public class LanternaLoggerFactory implements ILoggerFactory {


    ConcurrentMap<String, Logger> loggerMap;

    public LanternaLoggerFactory() {
        loggerMap = new ConcurrentHashMap<>();
    }
    public Logger getLogger(String name) {
        return loggerMap.computeIfAbsent(name, this::createLogger);
    }

    /**
     * Actually creates the logger for the given name.
     */
    protected Logger createLogger(String name) {
        return new LanternaLogger(name);
    }

    /**
     * Clear the internal logger cache.
     *
     * This method is intended to be called by classes (in the same package or
     * subclasses) for testing purposes. This method is internal. It can be
     * modified, renamed or removed at any time without notice.
     *
     * You are strongly discouraged from calling this method in production code.
     */
    protected void reset() {
        loggerMap.clear();
    }
}
