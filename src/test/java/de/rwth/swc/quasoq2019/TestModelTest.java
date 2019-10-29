package de.rwth.swc.quasoq2019;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestModelTest {

    @Test
    void testNullValidValues() {
        assertThrows(NullPointerException.class,
                () -> new TestModel(null, new int[0]));
    }

    @Test
    void testNullInvalidValues() {
        assertThrows(NullPointerException.class,
                () -> new TestModel(new int[] {1}, null));
    }

    @Test
    void testNoValidValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestModel(new int[0], new int[0]));
    }

    @Test
    void testNoInvalidValues() {
        assertDoesNotThrow(
                () -> new TestModel(new int[] {1}, new int[] {0}));
    }

    @Test
    void testValidAndInvalidValues() {
        assertDoesNotThrow(
                () -> new TestModel(new int[] {1}, new int[] {1}));
    }

    @Test
    void testNoZeroValidValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestModel(new int[] {2, 2, 0}, new int[] {0, 0, 0}));
    }

    @Test
    void testTooFewValidValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestModel(new int[] {2, 2, 2}, new int[] {0, 0}));
    }

    @Test
    void testTooManyValidValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestModel(new int[] {2, 2, 2}, new int[] {0, 0, 0, 0}));
    }

    @Test
    void testGetNumberOfParameters() {
        final TestModel testModel = new TestModel(new int[] {2, 2, 2}, new int[] {0, 0, 0});

        assertEquals(3, testModel.getNumberOfParameters());
    }

    @Test
    void testGetValidValuesOfParameter() {
        final TestModel testModel = new TestModel(new int[] {2, 2, 2}, new int[] {1, 1, 1});

        assertArrayEquals(new int[] {0, 1}, testModel.getValidValuesOfParameter(1));
    }

    @Test
    void testGetInvalidValuesOfParameter() {
        final TestModel testModel = new TestModel(new int[] {2, 2, 2}, new int[] {2, 2, 2});

        assertArrayEquals(new String[] {"a", "b"}, testModel.getInvalidValuesOfParameter(1));
    }

    @Test
    void testGetInvalidValuesOfEmptyParameter() {
        final TestModel testModel = new TestModel(new int[] {2, 2, 2}, new int[] {0, 0, 0});

        assertArrayEquals(new String[0], testModel.getInvalidValuesOfParameter(1));
    }

    @Test
    void testGetValuesOfParameter() {
        final TestModel testModel = new TestModel(new int[] {2, 2, 2}, new int[] {1, 1, 1});

        assertArrayEquals(new Object[] {0, 1, "a"}, testModel.getValuesOfParameter(1));
    }
}
