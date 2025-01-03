package com.github.rossilor95.jlox.parse;

public sealed interface Token {
    record EndOfFile() implements Token { }
    record Illegal() implements Token { }

    // Single-character tokens.
    record LeftParen() implements Token { }
    record RightParen() implements Token { }
    record LeftBrace() implements Token { }
    record RightBrace() implements Token { }
    record Comma() implements Token { }
    record Dot() implements Token { }
    record Semicolon() implements Token { }

    // One or two character tokens.
    record Bang() implements Token { }
    record BangEqual() implements Token { }
    record Equal() implements Token { }
    record EqualEqual() implements Token { }
    record Greater() implements Token { }
    record GreaterEqual() implements Token { }
    record Less() implements Token { }
    record LessEqual() implements Token { }
    record Minus() implements Token { }
    record MinusEqual() implements Token { }
    record Plus() implements Token { }
    record PlusEqual() implements Token { }
    record Slash() implements Token { }
    record SlashEqual() implements Token { }
    record Star() implements Token { }
    record StarEqual() implements Token { }

    // Identifier and literals.
    record Identifier(String lexeme) implements Token { }
    record NumberLiteral(double lexeme) implements Token { }
    record StringLiteral(String lexeme) implements Token { }

    // Keywords.
    record And() implements Token { }
    record Class() implements Token { }
    record Else() implements Token { }
    record False() implements Token { }
    record Fun() implements Token { }
    record For() implements Token { }
    record If() implements Token { }
    record Nil() implements Token { }
    record Or() implements Token { }
    record Print() implements Token { }
    record Return() implements Token { }
    record Super() implements Token { }
    record This() implements Token { }
    record True() implements Token { }
    record Var() implements Token { }
    record While() implements Token { }
}
