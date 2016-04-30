package hdm.pk070.jscheme.reader;

import hdm.pk070.jscheme.error.SchemeError;
import hdm.pk070.jscheme.obj.SchemeObject;
import hdm.pk070.jscheme.obj.type.*;
import hdm.pk070.jscheme.symbolTable.SchemeSymbolTable;
import hdm.pk070.jscheme.util.ReflectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author patrick.kleindienst
 */

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(SchemeSymbolTable.class)
public class SchemeReaderTest {

    private static final String METHOD_READ_SYMBOL = "readSymbol";

    private SchemeSymbolTable symbolTableMock;

    private SchemeReader schemeReader;


    @Before
    public void setUp() throws Exception {
        symbolTableMock = Mockito.mock(SchemeSymbolTable.class);
        Mockito.when(symbolTableMock.getOrAdd("abc")).thenReturn(new SchemeSymbol("abc"));

        PowerMockito.mockStatic(SchemeSymbolTable.class);
        PowerMockito.when(SchemeSymbolTable.getInstance()).thenReturn(symbolTableMock);
    }

    @Test
    public void testSymbolTableMockIsReady() {
        assertThat(SchemeSymbolTable.getInstance(), notNullValue());
        assertThat(SchemeSymbolTable.getInstance(), equalTo(symbolTableMock));
    }

    @Test
    public void testCreateSchemeReaderInstance() {
        schemeReader = SchemeReader.withStdin();
        assertThat(schemeReader, notNullValue());

        schemeReader = SchemeReader.withInputStream(System.in);
        assertThat(schemeReader, notNullValue());
    }

    @Test(expected = NullPointerException.class)
    public void testPassNullToCreationMethodThrowsException() {
        SchemeReader.withInputStream(null);
    }


    @Test
    public void testReadNumber() throws SchemeError {
        assertNumberInput("1234");
        assertNumberInput("   1234");
        assertNumberInput("    1234    ");
    }

    @Test
    public void testReadString() throws SchemeError {
        assertStringInput("\"This is just a test\"");
        assertStringInput("\n\"This is just a test\"");
        assertStringInput("\t\"This is just a test\"");
        assertStringInput("\r\"This is just a test\"");
        assertStringInput("       \"This is just a test\"      ");
    }


    @Test
    public void testReadSymbol() throws SchemeError {
        assertReadSymbol("nil   ", new SchemeNil());
        assertReadSymbol("   #t  ", new SchemeTrue());
        assertReadSymbol("       #f", new SchemeFalse());
        assertReadSymbol("abc", new SchemeSymbol("abc"));
    }


    private void assertReadSymbol(String input, SchemeSymbol expectedSymbol) throws SchemeError {
        Objects.requireNonNull(expectedSymbol);
        schemeReader = SchemeReader.withInputStream(new ByteArrayInputStream(input.getBytes()));
        SchemeObject schemeObject = schemeReader.read();

        assertThat("symbol is null!", schemeObject, notNullValue());
        assertThat("symbol is not of type SchemeSymbol!", SchemeSymbol.class.isAssignableFrom(schemeObject.getClass()),
                equalTo(true));
        assertThat(String.format("symbol is not of type %s!", expectedSymbol.getClass().getSimpleName()),
                schemeObject.typeOf(expectedSymbol.getClass()), equalTo(true));
        assertThat("symbol does not have expected value!", schemeObject, equalTo(expectedSymbol));
    }

    @After
    public void tearDown() {
        if (Objects.nonNull(schemeReader)) {
            schemeReader.shutdown();
        }
    }


    private void assertNumberInput(String numberInput) throws SchemeError {
        schemeReader = SchemeReader.withInputStream(new ByteArrayInputStream(numberInput.getBytes()));
        SchemeObject number = schemeReader.read();

        assertThat("number is null!", number, notNullValue());
        assertThat("number is not of type SchemeInteger!", number.typeOf(SchemeInteger.class),
                equalTo(true));
        assertThat("number does not have expected value!", number.getValue(), equalTo(Integer
                .valueOf(numberInput.trim())));
    }

    private void assertStringInput(String stringInput) throws SchemeError {
        schemeReader = SchemeReader.withInputStream(new ByteArrayInputStream(stringInput.getBytes()));
        SchemeObject schemeObject = schemeReader.read();

        assertThat(schemeObject, notNullValue());
        assertThat(schemeObject.typeOf(SchemeString.class), equalTo(true));
        assertThat(schemeObject.getValue(), equalTo(stringInput.trim()));
    }

}