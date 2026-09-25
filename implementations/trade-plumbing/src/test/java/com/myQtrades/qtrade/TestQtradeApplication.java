package com.myQtrades.qtrade;

import org.springframework.boot.SpringApplication;

public class TestQtradeApplication {

	public static void main(String[] args) {
		SpringApplication.from(QtradeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
