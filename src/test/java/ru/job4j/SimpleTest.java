package ru.job4j;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleTest {
    @Test
    public void test() {
        assertThat(Simple.sum(1, 2), is(3));
    }
}