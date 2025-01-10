package com.github.rossilor95.jlox;

public sealed interface Token {
    record EndOfFile() implements Token { }
    record Illegal() implements Token { }

    record And() implements Token { }
    record Class() implements Token { }
    record Else() implements Token { }
    record False() implements Token { }
    record For() implements Token { }
    record Fun() implements Token { }
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

    record Identifier(String name) implements Token { }
    record NumberLiteral(double value) implements Token { }
    record StringLiteral(String value) implements Token { }

    record Bang() implements Token { }
    record Comma() implements Token { }
    record Dot() implements Token { }
    record Equal() implements Token { }
    record Greater() implements Token { }
    record LeftBrace() implements Token { }
    record LeftParenthesis() implements Token { }
    record Less() implements Token { }
    record Minus() implements Token { }
    record Plus() implements Token { }
    record RightBrace() implements Token { }
    record RightParenthesis() implements Token { }
    record Semicolon() implements Token { }
    record Slash() implements Token { }
    record Star() implements Token { }

    record EqualEqual() implements Token { }
    record GreaterEqual() implements Token { }
    record LessEqual() implements Token { }
    record MinusEqual() implements Token { }
    record NotEqual() implements Token { }
    record PlusEqual() implements Token { }
    record SlashEqual() implements Token { }
    record StarEqual() implements Token { }
}
