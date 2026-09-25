package com.myQtrades.qtrade;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class QtradeApplicationTests {

	@Test
	void contextLoads() {
	}

}
