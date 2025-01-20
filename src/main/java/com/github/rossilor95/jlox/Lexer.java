package com.github.rossilor95.jlox;

import java.util.function.Supplier;

public final class Lexer {

    private final String source;
    private int position = -1;

    public Lexer(String source) {
        this.source = source;
    }

    public Token readToken() {
        advance(1);
        char ch = isAtEnd() ? '\0' : source.charAt(position);
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
            case '=' -> peekChar(1) == '=' ? advanceAndGet(Token.EqualEqual::new) : new Token.Equal();
            case '!' -> peekChar(1) == '=' ? advanceAndGet(Token.NotEqual::new) : new Token.Bang();
            case '<' -> peekChar(1) == '=' ? advanceAndGet(Token.LessEqual::new) : new Token.Less();
            case '>' -> peekChar(1) == '=' ? advanceAndGet(Token.GreaterEqual::new) : new Token.Greater();
            case '-' -> peekChar(1) == '=' ? advanceAndGet(Token.MinusEqual::new) : new Token.Minus();
            case '+' -> peekChar(1) == '=' ? advanceAndGet(Token.PlusEqual::new) : new Token.Plus();
            case '*' -> peekChar(1) == '=' ? advanceAndGet(Token.StarEqual::new) : new Token.Star();
            case '/' -> handleSlash();
            case '\0' -> new Token.EndOfFile();
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

    Token peek(int offset) {
        int start = position;
        Token token = readToken();
        for (int i = 1; i < offset; i += 1) {
            token = readToken();
        }
        position = start;
        return token;
    }

    private void advance(int steps) {
        position += steps;
    }

    private Token advanceAndGet(Supplier<Token> supplier) {
        advance(1);
        return supplier.get();
    }

    private void advanceWhile(Supplier<Boolean> condition) {
        while (condition.get() && !isAtEnd()) {
            advance(1);
        }
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private Token handleSlash() {
        return switch (peekChar(1)) {
            case '/' -> skipComment();
            case '*' -> skipBlockComment();
            case '=' -> advanceAndGet(Token.SlashEqual::new);
            default -> new Token.Slash();
        };
    }

    private char peekChar(int offset) {
        int targetPosition = position + offset;
        return targetPosition >= source.length() ? '\0' : source.charAt(targetPosition);
    }

    private Token readName() {
        int start = position;
        advanceWhile(() -> Character.isLetterOrDigit(peekChar(1)));
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
        advanceWhile(() -> Character.isDigit(peekChar(1)));
        if (peekChar(1) == '.' && Character.isDigit(peekChar(2))) {
            advance(1); // consume the '.'
            advanceWhile(() -> Character.isDigit(peekChar(1)));
        }
        double lexeme = Double.parseDouble(source.substring(start, position + 1));
        return new Token.NumberLiteral(lexeme);
    }

    private Token.StringLiteral readString() {
        int start = position;
        advanceWhile(() -> peekChar(1) != '"');

        Token.StringLiteral tok;
        if (isAtEnd()) {
            tok = new Token.StringLiteral("");
        } else {
            advance(1); // consume the closing '"'
            tok = new Token.StringLiteral(source.substring(start + 1, position));
        }
        return tok;
    }

    private Token skipComment() {
        advanceWhile(() -> peekChar(1) != '\n');
        advance(1); // consume the newline
        return readToken();
    }

    private Token skipBlockComment() {
        advanceWhile(() -> peekChar(1) != '*' || peekChar(2) != '/');
        advance(2); // consume the '*' and '/'
        return readToken();
    }

    private Token skipWhitespace() {
        advanceWhile(() -> Character.isWhitespace(peekChar(1)));
        return readToken();
    }
}
