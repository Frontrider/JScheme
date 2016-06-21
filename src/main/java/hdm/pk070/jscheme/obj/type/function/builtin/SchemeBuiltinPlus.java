package hdm.pk070.jscheme.obj.type.function.builtin;

import hdm.pk070.jscheme.error.SchemeError;
import hdm.pk070.jscheme.obj.SchemeObject;
import hdm.pk070.jscheme.obj.type.SchemeInteger;
import hdm.pk070.jscheme.stack.SchemeCallStack;

/**
 * Created by patrick on 19.06.16.
 */
public class SchemeBuiltinPlus extends SchemeBuiltinFunction {


    public static SchemeBuiltinPlus create() {
        return new SchemeBuiltinPlus("+");
    }

    private SchemeBuiltinPlus(String internalName) {
        super(internalName);
    }

    @Override
    public SchemeObject call(int argCount) throws SchemeError {
        int sum = 0;
        // pop argCount values from stack
        for (int i = 0; i < argCount; i++) {
            SchemeObject poppedArg = SchemeCallStack.instance().pop();
            // each arg must be of type SchemeInteger
            if (!poppedArg.typeOf(SchemeInteger.class)) {
                throw new SchemeError(String.format("(+): non-integer argument %s!", poppedArg.toString()));
            }
            sum += ((SchemeInteger) poppedArg).getValue();
        }
        return new SchemeInteger(sum);
    }

}
