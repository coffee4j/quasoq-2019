package de.rwth.swc.quasoq2019;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemModelTest {

    @Test
    void testNullConfiguration() {
        assertThrows(NullPointerException.class,
                () -> new SystemModel((int[]) null));
    }

    @Test
    void testNullValues() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertThrows(NullPointerException.class, () -> systemModel.test((Object[]) null));
    }

    @Test
    void testNoNullValues() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertThrows(IllegalArgumentException.class, () -> systemModel.test(1, null, 3, 4));
    }

    @Test
    void testTooFewValues() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertThrows(IllegalArgumentException.class, () -> systemModel.test(1, 2, 3, 4));
    }

    @Test
    void testTooManyValues() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertThrows(IllegalArgumentException.class, () -> systemModel.test(1, 2, 3, 4, 5, 6));
    }

    @Test
    void testCorrectTestModelAndValidInput() {
        final SystemModel systemModel = new SystemModel(1, 0, 0, 0, 0);

        assertTrue(systemModel.test(1, 2, 3, 4, 5));
    }

    @Test
    void testCorrectTestModelAndInvalidInput() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 0, 1);

        assertTrue(systemModel.test("A", 2, 3, 4, 5));
    }

    @Test
    void testIncorrectTestModelAndValidInput() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertTrue(systemModel.test(1, 2, 3, 4, 5));
    }

    @Test
    void testIncorrectTestModelAndMaskingInvalidInput() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertTrue(systemModel.test("A", 2, 3, "B", 5));
    }

    @Test
    void testIncorrectTestModelAndNotMaskingInvalidInput() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertTrue(systemModel.test(1, 2, 3, 4, "A"));
    }

    @Test
    void testIncorrectTestModelAndFailingInvalidInput() {
        final SystemModel systemModel = new SystemModel(0, 0, 0, 1, 0);

        assertFalse(systemModel.test(1, 2, 3, "A", 5));
    }
}
