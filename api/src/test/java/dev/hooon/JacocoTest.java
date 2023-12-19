package dev.hooon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
