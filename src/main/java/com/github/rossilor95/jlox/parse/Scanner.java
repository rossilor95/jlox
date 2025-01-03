package com.github.rossilor95.jlox.parse;

import java.util.Iterator;
import java.util.function.Supplier;

public final class Scanner implements Iterator<Token> {

    private static final char CARRIAGE_RETURN = '\r';
    private static final char DIGIT_SEPARATOR = '_';
    private static final char HORIZONTAL_TAB = '\t';
    private static final char LINE_FEED = '\n';
    private static final char NULL_CHAR = '\0';

    private final String source;
    private int position = -1;
    private int line = 1;
    private int column = 0;

    public Scanner(String source) {
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return (position + 1) < source.length();
    }

    @Override
    public Token next() {
        var ch = nextChar();
        return switch (ch) {
            case ' ', CARRIAGE_RETURN, HORIZONTAL_TAB, LINE_FEED -> skipWhitespace(ch);
            case '"' -> scanString();
            case '(' -> new Token.LeftParen();
            case ')' -> new Token.RightParen();
            case '{' -> new Token.LeftBrace();
            case '}' -> new Token.RightBrace();
            case ',' -> new Token.Comma();
            case '.' -> new Token.Dot();
            case ';' -> new Token.Semicolon();
            case '!' -> peek() == '=' ? advanceAndGet(Token.BangEqual::new) : new Token.Bang();
            case '=' -> peek() == '=' ? advanceAndGet(Token.EqualEqual::new) : new Token.Equal();
            case '<' -> peek() == '=' ? advanceAndGet(Token.LessEqual::new) : new Token.Less();
            case '>' -> peek() == '=' ? advanceAndGet(Token.GreaterEqual::new) : new Token.Greater();
            case '-' -> peek() == '=' ? advanceAndGet(Token.MinusEqual::new) : new Token.Minus();
            case '+' -> peek() == '=' ? advanceAndGet(Token.PlusEqual::new) : new Token.Plus();
            case '*' -> peek() == '=' ? advanceAndGet(Token.StarEqual::new) : new Token.Star();
            case '/' -> handleSlash();
            case NULL_CHAR -> new Token.EndOfFile();
            default -> {
                if (Character.isDigit(ch)) {
                    yield scanNumber();
                } else if (Character.isLetter(ch)) {
                    yield scanIdentifier();
                } else {
                    System.err.println("[" + line + ":" + column + "] Unexpected character: " + ch);
                    yield new Token.Illegal();
                }
            }
        };
    }

    private void advance() {
        position += 1;
        column += 1;
    }

    private Token advanceAndGet(Supplier<Token> token) {
        advance();
        return token.get();
    }

    private void advanceWhile(Supplier<Boolean> condition) {
        while (condition.get() && hasNext()) {
            if (isNewline(peek())) {
                handleNewline();
            }
            advance();
        }
    }

    private Token handleSlash() {
        return switch (peek()) {
            case '/' -> {
                advanceWhile(() -> !isNewline(peek()));
                yield next();
            }
            case '*' -> {
                advanceWhile(() -> peek() != '*' || peekNext() != '/');
                advance(); // consume the '*'
                advance(); // consume the '/'
                yield next();
            }
            case '=' -> advanceAndGet(Token.SlashEqual::new);
            default -> new Token.Slash();
        };
    }

    private boolean isNewline(char ch) {
        return ch == LINE_FEED || ch == CARRIAGE_RETURN;
    }

    private char nextChar() {
        char c = NULL_CHAR;
        if (hasNext()) {
            advance();
            c = source.charAt(position);
        }
        return c;
    }

    private char peek() {
        return hasNext() ? source.charAt(position + 1) : NULL_CHAR;
    }

    private char peekNext() {
        return (position + 2) < source.length() ? source.charAt(position + 2) : NULL_CHAR;
    }

    private Token scanIdentifier() {
        int start = position;
        advanceWhile(() -> Character.isLetterOrDigit(peek()));
        String lexeme = source.substring(start, position + 1);
        return switch (lexeme) {
            case "and" -> new Token.And();
            case "class" -> new Token.Class();
            case "else" -> new Token.Else();
            case "false" -> new Token.False();
            case "for" -> new Token.For();
            case "fun" -> new Token.Fun();
            case "if" -> new Token.If();
            case "nil" -> new Token.Nil();
            case "or" -> new Token.Or();
            case "print" -> new Token.Print();
            case "return" -> new Token.Return();
            case "super" -> new Token.Super();
            case "this" -> new Token.This();
            case "true" -> new Token.True();
            case "var" -> new Token.Var();
            case "while" -> new Token.While();
            default -> new Token.Identifier(lexeme);
        };
    }

    private Token scanNumber() {
        int start = position;
        advanceWhile(() -> Character.isDigit(peek()) || (peek() == DIGIT_SEPARATOR && Character.isDigit(peekNext())));

        Token tok;
        if ((peek() == DIGIT_SEPARATOR || peek() == '.') && !Character.isDigit(peekNext())) {
            System.err.println("[" + line + ":" + column + "] Malformed number literal.");
            advanceWhile(() -> Character.isDigit(peek()) || peek() == DIGIT_SEPARATOR || peek() == '.');
            tok = new Token.Illegal();
        } else if (peek() == '.' && Character.isDigit(peekNext())) {
            advance(); // consume the dot '.'
            advanceWhile(() -> Character.isDigit(peek()));
            double lexeme = Double.parseDouble(source.substring(start, position + 1).replace("_", ""));
            tok = new Token.NumberLiteral(lexeme);
        } else {
            double lexeme = Double.parseDouble(source.substring(start, position + 1).replace("_", ""));
            tok = new Token.NumberLiteral(lexeme);
        }
        return tok;
    }

    private Token scanString() {
        int start = position;
        advanceWhile(() -> peek() != '"' && !isNewline(peek()));

        Token tok;
        if (!hasNext()) {
            System.err.println("[" + line + ":" + column + "] ERROR: Unterminated string.");
            tok = new Token.Illegal();
        } else {
            advance(); // advance past the closing quote
            String lexeme = source.substring(start + 1, position);
            tok = new Token.StringLiteral(lexeme);
        }
        return tok;
    }

    private Token skipWhitespace(char ch) {
        if (isNewline(ch)) {
            handleNewline();
        }
        return next();
    }

    private void handleNewline() {
        line += 1;
        column = 1;
    }
}
