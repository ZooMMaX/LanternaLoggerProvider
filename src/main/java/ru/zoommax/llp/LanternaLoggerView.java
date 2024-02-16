package ru.zoommax.llp;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.terminal.Terminal;

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
public class LanternaLoggerView extends BasicWindow {

    private static LanternaLoggerView instance = null;


    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private static int startX = -1;
    private static int startY = -1;
    private static int stopX = -1;
    private static int stopY = -1;

    private Terminal terminal = null;
    ExecutorService service;

    Panel panel = new Panel();
    List<Label> labels = new ArrayList<>();

    /**
     * The constructor is private so that the class cannot be instantiated from outside.
     * It is used to create a single instance of the class.
     */
    private LanternaLoggerView() {
        super("Lanterna Logger");
        panel.setLayoutManager(new LinearLayout());
        setComponent(panel);
    }

    /**
     * The method is used to get the instance of the class.
     * If the instance does not exist, it is created.
     * @return the instance of the class
     */
    public static LanternaLoggerView getInstance() {
        if (instance == null) {
            instance = new LanternaLoggerView();
            instance.service = Executors.newFixedThreadPool(1);
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
            instance.service = Executors.newFixedThreadPool(1);
        }
        instance.terminal = terminal;
        return instance;
    }

    private void drawSettings() throws IOException {
        if (startX == -1 && startY == -1) {
            startX = getPosition().getColumn() + 2;
            startY = getPosition().getRow() + 1;
            stopX = getPosition().getColumn() + getSize().getColumns() + 2;
            stopY = getPosition().getRow() + getSize().getRows() + 1;
            int lineCount = stopY - startY;
            for (int i = 0; i < lineCount; i++) {
                Label label = new Label(" ".repeat(stopX - startX));
                label.setBackgroundColor(TextColor.ANSI.BLACK);
                label.setForegroundColor(TextColor.ANSI.BLACK);
                labels.add(label);
                panel.addComponent(label);
            }
        }
    }

    /**
     * The method is used to display an error log in the terminal window.
     * @param message the message to be displayed
     */
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
                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        Label label = labels.get(i);
                        labels.get(i-1).setText(label.getText());
                        labels.get(i-1).setBackgroundColor(label.getBackgroundColor());
                        labels.get(i-1).setForegroundColor(label.getForegroundColor());
                    }
                    if (line.length() < lineLength) {
                        labels.get(lineCount - 1).setText(line + " ".repeat(lineLength - line.length()));
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.RED);
                    } else {
                        labels.get(lineCount - 1).setText(line);
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.RED);
                    }
                }
                getTextGUI().updateScreen();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }


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
                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        Label label = labels.get(i);
                        labels.get(i-1).setText(label.getText());
                        labels.get(i-1).setBackgroundColor(label.getBackgroundColor());
                        labels.get(i-1).setForegroundColor(label.getForegroundColor());
                    }
                    if (line.length() < lineLength) {
                        labels.get(lineCount - 1).setText(line + " ".repeat(lineLength - line.length()));
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.GREEN);
                    } else {
                        labels.get(lineCount - 1).setText(line);
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.GREEN);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }


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
                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        Label label = labels.get(i);
                        labels.get(i-1).setText(label.getText());
                        labels.get(i-1).setBackgroundColor(label.getBackgroundColor());
                        labels.get(i-1).setForegroundColor(label.getForegroundColor());
                    }
                    if (line.length() < lineLength) {
                        labels.get(lineCount - 1).setText(line + " ".repeat(lineLength - line.length()));
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.YELLOW);
                    } else {
                        labels.get(lineCount - 1).setText(line);
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.YELLOW);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }


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
                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        Label label = labels.get(i);
                        labels.get(i-1).setText(label.getText());
                        labels.get(i-1).setBackgroundColor(label.getBackgroundColor());
                        labels.get(i-1).setForegroundColor(label.getForegroundColor());
                    }
                    if (line.length() < lineLength) {
                        labels.get(lineCount - 1).setText(line + " ".repeat(lineLength - line.length()));
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.BLUE);
                    } else {
                        labels.get(lineCount - 1).setText(line);
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.BLUE);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }


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
                int lineCount = stopY - startY;
                int lineLength = stopX - startX;

                List<String> lines = getStrings(msg);
                for (String line : lines) {
                    for (int i = 1; i < lineCount; i++) { //y
                        Label label = labels.get(i);
                        labels.get(i-1).setText(label.getText());
                        labels.get(i-1).setBackgroundColor(label.getBackgroundColor());
                        labels.get(i-1).setForegroundColor(label.getForegroundColor());
                    }
                    if (line.length() < lineLength) {
                        labels.get(lineCount - 1).setText(line + " ".repeat(lineLength - line.length()));
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.CYAN);
                    } else {
                        labels.get(lineCount - 1).setText(line);
                        labels.get(lineCount - 1).setBackgroundColor(TextColor.ANSI.BLACK);
                        labels.get(lineCount - 1).setForegroundColor(TextColor.ANSI.CYAN);
                    }
                }
                getTextGUI().updateScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        service.submit(runnable);
    }


    private static List<String> getStrings(String message) {
        int lineLength = stopX - startX;
        List<String> words = new ArrayList<>();
        String[] wordsList = message.split(" ");
        for (String string : wordsList) {
            if (string.length() > lineLength) {
                String s = string;
                while (s.length() > lineLength) {
                    words.add(s.substring(0, lineLength));
                    s = s.substring(lineLength);
                }
                words.add(s);
            } else {
                words.add(string);
            }
        }
        return getLines(words, lineLength);
    }

    private static List<String> getLines(List<String> words, int lineLength) {
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
