package com.github.rossilor95.jlox;

import java.util.function.Supplier;

public final class Lexer {

    private final char NULL_CHAR = '\0';

    private final String source;
    private int position = -1;

    public Lexer(String source) {
        this.source = source;
    }

    public Token readToken() {
        char ch = nextChar();
        return switch (ch) {
            case ' ', '\n', '\r', '\t' -> skipWhitespace();
            case '{' -> new Token.LeftBrace();
            case '}' -> new Token.RightBrace();
            case '(' -> new Token.LeftParenthesis();
            case ')' -> new Token.RightParenthesis();
            case ';' -> new Token.Semicolon();
            case ',' -> new Token.Comma();
            case '.' -> new Token.Dot();
            case '"' -> readString();
            case '=' -> peek(1) == '=' ? advanceAndGet(Token.EqualEqual::new) : new Token.Equal();
            case '!' -> peek(1) == '=' ? advanceAndGet(Token.NotEqual::new) : new Token.Bang();
            case '<' -> peek(1) == '=' ? advanceAndGet(Token.LessEqual::new) : new Token.Less();
            case '>' -> peek(1) == '=' ? advanceAndGet(Token.GreaterEqual::new) : new Token.Greater();
            case '-' -> peek(1) == '=' ? advanceAndGet(Token.MinusEqual::new) : new Token.Minus();
            case '+' -> peek(1) == '=' ? advanceAndGet(Token.PlusEqual::new) : new Token.Plus();
            case '*' -> peek(1) == '=' ? advanceAndGet(Token.StarEqual::new) : new Token.Star();
            case '/' -> handleSlash();
            case NULL_CHAR -> new Token.EndOfFile();
            default -> {
                if (Character.isLetter(ch)) {
                    yield readName();
                }
                if (Character.isDigit(ch)) {
                    yield readNumber();
                } else {
                    yield new Token.Illegal();
                }
            }
        };
    }

    private void advance() {
        position += 1;
    }

    private Token advanceAndGet(Supplier<Token> supplier) {
        advance();
        return supplier.get();
    }

    private void advanceWhile(Supplier<Boolean> condition) {
        while (condition.get() && !isAtEnd()) {
            advance();
        }
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private Token handleSlash() {
        return switch (peek(1)) {
            case '/' -> skipComment();
            case '*' -> skipBlockComment();
            case '=' -> advanceAndGet(Token.SlashEqual::new);
            default -> new Token.Slash();
        };
    }

    private char nextChar() {
        advance();
        return isAtEnd() ? NULL_CHAR : source.charAt(position);
    }

    private char peek(final int ahead) {
        int index = position + ahead;
        return index >= source.length() ? NULL_CHAR : source.charAt(index);
    }

    private Token readName() {
        int start = position;
        advanceWhile(() -> Character.isLetterOrDigit(peek(1)));
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

    private Token.NumberLiteral readNumber() {
        int start = position;
        advanceWhile(() -> Character.isDigit(peek(1)));
        if (peek(1) == '.' && Character.isDigit(peek(2))) {
            advance();
            advanceWhile(() -> Character.isDigit(peek(1)));
        }
        double lexeme = Double.parseDouble(source.substring(start, position + 1));
        return new Token.NumberLiteral(lexeme);
    }

    private Token.StringLiteral readString() {
        int start = position;
        advanceWhile(() -> peek(1) != '"');

        Token.StringLiteral tok;
        if (isAtEnd()) {
            tok = new Token.StringLiteral("");
        } else {
            advance();
            tok = new Token.StringLiteral(source.substring(start + 1, position));
        }
        return tok;
    }

    private Token skipComment() {
        advanceWhile(() -> peek(1) != '\n');
        advance(); // consume the newline
        return readToken();
    }

    private Token skipBlockComment() {
        advanceWhile(() -> peek(1) != '*' || peek(2) != '/');
        advance(); // consume the '*'
        advance(); // consume the '/'
        return readToken();
    }

    private Token skipWhitespace() {
        advanceWhile(() -> Character.isWhitespace(peek(1)));
        return readToken();
    }
}
