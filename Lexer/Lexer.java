
import java.util.ArrayList;
import java.util.List;

enum TokenType {
    TK_TYPE, TK_REAL, TK_BOOLEAN, TK_INT, TK_FLOAT, TK_CHAR, TK_ARRAY, TK_ROUTINE, TK_RECORD, TK_TRUE, TK_FALSE, TK_PROCEDURE,
    TK_VAR, TK_IS, TK_IF, TK_THEN, TK_ELSE, TK_END,
    TK_PRINT, TK_COMMA, TK_SEMICOLON, TK_COLON, TK_WHILE, TK_FOR, TK_LOOP, TK_IN,
    TK_LPAREN, TK_RPAREN, TK_LBRACKET, TK_RBRACKET, TK_PLUS, TK_MINUS,
    TK_EOF, TK_ERROR, TK_BREAK, TK_REVERSE, TK_LCBRACKET, TK_RCBRACKET, TK_NOT,
    TK_MULTIPLY, TK_DIVISION, TK_MOD, TK_GREATERTHAN, TK_LESSTHAN, TK_EQUAL, TK_NOTEQUAL, TK_APOSTRPHE,
    TK_AND, TK_OR, TK_XOR, TK_NEWLINE,
    TK_RETURN, TK_SPACE, TK_VARNAME
}

class Token {

    TokenType type;
    String value;
    int line;
    int column;

    Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\''
                + ", line=" + line + ", column=" + column + '}';
    }
}

class Lexer {

    private final String input;
    private int pos = 0;
    private int line = 1;
    private int column = 1;
    private char currentChar;

    public Lexer(String input) {
        this.input = input;
        this.currentChar = input.charAt(pos);
    }

    private void advance() {
        pos++;
        column++;
        if (pos >= input.length()) {
            currentChar = '\0';
        } else {
            currentChar = input.charAt(pos);
        }
    }

    private void skipWhitespace() {
        while (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
            if (currentChar == '\n') {
                line++;
                column = 1;
            }
            advance();
        }
    }

    private Token charLiteral() {
        StringBuilder result = new StringBuilder();
        int startCol = column;
        advance();
        while (currentChar != '\'' && currentChar != '\0') {
            result.append(currentChar);
            advance();
        }
        advance();
        return new Token(TokenType.TK_CHAR, result.toString(), line, startCol);
    }

    private Token arrayLiteral() {
        List<String> elements = new ArrayList<>();
        int startCol = column;
        advance();
        while (currentChar != ']' && currentChar != '\0') {
            if (Character.isLetter(currentChar)) {
                elements.add(String.valueOf(currentChar));
            }
            advance();
        }
        advance();
        return new Token(TokenType.TK_ARRAY, elements.toString(), line, startCol);
    }

    private Token identifier() {
        StringBuilder result = new StringBuilder();
        int startCol = column;
        while (Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }

        String value = result.toString();
        TokenType type;
        switch (value) {
            case "routine":
                type = TokenType.TK_ROUTINE;
                break;
            case "var":
                type = TokenType.TK_VAR;
                break;
            case "is":
                type = TokenType.TK_IS;
                break;
            case "if":
                type = TokenType.TK_IF;
                break;
            case "then":
                type = TokenType.TK_THEN;
                break;
            case "else":
                type = TokenType.TK_ELSE;
                break;
            case "end":
                type = TokenType.TK_END;
                break;
            case "print":
                type = TokenType.TK_PRINT;
                break;
            case "true":
                type = TokenType.TK_TRUE;
                break;
            case "false":
                type = TokenType.TK_FALSE;
                break;
            case "break":
                type = TokenType.TK_BREAK;
                break;
            case "reverse":
                type = TokenType.TK_REVERSE;
                break;
            case "return":
                type = TokenType.TK_RETURN;
                break;
            default:
                type = TokenType.TK_VARNAME;
                break;
        }
        return new Token(type, value, line, startCol);
    }

    public Token nextToken() {
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    line++;
                    column = 1;
                } else if (currentChar == '\t') {
                    column += 4;
                } else {
                    column++;
                }
                advance();
                continue;
            }
            if (currentChar == '\'') {
                return charLiteral();
            }
            if (currentChar == '[') {
                return arrayLiteral();
            }
            if (Character.isLetter(currentChar)) {
                return identifier();
            }

            int startCol = column;
            switch (currentChar) {
                case '+':
                    advance();
                    return new Token(TokenType.TK_PLUS, "+", line, startCol);
                case '-':
                    advance();
                    return new Token(TokenType.TK_MINUS, "-", line, startCol);
                case '*':
                    advance();
                    return new Token(TokenType.TK_MULTIPLY, "*", line, startCol);
                case '/':
                    advance();
                    return new Token(TokenType.TK_DIVISION, "/", line, startCol);
                case '%':
                    advance();
                    return new Token(TokenType.TK_MOD, "%", line, startCol);
                case '>':
                    advance();
                    return new Token(TokenType.TK_GREATERTHAN, ">", line, startCol);
                case '<':
                    advance();
                    return new Token(TokenType.TK_LESSTHAN, "<", line, startCol);
                case '=':
                    advance();
                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.TK_EQUAL, "==", line, startCol);
                    }
                    return new Token(TokenType.TK_EQUAL, "=", line, startCol);
                case '!':
                    advance();
                    if (currentChar == '=') {
                        advance();
                        return new Token(TokenType.TK_NOTEQUAL, "!=", line, startCol);
                    }
                    return new Token(TokenType.TK_NOT, "!", line, startCol);
                case '&':
                    advance();
                    return new Token(TokenType.TK_AND, "&", line, startCol);
                case '|':
                    advance();
                    return new Token(TokenType.TK_OR, "|", line, startCol);
                case '^':
                    advance();
                    return new Token(TokenType.TK_XOR, "^", line, startCol);
                case '(':
                    advance();
                    return new Token(TokenType.TK_LPAREN, "(", line, startCol);
                case ')':
                    advance();
                    return new Token(TokenType.TK_RPAREN, ")", line, startCol);
                case '[':
                    advance();
                    return new Token(TokenType.TK_LBRACKET, "[", line, startCol);
                case ']':
                    advance();
                    return new Token(TokenType.TK_RBRACKET, "]", line, startCol);
                case '{':
                    advance();
                    return new Token(TokenType.TK_LCBRACKET, "{", line, startCol);
                case '}':
                    advance();
                    return new Token(TokenType.TK_RCBRACKET, "}", line, startCol);
                case ',':
                    advance();
                    return new Token(TokenType.TK_COMMA, ",", line, startCol);
                case ';':
                    advance();
                    return new Token(TokenType.TK_SEMICOLON, ";", line, startCol);
                case ':':
                    advance();
                    return new Token(TokenType.TK_COLON, ":", line, startCol);
                default:
                    advance();
                    return new Token(TokenType.TK_ERROR, "Unrecognized character", line, startCol);
            }
        }
        return new Token(TokenType.TK_EOF, "", line, column);
    }
}
