package com.github.rossilor95.jlox;

import com.github.rossilor95.jlox.parse.Scanner;
import com.github.rossilor95.jlox.parse.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class JLox {

    private static final int EXIT_USAGE = 64;
    private static final int EXIT_DATAERR = 65;
    private static final String PROMPT = "jlox> ";

    public static void main(String[] args) throws IOException {
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

    private void runFile(String pathString) throws IOException {
        var path = Path.of(pathString);
        List<Token> tokens = scanTokens(Files.readString(path));
        tokens.forEach(System.out::println);
        if (tokens.stream().anyMatch(tok -> tok instanceof Token.Illegal)) {
            System.exit(EXIT_DATAERR);
        }
    }

    private void runPrompt() {
        System.out.println("Interactive mode");
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print(PROMPT);
                List<Token> tokens = scanTokens(reader.readLine());
                tokens.forEach(System.out::println);
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private ArrayList<Token> scanTokens(String source) {
        var scanner = new Scanner(source);
        var tokens = new ArrayList<Token>();
        scanner.forEachRemaining(tokens::add);
        return tokens;
    }
}
