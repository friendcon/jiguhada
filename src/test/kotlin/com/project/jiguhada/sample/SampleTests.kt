package com.project.jiguhada.sample

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SampleTests {
    @Test
    fun test1() {
        Assertions.assertThat(10).isEqualTo(10);
    }
}