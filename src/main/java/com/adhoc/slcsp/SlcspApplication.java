package com.adhoc.slcsp;

import com.adhoc.slcsp.Services.PlanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SlcspApplication implements CommandLineRunner {

    @Autowired
    PlanServiceImpl planServiceImpl;

	public static void main(String[] args) {
		SpringApplication.run(SlcspApplication.class, args);
	}

	@Override
	public void run(String ... args) {

    }

}
