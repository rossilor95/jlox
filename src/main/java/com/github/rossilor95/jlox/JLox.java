package com.github.rossilor95.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class JLox {

    private static final int EXIT_USAGE = 64;

    public static void main(String[] args) {
        var jlox = new JLox();
        switch (args.length) {
            case 0 -> jlox.runPrompt();
            case 1 -> jlox.runFile(args[0]);
            default -> {
                System.err.println("Usage: jlox [script]");
                System.exit(EXIT_USAGE);
            }
        }
    }

    private static void run(String line) {
        System.out.println(line);
    }

    private void runPrompt() {
        System.out.println("Interactive mode");
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            reader.lines().forEach(JLox::run);
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private void runFile(String pathString) {
        try (var lines = Files.lines(Path.of(pathString))) {
            lines.forEach(JLox::run);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
