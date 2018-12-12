package com.adhoc.slcsp;

import com.adhoc.slcsp.Repositories.PlanRepository;
import com.adhoc.slcsp.Repositories.ZipCodeRepository;
import com.adhoc.slcsp.Services.PlanService;
import com.adhoc.slcsp.Services.ZipCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses= {ZipCodeRepository.class})
public class SlcspApplication implements CommandLineRunner {

    @Autowired
    PlanService planService;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    ZipCodeService zipCodeService;

    @Autowired
    ZipCodeRepository zipCodeRepository;

	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SlcspApplication.class, args);
        ctx.close();
	}

	@Override
	public void run(String ... args) {
	    // no requirements were given for update logic so the DB is cleared every time the
        // application is run
        zipCodeRepository.deleteAll();
        planRepository.deleteAll();

//        String path = getClass().getClassLoader().getResource("zips.csv").getPath();
//        zipCodeService.saveCsv(path);
        zipCodeService.saveCsv(args[0]);
        planService.saveCsv(args[1]);

        System.out.println("Rate Area " + zipCodeService.getRateAreaByZipCode("36068"));
    }

}
