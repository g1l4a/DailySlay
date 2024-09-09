
import java.util.ArrayList;
import java.util.List;

enum TokenType {
    TK_TYPE, TK_REAL, TK_BOOLEAN, TK_INT, TK_FLOAT, TK_CHAR, TK_ARRAY, TK_ROUTINE, TK_RECORD, TK_TRUE, TK_FALSE,
    TK_VAR, TK_IS, TK_IF, TK_THEN, TK_ELSE, TK_END,
    TK_PRINT, TK_COMMA, TK_SEMICOLON, TK_COLON, TK_WHILE, TK_FOR, TK_LOOP, TK_IN,
    TK_LPAREN, TK_RPAREN, TK_LBRACKET, TK_RBRACKET, TK_PLUS, TK_MINUS,
    TK_EOF, TK_ERROR, TK_BREAK, TK_REVERSE, TK_LCBRACKET, TK_RCBRACKET, TK_NOT,
    TK_MULTIPLY, TK_DIVISION, TK_MOD, TK_GREATERTHAN, TK_LESSTHAN, TK_EQUAL, TK_NOTEQUAL, TK_APOSTRPHE,
    TK_AND, TK_OR, TK_XOR, TK_NEWLINE,
    TK_RETURN
}

class Token {

    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\''
                + '}';
    }
}

class Lexer {

    private final String input;
    private int pos = 0;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.currentChar = input.charAt(pos);
    }

    private void advance() {
        pos++;
        if (pos >= input.length()) {
            currentChar = '\0'; // End of input
        } else {
            currentChar = input.charAt(pos);
        }
    }

    private void skipWhitespace() {
        while (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
            advance();
        }
    }

    private Token charLiteral() {
        StringBuilder result = new StringBuilder();
        advance(); // Skip opening quote
        while (currentChar != '\'' && currentChar != '\0') {
            result.append(currentChar);
            advance();
        }
        advance(); // Skip closing quote
        return new Token(TokenType.TK_CHAR, result.toString());
    }

    private Token arrayLiteral() {
        List<String> elements = new ArrayList<>();
        advance(); // Skip '['
        while (currentChar != ']' && currentChar != '\0') {
            if (Character.isLetter(currentChar)) {
                elements.add(String.valueOf(currentChar));
            }
            advance();
        }
        advance(); // Skip ']'
        return new Token(TokenType.TK_ARRAY, elements.toString());
    }

    private Token identifier() {
        StringBuilder result = new StringBuilder();
        while (Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        String value = result.toString();
        return switch (value) {
            case "routine" ->
                new Token(TokenType.TK_ROUTINE, value);
            case "var" ->
                new Token(TokenType.TK_VAR, value);
            case "is" ->
                new Token(TokenType.TK_IS, value);
            case "if" ->
                new Token(TokenType.TK_IF, value);
            case "then" ->
                new Token(TokenType.TK_THEN, value);
            case "else" ->
                new Token(TokenType.TK_ELSE, value);
            case "end" ->
                new Token(TokenType.TK_END, value);
            case "print" ->
                new Token(TokenType.TK_PRINT, value);
            case "true" ->
                new Token(TokenType.TK_TRUE, value);
            case "false" ->
                new Token(TokenType.TK_FALSE, value);
            case "break" ->
                new Token(TokenType.TK_BREAK, value);
            case "reverse" ->
                new Token(TokenType.TK_REVERSE, value);
            case "return" ->
                new Token(TokenType.TK_RETURN, value);
            default ->
                new Token(TokenType.TK_ERROR, value);
        };
    }

    public Token nextToken() {
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    advance();
                    return new Token(TokenType.TK_NEWLINE, "\n");
                }
                skipWhitespace();
                continue;
            }
            if (currentChar == '\'') {
                return charLiteral(); // Handle character literals
            }
            if (currentChar == '[') {
                return arrayLiteral(); // Handle arrays of characters
            }
            if (Character.isLetter(currentChar)) {
                return identifier(); // Handle keywords and identifiers
            }

            switch (currentChar) {
                case '+':
                    advance();
                    return new Token(TokenType.TK_PLUS, "+");
                case '-':
                    advance();
                    return new Token(TokenType.TK_MINUS, "-");
                case '*':
                    advance();
                    return new Token(TokenType.TK_MULTIPLY, "*");
                case '/':
                    advance();
                    return new Token(TokenType.TK_DIVISION, "/");
                case '%':
                    advance();
                    return new Token(TokenType.TK_MOD, "%");
                case '>':
                    advance();
                    return new Token(TokenType.TK_GREATERTHAN, ">");
                case '<':
                    advance();
                    return new Token(TokenType.TK_LESSTHAN, "<");
                case '=':
                    advance();
                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.TK_EQUAL, "==");
                    }
                    return new Token(TokenType.TK_EQUAL, "=");
                case '!':
                    advance();
                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.TK_NOTEQUAL, "!=");
                    }
                    return new Token(TokenType.TK_NOT, "!");
                case '&':
                    advance();
                    return new Token(TokenType.TK_AND, "&");
                case '|':
                    advance();
                    return new Token(TokenType.TK_OR, "|");
                case '^':
                    advance();
                    return new Token(TokenType.TK_XOR, "^");
                case '(':
                    advance();
                    return new Token(TokenType.TK_LPAREN, "(");
                case ')':
                    advance();
                    return new Token(TokenType.TK_RPAREN, ")");
                case '[':
                    advance();
                    return new Token(TokenType.TK_LBRACKET, "[");
                case ']':
                    advance();
                    return new Token(TokenType.TK_RBRACKET, "]");
                case '{':
                    advance();
                    return new Token(TokenType.TK_LCBRACKET, "{");
                case '}':
                    advance();
                    return new Token(TokenType.TK_RCBRACKET, "}");
                case ',':
                    advance();
                    return new Token(TokenType.TK_COMMA, ",");
                case ';':
                    advance();
                    return new Token(TokenType.TK_SEMICOLON, ";");
                case ':':
                    advance();
                    return new Token(TokenType.TK_COLON, ":");
                default:
                    advance();
                    return new Token(TokenType.TK_ERROR, "Unrecognized character");
            }
        }
        return new Token(TokenType.TK_EOF, "");
    }
}
