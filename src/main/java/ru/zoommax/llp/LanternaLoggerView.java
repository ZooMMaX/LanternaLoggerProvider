package ru.zoommax.llp;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* LanternaLoggerView is a class that implements the Logger interface from the slf4j library.
* It is used to display logs in the terminal window using the Lanterna library.
* The class is a singleton, so it can be accessed from anywhere in the code.
* The class has methods for displaying logs of different levels: error, info, warn, debug, trace.
*/
public class LanternaLoggerView extends BasicWindow implements Logger {

    private static LanternaLoggerView instance = null;


    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private int startX = -1;
    private int startY = -1;
    private int stopX = -1;
    private int stopY = -1;

    private Terminal terminal = null;
    ExecutorService service;

    /**
     * The constructor is private so that the class cannot be instantiated from outside.
     * It is used to create a single instance of the class.
     */
    private LanternaLoggerView() {
        super("Lanterna Logger");
    }

    /**
     * The method is used to get the instance of the class.
     * If the instance does not exist, it is created.
     * @return the instance of the class
     * @throws IOException if an I/O error occurs
     */
    public static LanternaLoggerView getInstance() throws IOException {
        if (instance == null) {
            instance = new LanternaLoggerView();
            instance.service = Executors.newSingleThreadExecutor();
        }
        return instance;
    }

    /**
     * The method is used to get the instance of the class.
     * If the instance does not exist, it is created.
     * @param terminal the terminal to be used for displaying logs
     * @return the instance of the class
     * @throws IOException if an I/O error occurs
     */
    public static LanternaLoggerView getInstance(Terminal terminal) throws IOException {
        if (instance == null) {
            instance = new LanternaLoggerView();
            instance.service = Executors.newSingleThreadExecutor();
        }
        instance.terminal = terminal;
        return instance;
    }

    private void drawSettings() throws IOException {
        if (startX == -1 && startY == -1) {
            startX = getPosition().getColumn() + 2;
            startY = getPosition().getRow() + 1;
            stopX = getPosition().getColumn() + getSize().getColumns();
            stopY = getPosition().getRow() + getSize().getRows() + 1;
            int lineCount = stopY - startY;
            for (int i = 0; i < lineCount; i++) {
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setForegroundColor(TextColor.ANSI.BLACK);
                terminal.setCursorPosition(startX, startY + i);
                terminal.putString(" ".repeat(stopX - startX));
            }
        }
    }

