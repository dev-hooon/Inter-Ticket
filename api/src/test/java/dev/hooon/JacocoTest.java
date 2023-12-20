package dev.hooon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JacocoTest {

    @DisplayName("jacoco test")
    @Test
    void test() {
        // given
        Jacoco jacoco = new Jacoco();

        // when

        // then
        assertEquals(1, jacoco.jacocoTest());
    }
}
