# Lanterna Logger Provider
### SLF4J Logger Provider for Lanterna

![Maven Central](https://img.shields.io/maven-central/v/ru.zoommax/LanternaLoggerProvider?style=plastic)
![GitHub](https://img.shields.io/github/license/ZooMMaX/LanternaLoggerProvider?style=plastic)
[![GitHub issues](https://img.shields.io/github/issues/ZooMMaX/bitcoin-rpc-client?style=plastic)](https://github.com/ZooMMaX/LanternaLoggerProvider/issues)


![screenshot](https://github.com/ZooMMaX/LanternaLoggerProvider/blob/26caac5dd6127f28705a623aa7956ac53543ffa9/screen02.jpg?raw=true)



## Dependency

![dependency maven](https://img.shields.io/badge/DEPENDENCY-Maven-C71A36?style=plastic&logo=apachemaven)
```xml
<dependencies>
    <dependency>
        <groupId>ru.zoommax</groupId>
        <artifactId>LanternaLoggerProvider</artifactId>
        <version>1.1</version>
    </dependency>
</dependencies>
```

![dependency gradle](https://img.shields.io/badge/DEPENDENCY-Gradle-02303A?style=plastic&logo=gradle)
```groovy
iimplementation 'ru.zoommax:LanternaLoggerProvider:1.1'
```

### How to use

To start displaying logs, you need to pass the `terminal` variable to `LanternaLoggerView.getInstance(Terminal terminal)`.

```java
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
```