    /**
     * The method is used to display an error log in the terminal window.
     * @param message the message to be displayed
     */
    @Override
    public void error(String message) {
        try {
            FileOutputStream fot = new FileOutputStream("logError.txt", true);
            fot.write((formatter.format(new Date()) + " Error: " + message + "\n").getBytes());
            fot.close();
            drawSettings();
        }catch (IOException e) {
            e.printStackTrace();
        }
        final String msg = formatter.format(new Date()) + " Error: " + message;
        Runnable runnable = () -> {
            try {
                terminal.setCursorPosition(startX, startY);
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setForegroundColor(TextColor.ANSI.RED);

                int lineCount = stopY - startY;
                int lineLength = stopX - startX;


                List<String> lines = getStrings(msg, lineLength);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        for (int j = 0; j < lineLength; j++) { //x
                            TextCharacter character = terminal.newTextGraphics().getCharacter(new TerminalPosition(startX + j, startY + i));
                            terminal.setCursorPosition(startX + j, startY + i - 1);
                            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                            terminal.setForegroundColor(TextColor.ANSI.valueOf(character.getForegroundColor().toString()));
                            terminal.putString(character.getCharacterString());
                        }
                    }
                    terminal.setCursorPosition(startX, startY + lineCount - 1);
                    terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                    terminal.setForegroundColor(TextColor.ANSI.RED);
                    if (line.length() < lineLength) {
                        terminal.putString(line + " ".repeat(lineLength - line.length()));
                    } else {
                        terminal.putString(line);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }

    /**
     * The method is used to display an error log in the terminal window.
     * @param format the format string
     * @param arg the argument
     */
    @Override
    public void error(String format, Object arg) {
        String msg = arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        error(msg);
    }

    /**
     * The method is used to display an error log in the terminal window.
     * @param format the format string
     * @param arg1 the first argument
     * @param arg2 the second argument
     */
    @Override
    public void error(String format, Object arg1, Object arg2) {
        String msg = arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        error(msg);
    }

    @Override
    public void error(String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        error(msg.toString());
    }

    @Override
    public void error(String msg, Throwable t) {
        String message = msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        error(message);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String msg) {
        String message = marker.getName() + ":\n" + msg;
        error(message);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        String msg = marker.getName() +":\n"+arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        String msg = marker.getName() +":\n"+arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        error(marker, msg.toString());
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        String message = marker.getName() + ":\n" + msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        error(message);
    }

    @Override
    public void info(String message) {
        try {
            FileOutputStream fot = new FileOutputStream("logInfo.txt", true);
            fot.write((formatter.format(new Date()) + " Info: " + message + "\n").getBytes());
            fot.close();
            drawSettings();
        }catch (IOException e) {
            e.printStackTrace();
        }
        final String msg = formatter.format(new Date()) + " Info: " + message;
        Runnable runnable = () -> {
            try {
                terminal.setCursorPosition(startX, startY);
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setForegroundColor(TextColor.ANSI.GREEN);

                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg, lineLength);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        for (int j = 0; j < lineLength; j++) { //x
                            TextCharacter character = terminal.newTextGraphics().getCharacter(new TerminalPosition(startX + j, startY + i));
                            terminal.setCursorPosition(startX + j, startY + i - 1);
                            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                            terminal.setForegroundColor(TextColor.ANSI.valueOf(character.getForegroundColor().toString()));
                            terminal.putString(character.getCharacterString());
                        }
                    }
                    terminal.setCursorPosition(startX, startY + lineCount - 1);
                    terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                    terminal.setForegroundColor(TextColor.ANSI.GREEN);
                    if (line.length() < lineLength) {
                        terminal.putString(line + " ".repeat(lineLength - line.length()));
                    } else {
                        terminal.putString(line);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }

    @Override
    public void info(String format, Object arg) {
        String msg = arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        info(msg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        String msg = arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        info(msg);
    }

    @Override
    public void info(String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        info(msg.toString());
    }

    @Override
    public void info(String msg, Throwable t) {
        String message = msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        info(message);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {
        String message = marker.getName() + ":\n" + msg;
        info(message);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        String msg = marker.getName() +":\n"+arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        String msg = marker.getName() +":\n"+arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        info(marker, msg.toString());
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        String message = marker.getName() + ":\n" + msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        info(message);
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String message) {
        try {
            FileOutputStream fot = new FileOutputStream("logWarn.txt", true);
            fot.write((formatter.format(new Date()) + " Warn: " + message + "\n").getBytes());
            fot.close();
            drawSettings();
        }catch (IOException e) {
            e.printStackTrace();
        }
        final String msg = formatter.format(new Date()) + " Warn: " + message;
        Runnable runnable = () -> {
            try {
                terminal.setCursorPosition(startX, startY);
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setForegroundColor(TextColor.ANSI.YELLOW);

                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg, lineLength);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        for (int j = 0; j < lineLength; j++) { //x
                            TextCharacter character = terminal.newTextGraphics().getCharacter(new TerminalPosition(startX + j, startY + i));
                            terminal.setCursorPosition(startX + j, startY + i - 1);
                            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                            terminal.setForegroundColor(TextColor.ANSI.valueOf(character.getForegroundColor().toString()));
                            terminal.putString(character.getCharacterString());
                        }
                    }
                    terminal.setCursorPosition(startX, startY + lineCount - 1);
                    terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                    terminal.setForegroundColor(TextColor.ANSI.YELLOW);
                    if (line.length() < lineLength) {
                        terminal.putString(line + " ".repeat(lineLength - line.length()));
                    } else {
                        terminal.putString(line);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }

    @Override
    public void warn(String format, Object arg) {
        String msg = arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        warn(msg.toString());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        String msg = arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        warn(msg);
    }

    @Override
    public void warn(String msg, Throwable t) {
        String message = msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        warn(message);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String msg) {
        String message = marker.getName() + ":\n" + msg;
        warn(message);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        String msg = marker.getName() +":\n"+arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        String msg = marker.getName() +":\n"+arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        warn(marker, msg.toString());
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        String message = marker.getName() + ":\n" + msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        warn(message);
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void debug(String message) {
        try {
            FileOutputStream fot = new FileOutputStream("logDebug.txt", true);
            fot.write((formatter.format(new Date()) + " Debug: " + message + "\n").getBytes());
            fot.close();
            drawSettings();
        }catch (IOException e) {
            e.printStackTrace();
        }
        final String msg = formatter.format(new Date()) + " Debug: " + message;
        Runnable runnable = () -> {
            try {
                terminal.setCursorPosition(startX, startY);
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setForegroundColor(TextColor.ANSI.BLUE);

                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg, lineLength);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        for (int j = 0; j < lineLength; j++) { //x
                            TextCharacter character = terminal.newTextGraphics().getCharacter(new TerminalPosition(startX + j, startY + i));
                            terminal.setCursorPosition(startX + j, startY + i - 1);
                            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                            terminal.setForegroundColor(TextColor.ANSI.valueOf(character.getForegroundColor().toString()));
                            terminal.putString(character.getCharacterString());
                        }
                    }
                    terminal.setCursorPosition(startX, startY + lineCount - 1);
                    terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                    terminal.setForegroundColor(TextColor.ANSI.BLUE);
                    if (line.length() < lineLength) {
                        terminal.putString(line + " ".repeat(lineLength - line.length()));
                    } else {
                        terminal.putString(line);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }

    @Override
    public void debug(String format, Object arg) {
        String msg = arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        debug(msg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        String msg = arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        debug(msg);
    }

    @Override
    public void debug(String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        debug(msg.toString());
    }

    @Override
    public void debug(String msg, Throwable t) {
        String message = msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        debug(message);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String msg) {
        String message = marker.getName() + ":\n" + msg;
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        String msg = marker.getName() +":\n"+arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        String msg = marker.getName() +":\n"+arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        debug(marker, msg.toString());
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        String message = marker.getName() + ":\n" + msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        debug(message);
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String message) {
        try {
            FileOutputStream fot = new FileOutputStream("logTrace.txt", true);
            fot.write((formatter.format(new Date()) + " Trace: " + message + "\n").getBytes());
            fot.close();
            drawSettings();
        }catch (IOException e) {
            e.printStackTrace();
        }
        final String msg = formatter.format(new Date()) + " Trace: " + message;
        Runnable runnable = () -> {
            try {
                terminal.setCursorPosition(startX, startY);
                terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                terminal.setForegroundColor(TextColor.ANSI.CYAN);

                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg, lineLength);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        for (int j = 0; j < lineLength; j++) { //x
                            TextCharacter character = terminal.newTextGraphics().getCharacter(new TerminalPosition(startX + j, startY + i));
                            terminal.setCursorPosition(startX + j, startY + i - 1);
                            terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                            terminal.setForegroundColor(TextColor.ANSI.valueOf(character.getForegroundColor().toString()));
                            terminal.putString(character.getCharacterString());
                        }
                    }
                    terminal.setCursorPosition(startX, startY + lineCount - 1);
                    terminal.setBackgroundColor(TextColor.ANSI.BLACK);
                    terminal.setForegroundColor(TextColor.ANSI.CYAN);
                    if (line.length() < lineLength) {
                        terminal.putString(line + " ".repeat(lineLength - line.length()));
                    } else {
                        terminal.putString(line);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }

    @Override
    public void trace(String format, Object arg) {
        String msg = arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        trace(msg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        String msg = arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        trace(msg);
    }

    @Override
    public void trace(String format, Object... arguments) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : arguments) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        trace(msg.toString());
    }

    @Override
    public void trace(String msg, Throwable t) {
        String message = msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        trace(message);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {
        String message = marker.getName() + ":\n" + msg;
        trace(message);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        String msg = marker.getName() +":\n"+arg.getClass().getName() + " " + arg.toString() + ":\n" + format;
        trace(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        String msg = marker.getName() +":\n"+arg1.getClass().getName() + " " + arg1.toString() + " " + arg2.getClass().getName() + " " + arg2.toString() + ":\n" + format;
        trace(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        StringBuilder msg = new StringBuilder();
        for (Object arg : argArray) {
            msg.append(arg.getClass().getName()).append(" ").append(arg.toString()).append(" ");
        }
        msg.append(":\n").append(format);
        trace(marker, msg.toString());
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        String message = marker.getName() + ":\n" + msg + "\n" + t.getClass().getName() + " " + t.getMessage();
        trace(message);
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    private static List<String> getStrings(String message, int lineLength) {
        String[] words = message.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (line.length() + word.length() > lineLength) {
                if (!line.isEmpty()) {
                    lines.add(line.toString());
                    line = new StringBuilder();
                }
            }
            if (!line.isEmpty()) {
                line.append(" ");
            }
            line.append(word);
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        return lines;
    }
}
