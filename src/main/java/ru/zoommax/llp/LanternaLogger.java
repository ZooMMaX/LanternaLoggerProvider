package ru.zoommax.llp;

import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logger for LanternaLoggerView
 */
public class LanternaLogger extends LegacyAbstractLogger {

    public LanternaLogger(String name){
        this.name = name;
    }
    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {

        List<Marker> markers = null;

        if (marker != null) {
            markers = new ArrayList<>();
            markers.add(marker);
        }

        innerHandleNormalizedLoggingCall(level, markers, messagePattern, arguments, throwable);
    }

    private void innerHandleNormalizedLoggingCall(Level level, List<Marker> markers, String messagePattern, Object[] arguments, Throwable t) {

        StringBuilder buf = new StringBuilder(32);

            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");

            buf.append("tid=");
            buf.append(Thread.currentThread().getId());
            buf.append(" ");


            buf.append(String.valueOf(name)).append(" - ");

        if (markers != null) {
            buf.append(" ");
            for (Marker marker : markers) {
                buf.append(marker.getName()).append(" ");
            }
        }

        String formattedMessage = MessageFormatter.basicArrayFormat(messagePattern, arguments);

        // Append the message
        buf.append(formattedMessage);

        LanternaLoggerView loggerView = LanternaLoggerView.getInstance();

        switch(level){
            case TRACE:
                loggerView.trace(buf.toString());
                if(t != null){
                    loggerView.trace(t.getMessage());
                }
                break;
            case DEBUG:
                loggerView.debug(buf.toString());
                if(t != null){
                    loggerView.debug(t.getMessage());
                }
                break;
            case INFO:
                loggerView.info(buf.toString());
                if(t != null){
                    loggerView.info(t.getMessage());
                }
                break;
            case WARN:
                loggerView.warn(buf.toString());
                if(t != null){
                    loggerView.warn(t.getMessage());
                }
                break;
            case ERROR:
                loggerView.error(buf.toString());
                if(t != null){
                    loggerView.error(t.getMessage());
                }
                break;
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }
}
