package highlighting.antlr;

import java.util.Scanner;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class PrettyPrinterDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Einrueckung in Leerzeichen: ");
        int indentWidth = Integer.parseInt(scanner.nextLine());

        String[] examples = {
            """
      public class Einfach{private String name;public void test(){return;}}
      """,

            """
      public class Kontrollfluss{private String text;public void test(){if(text!=null){return;}else{while(text!=null){text=null;}}}}
      """,

            """
      public class Verschachtelt{public void test(){{{return;}}}}
      """
        };

        for (String input : examples) {

            MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiniJavaParser parser = new MiniJavaParser(tokens);

            MiniJavaParser.CompilationUnitContext tree = parser.compilationUnit();

            PrettyPrinterVisitor visitor = new PrettyPrinterVisitor(indentWidth);
            visitor.visit(tree);

            System.out.println("===== Beispiel =====");
            System.out.println(visitor.result());
            System.out.println();
        }

        scanner.close();
    }
}
