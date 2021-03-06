package hdm.pk070.jscheme.obj.builtin.function.math;

import hdm.pk070.jscheme.error.SchemeError;
import hdm.pk070.jscheme.obj.SchemeObject;
import hdm.pk070.jscheme.obj.builtin.function.SchemeBuiltinFunction;
import hdm.pk070.jscheme.obj.builtin.function.math.SchemeBuiltinPlus;
import hdm.pk070.jscheme.obj.builtin.simple.number.exact.SchemeInteger;
import hdm.pk070.jscheme.obj.builtin.simple.SchemeSymbol;
import hdm.pk070.jscheme.obj.builtin.simple.number.floatComplex.SchemeFloat;
import hdm.pk070.jscheme.stack.SchemeCallStack;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A test class for {@link SchemeBuiltinPlus}.
 *
 * @author patrick.kleindienst
 */

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(SchemeCallStack.class)
public class SchemeBuiltinPlusTest {

    private SchemeBuiltinFunction builtinFunction;

    private SchemeCallStack validStack;
    private SchemeCallStack invalidStack;
    private SchemeCallStack floatStack;
    private final int fixedArgCount = 2;

    @Before
    public void setUp() {
        this.builtinFunction = SchemeBuiltinPlus.create();

        validStack = mock(SchemeCallStack.class);
        Mockito.when(validStack.pop()).thenReturn(new SchemeInteger(2)).thenReturn(new SchemeInteger(3));

        invalidStack = mock(SchemeCallStack.class);
        Mockito.when(invalidStack.pop()).thenReturn(new SchemeInteger(42)).thenReturn(new SchemeSymbol("invalid"));

        floatStack = mock(SchemeCallStack.class);
        Mockito.when(floatStack.pop()).thenReturn(new SchemeInteger(1)).thenReturn(new SchemeFloat(2.0f)).thenReturn
                (new SchemeInteger(3));
    }


    @Test
    public void testCallBuiltinPlusWithValidStack() throws SchemeError {
        PowerMockito.mockStatic(SchemeCallStack.class);
        PowerMockito.when(SchemeCallStack.instance()).thenReturn(validStack);

        SchemeObject plusResult = builtinFunction.call(fixedArgCount);

        assertThat(plusResult, notNullValue());
        assertThat(plusResult.typeOf(SchemeInteger.class), equalTo(true));
        assertThat(plusResult.equals(new SchemeInteger(5)), equalTo(true));
    }

    @Test(expected = SchemeError.class)
    public void testCallBuiltinPlusWithInvalidStack() throws SchemeError {
        PowerMockito.mockStatic(SchemeCallStack.class);
        PowerMockito.when(SchemeCallStack.instance()).thenReturn(invalidStack);

        builtinFunction.call(fixedArgCount);
    }

    @Test
    public void testResultIsSchemeFloat() throws SchemeError {
        PowerMockito.mockStatic(SchemeCallStack.class);
        PowerMockito.when(SchemeCallStack.instance()).thenReturn(floatStack);
        SchemeObject result = builtinFunction.call(3);

        assertThat("Result must not be null!", result, notNullValue());
        assertThat("Result does not match expected type!", result.typeOf(SchemeFloat.class), equalTo(true));
        assertThat("Result does not match expected value!", result.getValue(), equalTo(6.0f));
    }
}
