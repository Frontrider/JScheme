package hdm.pk070.jscheme;

import hdm.pk070.jscheme.error.SchemeError;
import hdm.pk070.jscheme.eval.SchemeEval;
import hdm.pk070.jscheme.obj.SchemeObject;
import hdm.pk070.jscheme.obj.function.builtin.SchemeBuiltinMinus;
import hdm.pk070.jscheme.obj.simple.SchemeSymbol;
import hdm.pk070.jscheme.obj.function.builtin.SchemeBuiltinPlus;
import hdm.pk070.jscheme.reader.SchemeReader;
import hdm.pk070.jscheme.table.environment.GlobalEnvironment;
import hdm.pk070.jscheme.table.environment.entry.EnvironmentEntry;
import hdm.pk070.jscheme.table.symbolTable.SchemeSymbolTable;

/**
 * JScheme entry point
 *
 * @author patrick.kleindienst
 */
public class JScheme {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";


    public static void main(String[] args) throws SchemeError {

        // TODO Move this to another class
        GlobalEnvironment.getInstance().add(EnvironmentEntry.create(SchemeSymbolTable.getInstance().add(new SchemeSymbol
                ("+")), SchemeBuiltinPlus.create()));
        GlobalEnvironment.getInstance().add(EnvironmentEntry.create(SchemeSymbolTable.getInstance().add(new SchemeSymbol
                ("-")), SchemeBuiltinMinus.create()));


        System.out.println("\n### Welcome to Scheme ###\n");
        SchemeReader schemeReader = SchemeReader.withStdin();
        for (; ; ) {
            System.out.print(">> ");
            try {
                SchemeObject readResult = schemeReader.read();
                SchemeObject evalResult = SchemeEval.getInstance().eval(readResult, GlobalEnvironment.getInstance());
                System.out.println("=> " + evalResult);
            } catch (SchemeError schemeError) {
                schemeReader.clearReaderOnError();
                System.out.println(ANSI_RED + "### ERROR: " + schemeError.getMessage() + ANSI_RESET);
            }
        }
    }

}
