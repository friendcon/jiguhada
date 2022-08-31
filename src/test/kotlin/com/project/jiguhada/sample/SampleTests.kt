package com.project.jiguhada.sample

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class SampleTests {
    @Test
    fun test1() {
        Assertions.assertThat(10).isEqualTo(10);
    }
}