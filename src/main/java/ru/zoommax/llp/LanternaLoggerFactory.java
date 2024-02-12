package ru.zoommax.llp;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Logger factory for LanternaLoggerView
 */
public class LanternaLoggerFactory implements ILoggerFactory {
    @Override
    public Logger getLogger(String name) {
        try {
            return LanternaLoggerView.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
