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
        <version>1.2</version>
    </dependency>
</dependencies>
```

![dependency gradle](https://img.shields.io/badge/DEPENDENCY-Gradle-02303A?style=plastic&logo=gradle)
```groovy
iimplementation 'ru.zoommax:LanternaLoggerProvider:1.2'
```

## How to use

### Settings and Default Values

The `LanternaLoggerSettings` class provides several settings that you can adjust according to your needs. Here are the settings and their default values:

- `SETTINGS_DEBUG`: This setting controls whether debug logs are displayed. The default value is `false`.
- `SETTINGS_ERROR`: This setting controls whether error logs are displayed. The default value is `false`.
- `SETTINGS_INFO`: This setting controls whether info logs are displayed. The default value is `false`.
- `SETTINGS_TRACE`: This setting controls whether trace logs are displayed. The default value is `false`.
- `SETTINGS_WARN`: This setting controls whether warning logs are displayed. The default value is `false`.

You can also control whether these logs are saved to a file:

- `SETTINGS_SAVE_LOG_DEBUG`: This setting controls whether debug logs are saved to a file. The default value is `false`.
- `SETTINGS_SAVE_LOG_ERROR`: This setting controls whether error logs are saved to a file. The default value is `false`.
- `SETTINGS_SAVE_LOG_INFO`: This setting controls whether info logs are saved to a file. The default value is `false`.
- `SETTINGS_SAVE_LOG_TRACE`: This setting controls whether trace logs are saved to a file. The default value is `false`.
- `SETTINGS_SAVE_LOG_WARN`: This setting controls whether warning logs are saved to a file. The default value is `false`.

You can change these settings in your code like this:

```java
LanternaLoggerSettings.SETTINGS_DEBUG = true; // This will enable debug logs
LanternaLoggerSettings.SETTINGS_SAVE_LOG_ERROR = true; // This will enable saving error logs to a file
```

### Example
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
            // Create a LanternaLoggerView window. LanternaLoggerView is a singleton.
            // You can get the window using the getInstance() method.
            // The size of the log output area automatically adjusts to the size of the window.
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
```
