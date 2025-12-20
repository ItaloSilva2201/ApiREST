package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void mainMethod_mustBeCovered() {

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {

            String[] args = {};

            DemoApplication.main(args);

            mocked.verify(() -> SpringApplication.run(eq(DemoApplication.class), eq(args)));

        }
    }
}
