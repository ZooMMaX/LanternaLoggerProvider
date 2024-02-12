package ru.zoommax.llp;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * SLF4J ServiceProvider for LanternaLogger
 */
public class LanternaLoggerServiceProvider implements SLF4JServiceProvider {

    private final LanternaLoggerFactory factory = new LanternaLoggerFactory();

    @Override
    public ILoggerFactory getLoggerFactory() {
        return factory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return null;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return new NOPMDCAdapter();
    }

    @Override
    public String getRequestedApiVersion() {
        return "2.0.11";
    }

    @Override
    public void initialize() {
        // Код инициализации для вашей фабрики логгеров
    }
}