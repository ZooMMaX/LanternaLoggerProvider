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
import com.googlecode.lanterna.terminal.swing.AWTTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zoommax.llp.LanternaLoggerView;

import java.io.IOException;

public class Main {
    public static Terminal terminal;
    public static Screen screen;
    public static MultiWindowTextGUI gui;
    public static void main(String[] args) {
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
            // Create a LanternaLoggerView window and pass the terminal variable
            Window info = LanternaLoggerView.getInstance(terminal);
            // Set the window size
            info.setFixedSize(new TerminalSize(terminal.getTerminalSize().getColumns() - 5, terminal.getTerminalSize().getRows() / 4));
            // Add the window to MultiWindowTextGui
            gui.addWindow(info);
            // Set the position on the screen
            info.setPosition(new TerminalPosition(1, terminal.getTerminalSize().getRows() - info.getDecoratedSize().getRows()));
            // Update the screen
            info.getTextGUI().updateScreen();

            // Write a log
            logger.info("App started");
            gui.waitForWindowToClose(info);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}