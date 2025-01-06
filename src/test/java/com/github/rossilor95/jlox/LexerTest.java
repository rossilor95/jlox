package com.github.rossilor95.jlox;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class LexerTest {

    @Test
    void shouldLexKeywords() {
        // Given
        var source = """
                and class else false for fun if nil or print return super this true var while""";

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(
                new Token.And(),
                new Token.Class(),
                new Token.Else(),
                new Token.False(),
                new Token.For(),
                new Token.Fun(),
                new Token.If(),
                new Token.Nil(),
                new Token.Or(),
                new Token.Print(),
                new Token.Return(),
                new Token.Super(),
                new Token.This(),
                new Token.True(),
                new Token.Var(),
                new Token.While(),
                new Token.EndOfFile());
    }

    @Test
    void shouldLexIdentifiers() {
        // Given
        var source = """
                foo
                fooBar
                foo123
                bar345baz""";

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(
                new Token.Identifier("foo"),
                new Token.Identifier("fooBar"),
                new Token.Identifier("foo123"),
                new Token.Identifier("bar345baz"),
                new Token.EndOfFile());
    }

    @Test
    void shouldLexNumberLiterals() {
        // Given
        var source = """
                123
                456.789""";

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(
                new Token.NumberLiteral(123),
                new Token.NumberLiteral(456.789),
                new Token.EndOfFile());
    }

    @Test
    void shouldLexStringLiterals() {
        // Given
        var source = """
                "Hello, World!"
                "Goodbye, Mars!""";

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(
                new Token.StringLiteral("Hello, World!"),
                new Token.StringLiteral("Goodbye, Mars!"),
                new Token.EndOfFile());
    }

    @Test
    void shouldLexSymbols() {
        // Given
        var source = """
                ! , . = > { ( < - + } ) ; / *
                == >= <= -= != += /= *=""";

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(
                new Token.Bang(),
                new Token.Comma(),
                new Token.Dot(),
                new Token.Equal(),
                new Token.Greater(),
                new Token.LeftBrace(),
                new Token.LeftParenthesis(),
                new Token.Less(),
                new Token.Minus(),
                new Token.Plus(),
                new Token.RightBrace(),
                new Token.RightParenthesis(),
                new Token.Semicolon(),
                new Token.Slash(),
                new Token.Star(),
                new Token.EqualEqual(),
                new Token.GreaterEqual(),
                new Token.LessEqual(),
                new Token.MinusEqual(),
                new Token.NotEqual(),
                new Token.PlusEqual(),
                new Token.SlashEqual(),
                new Token.StarEqual(),
                new Token.EndOfFile());
    }

    @Test
    void shouldSkipComments() {
        // Given
        var source = """
                // This is a / comment

                /* This is a block
                 * comment
                 */""";

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(new Token.EndOfFile());
    }

    @Test
    void shouldSkipWhitespace() {
        // Given
        var source = """
                \t \n \r \n\r  """;

        // When
        var lexer = new Lexer(source);
        var tokens = new ArrayList<Token>();
        lexer.forEachRemaining(tokens::add);

        // Then
        assertThat(tokens).containsExactly(new Token.EndOfFile());
    }
}