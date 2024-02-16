package com.example;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zoommax.llp.LanternaLoggerSettings;
import ru.zoommax.llp.LanternaLoggerView;

import java.io.IOException;

public class Main {
    public static Terminal terminal;
    public static Screen screen;
    public static MultiWindowTextGUI gui;
    public static void main(String[] args) {
        // Change log levels
        LanternaLoggerSettings.SETTINGS_DEBUG = true;
        LanternaLoggerSettings.SETTINGS_ERROR = true;
        LanternaLoggerSettings.SETTINGS_INFO = true;
        LanternaLoggerSettings.SETTINGS_TRACE = true;
        LanternaLoggerSettings.SETTINGS_WARN = true;

        // Change save log levels to file
        LanternaLoggerSettings.SETTINGS_SAVE_LOG_DEBUG = true;
        LanternaLoggerSettings.SETTINGS_SAVE_LOG_ERROR = true;
        LanternaLoggerSettings.SETTINGS_SAVE_LOG_INFO = true;
        LanternaLoggerSettings.SETTINGS_SAVE_LOG_TRACE = true;
        LanternaLoggerSettings.SETTINGS_SAVE_LOG_WARN = true;

        // Create a logger according to slf4j-api
        Logger logger = LoggerFactory.getLogger(Main.class.getName());
        try {
            // Assign a value to the terminal
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            try {
                screen.startScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DefaultWindowManager windowManager = new DefaultWindowManager();
            EmptySpace emptySpace = new EmptySpace(TextColor.ANSI.BLUE);
            gui = new MultiWindowTextGUI(screen, windowManager, emptySpace);
            // Create a LanternaLoggerView window
            Window info = LanternaLoggerView.getInstance();
            // Set the window size
            info.setFixedSize(new TerminalSize(terminal.getTerminalSize().getColumns() - 5, terminal.getTerminalSize().getRows() / 4));
            // Add the window to MultiWindowTextGui
            gui.addWindow(info);
            // Set the position on the screen
            info.setPosition(new TerminalPosition(1, terminal.getTerminalSize().getRows() - info.getDecoratedSize().getRows()));
            // Update the screen
            info.getTextGUI().updateScreen();

            // Write a log
            logger.info("Example info log");
            logger.error("Example error log");
            logger.warn("Example warn log");
            logger.debug("Example debug log");
            logger.trace("Example trace log");
            logger.info(/*long string*/ "Example long string log \"hjdfaiush;asuhgl;asuhfgliashugliasuygp;oigyuaiosufdyghagyhlasiugyhaljkhfgalkdfjghadkfjghalkdfjghlakfjgh" +
                    "lasfjdghkldafjghladksfjghalkfjghalkfjhgakfjghalskfjhgafjghadfjghaldfgjkhalkjfghoufyhgfjkghsdjfhglsuoyhva'krltgujpaoirytgua;siryhtgpsor8tuygfp89y4u5hjt;" +
                    "iufgyoisduryfgtohle8iryugljhdflkjghsldfkugy978yhvbaelrkuity;ao9iwserjf;a98yhr;goasdfknmgp;or8ewi9uytrguhrg\"");
            gui.waitForWindowToClose(info);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}