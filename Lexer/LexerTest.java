
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LexerTest {

    public static void main(String[] args) {
        String filePath = "./sampleProgram.txt";
        String program = "";
        try {
            program = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        Lexer lexer = new Lexer(program);
        Token token = lexer.nextToken();
        while (token.type != TokenType.TK_EOF) {
            System.out.println(token);
            token = lexer.nextToken();
        }
        System.out.println(token);
    }
}
