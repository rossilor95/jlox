package com.github.rossilor95.jlox.parse;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScannerTest {

    @Test
    void shouldScanTokens() {
        // Given
        var source = """
                // This is a / comment
                
                /*
                This is a block * comment.
                It can span multiple lines.
                */
                
                5_000.10 <= 10.10 == 5 >= 10 != 5.2;
                1 - 2 / 3 * 4 /= 5 *= 6;

                true and false or nil;

                fun myFun(num1, num2) {
                    var result = 0;
                    if (num1 > num2) {
                        result = num1;
                    } else {
                        result = num2;
                    }
                    return result
                }

                for (var i = 0; i < 10; i += 1) {
                    result -= i;
                }

                while(!true) {
                    print "This will never print";
                }

                class Person {
                    init(name) {
                        this.name = name;
                    }
                }

                class Employee < Person {
                    init(name, title) {
                        super.init(name);
                        this.title = title;
                    }
                }""";

        var scanner = new Scanner(source);

        // When
        var tokens = scanTokens(scanner);

        // Then
        assertThat(tokens).containsExactly(
                new Token.NumberLiteral(5000.10),
                new Token.LessEqual(),
                new Token.NumberLiteral(10.10),
                new Token.EqualEqual(),
                new Token.NumberLiteral(5),
                new Token.GreaterEqual(),
                new Token.NumberLiteral(10),
                new Token.BangEqual(),
                new Token.NumberLiteral(5.2),
                new Token.Semicolon(),

                new Token.NumberLiteral(1),
                new Token.Minus(),
                new Token.NumberLiteral(2),
                new Token.Slash(),
                new Token.NumberLiteral(3),
                new Token.Star(),
                new Token.NumberLiteral(4),
                new Token.SlashEqual(),
                new Token.NumberLiteral(5),
                new Token.StarEqual(),
                new Token.NumberLiteral(6),
                new Token.Semicolon(),

                new Token.True(),
                new Token.And(),
                new Token.False(),
                new Token.Or(),
                new Token.Nil(),
                new Token.Semicolon(),

                new Token.Fun(),
                new Token.Identifier("myFun"),
                new Token.LeftParen(),
                new Token.Identifier("num1"),
                new Token.Comma(),
                new Token.Identifier("num2"),
                new Token.RightParen(),
                new Token.LeftBrace(),
                new Token.Var(),
                new Token.Identifier("result"),
                new Token.Equal(),
                new Token.NumberLiteral(0),
                new Token.Semicolon(),
                new Token.If(),
                new Token.LeftParen(),
                new Token.Identifier("num1"),
                new Token.Greater(),
                new Token.Identifier("num2"),
                new Token.RightParen(),
                new Token.LeftBrace(),
                new Token.Identifier("result"),
                new Token.Equal(),
                new Token.Identifier("num1"),
                new Token.Semicolon(),
                new Token.RightBrace(),
                new Token.Else(),
                new Token.LeftBrace(),
                new Token.Identifier("result"),
                new Token.Equal(),
                new Token.Identifier("num2"),
                new Token.Semicolon(),
                new Token.RightBrace(),
                new Token.Return(),
                new Token.Identifier("result"),
                new Token.RightBrace(),

                new Token.For(),
                new Token.LeftParen(),
                new Token.Var(),
                new Token.Identifier("i"),
                new Token.Equal(),
                new Token.NumberLiteral(0),
                new Token.Semicolon(),
                new Token.Identifier("i"),
                new Token.Less(),
                new Token.NumberLiteral(10),
                new Token.Semicolon(),
                new Token.Identifier("i"),
                new Token.PlusEqual(),
                new Token.NumberLiteral(1),
                new Token.RightParen(),
                new Token.LeftBrace(),
                new Token.Identifier("result"),
                new Token.MinusEqual(),
                new Token.Identifier("i"),
                new Token.Semicolon(),
                new Token.RightBrace(),

                new Token.While(),
                new Token.LeftParen(),
                new Token.Bang(),
                new Token.True(),
                new Token.RightParen(),
                new Token.LeftBrace(),
                new Token.Print(),
                new Token.StringLiteral("This will never print"),
                new Token.Semicolon(),
                new Token.RightBrace(),

                new Token.Class(),
                new Token.Identifier("Person"),
                new Token.LeftBrace(),
                new Token.Identifier("init"),
                new Token.LeftParen(),
                new Token.Identifier("name"),
                new Token.RightParen(),
                new Token.LeftBrace(),
                new Token.This(),
                new Token.Dot(),
                new Token.Identifier("name"),
                new Token.Equal(),
                new Token.Identifier("name"),
                new Token.Semicolon(),
                new Token.RightBrace(),
                new Token.RightBrace(),

                new Token.Class(),
                new Token.Identifier("Employee"),
                new Token.Less(),
                new Token.Identifier("Person"),
                new Token.LeftBrace(),
                new Token.Identifier("init"),
                new Token.LeftParen(),
                new Token.Identifier("name"),
                new Token.Comma(),
                new Token.Identifier("title"),
                new Token.RightParen(),
                new Token.LeftBrace(),
                new Token.Super(),
                new Token.Dot(),
                new Token.Identifier("init"),
                new Token.LeftParen(),
                new Token.Identifier("name"),
                new Token.RightParen(),
                new Token.Semicolon(),
                new Token.This(),
                new Token.Dot(),
                new Token.Identifier("title"),
                new Token.Equal(),
                new Token.Identifier("title"),
                new Token.Semicolon(),
                new Token.RightBrace(),
                new Token.RightBrace());
    }

    @Test
    void shouldReturnInvalidTokensWhenSourceContainsInvalidSyntax() {
        // Given
        var source = """
                10_
                112.
                @#^
                "Unterminated string""";
        var scanner = new Scanner(source);

        // When
        var tokens = scanTokens(scanner);

        // Then
        assertThat(tokens).containsExactly(
                new Token.Illegal(),
                new Token.Illegal(),
                new Token.Illegal(),
                new Token.Illegal(),
                new Token.Illegal(),
                new Token.Illegal());
    }

    private List<Token> scanTokens(Scanner scanner) {
        var tokens = new ArrayList<Token>();
        scanner.forEachRemaining(tokens::add);
        return tokens;
    }
}