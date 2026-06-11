package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.*;

public class AntlrTokenCollector extends SyntaxHighlighter {

    @Override
    public List<HighlightRegion> collectMatches(String text) {
        List<HighlightRegion> regions = new ArrayList<>();

        CharStream input = CharStreams.fromString(text);
        MiniJavaLexer lexer = new MiniJavaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        tokenStream.fill();

        List<Token> tokens = tokenStream.getTokens();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == Token.EOF) {
                continue;
            }

            Color colour = colourFor(token.getType());

            if (colour != null) {
                regions.add(
                    new HighlightRegion(
                        token.getStartIndex(),
                        token.getStopIndex() + 1,
                        colour));
            }

            if (token.getType() == MiniJavaLexer.AT && i + 1 < tokens.size()) {
                Token next = tokens.get(i + 1);

                if (next.getType() == MiniJavaLexer.IDENTIFIER) {
                    regions.add(
                        new HighlightRegion(
                            next.getStartIndex(),
                            next.getStopIndex() + 1,
                            MiniJavaColours.ANNOTATION_COLOUR));
                }
            }
        }

        return regions;
    }

    private Color colourFor(int tokenType) {
        return switch (tokenType) {
            case MiniJavaLexer.PACKAGE,
                 MiniJavaLexer.IMPORT,
                 MiniJavaLexer.CLASS,
                 MiniJavaLexer.PUBLIC,
                 MiniJavaLexer.PRIVATE,
                 MiniJavaLexer.FINAL,
                 MiniJavaLexer.RETURN,
                 MiniJavaLexer.NULL,
                 MiniJavaLexer.NEW,
                 MiniJavaLexer.IF,
                 MiniJavaLexer.ELSE,
                 MiniJavaLexer.WHILE,
                 MiniJavaLexer.EXTENDS,
                 MiniJavaLexer.IMPLEMENTS -> MiniJavaColours.KEYWORD_COLOUR;

            case MiniJavaLexer.STRING_LITERAL -> MiniJavaColours.STRING_LITERAL_COLOUR;

            case MiniJavaLexer.CHAR_LITERAL -> MiniJavaColours.CHAR_LITERAL_COLOUR;

            case MiniJavaLexer.LINE_COMMENT -> MiniJavaColours.LINE_COMMENT_COLOUR;

            case MiniJavaLexer.BLOCK_COMMENT -> MiniJavaColours.BLOCK_COMMENT_COLOUR;

            case MiniJavaLexer.JAVADOC_COMMENT -> MiniJavaColours.JAVADOC_COMMENT_COLOUR;

            case MiniJavaLexer.AT -> MiniJavaColours.ANNOTATION_COLOUR;

            default -> null;
        };
    }
}
