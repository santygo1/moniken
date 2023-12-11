package ru.moniken.events;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.moniken.config.MonikenConfig;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
class ConsoleInfoPrinter implements ApplicationListener<ApplicationReadyEvent> {

    // Удаляет ANSI символы из строки
    static final String DELETE_ANSI_REGEX =  "\u001B\\[[\\d;]*[^\\d;]";
    private enum AnsiColor {
        BLACK("\u001B[30m"),
        RESET("\u001B[0m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        RED("\u001B[31m"),
        WHITE("\u001B[37m"),
        GREEN("\u001B[32m");

        public final String code;
        AnsiColor(String code){
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private final static int LOGO_LENGTH = 44;
    private final static String logo = """
            ___  ___               _  _                 \s
            |  \\/  |              (_)| |                \s
            | .  . |  ___   _ __   _ | | __  ___  _ __  \s
            | |\\/| | / _ \\ | '_ \\ | || |/ / / _ \\| '_ \\ \s
            | |  | || (_) || | | || ||   < |  __/| | | |\s
            \\_|  |_/ \\___/ |_| |_||_||_|\\_\\ \\___||_| |_|""";


    final MonikenConfig moniken;

    @Value("${server.port}")
    String port;

    @Value("${springdoc.api-docs.path}")
    String apiDocsPath;

    @Value("${springdoc.swagger-ui.path}")
    String swaggerPath;

    @Value("${moniken.h2}")
    String h2;

    @Value("${spring.h2.console.path}")
    String h2Path;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!moniken.isWelcomeConsole()) return;
        printlnField(
                colorful(":: Moniken ::", AnsiColor.BLUE),
                String.format("(v%s)", moniken.getVersion()));
        printlnField("Moniken was launched on the port:", port);
        System.out.println();
        printTitle(colorful("MAP", AnsiColor.BLUE ));

        String monikenEndpoint = moniken.getEndpoint();
        printlnField("UI:", monikenEndpoint + "/ui");
        printlnField("Collections:", monikenEndpoint + "/collections");
        printlnField("Routes:", monikenEndpoint + "/routes");

        if (swaggerPath != null) printlnField("Swagger:", swaggerPath);
        if (apiDocsPath != null) printlnField("Api Docs:", apiDocsPath);
        if (Boolean.parseBoolean(h2)) printlnField("H2:", h2Path);

    }
    private static void printlnField(String left, String right) {
        int spaceCount = LOGO_LENGTH - left.replaceAll(DELETE_ANSI_REGEX,"").length()
                - right.replaceAll(DELETE_ANSI_REGEX,"").length();
        spaceCount = spaceCount > 0 ? spaceCount : 2;

        System.out.println(left + " ".repeat(spaceCount) + right);
    }

    private static void printTitle(String title) {
        int space = LOGO_LENGTH - title.replaceAll(DELETE_ANSI_REGEX,"").length();
        space = Math.max(space, 0);
        final String spaceSymbol = "=";
        String leftIndent = spaceSymbol.repeat(space / 2 - 1);
        String rightIndent = space % 2 == 0 ? leftIndent : leftIndent + spaceSymbol;
        leftIndent = leftIndent + " ";
        rightIndent = " " + rightIndent;
        System.out.println(leftIndent + title + rightIndent);
    }

    private static String colorful(String text, AnsiColor color) {
        return color.getCode() + text + AnsiColor.RESET.getCode();
    }
}
