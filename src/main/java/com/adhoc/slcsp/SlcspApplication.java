package com.adhoc.slcsp;

import com.adhoc.slcsp.Repositories.PlanRepository;
import com.adhoc.slcsp.Repositories.ZipCodeRepository;
import com.adhoc.slcsp.Services.PlanService;
import com.adhoc.slcsp.Services.ZipCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses= {ZipCodeRepository.class})
public class SlcspApplication implements ApplicationRunner {

    public static final String ZIPCODES_PATH = "zipcodes.path";
    public static final String PLANS_PATH = "plans.path";
    Logger log = LoggerFactory.getLogger(SlcspApplication.class);

    @Autowired
    PlanService planService;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    ZipCodeService zipCodeService;

    @Autowired
    ZipCodeRepository zipCodeRepository;

    public static void main(String... args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SlcspApplication.class, args);
        // ensures application is not continually running
        ctx.close();
    }

    private void checkArgs(ApplicationArguments args) {
        if (!args.containsOption(ZIPCODES_PATH)) {
            log.error(ZIPCODES_PATH + " is missing from the passed arguments!");
        } else if (args.getOptionValues(ZIPCODES_PATH).size() != 1){
            log.error("Too many values were passed to " + ZIPCODES_PATH + "!");
        }

        if (!args.containsOption(PLANS_PATH)) {
            log.error(PLANS_PATH + " is missing from the passed arguments!");
        } else if ((args.getOptionValues(PLANS_PATH).size() != 1)) {
            log.error("Too many values were passed to " + PLANS_PATH + "!");
        }
    }

    @Override
    public void run(ApplicationArguments args) {
//        checkArgs(args);
        // no requirements were given for update logic so the DB is cleared every time the
        // application is run
        zipCodeRepository.deleteAll();
        planRepository.deleteAll();

//        String zipPath = getClass().getClassLoader().getResource("zips.csv").getPath();
//        String planPath = getClass().getClassLoader().getResource("plans.csv").getPath();
//        zipCodeService.saveCsv(zipPath);
//        planService.saveCsv(planPath);
        zipCodeService.saveCsv(args.getOptionValues(ZIPCODES_PATH).get(0));
        planService.saveCsv(args.getOptionValues(PLANS_PATH).get(0));

        log.info("Sample Rate Area : " + zipCodeService.getRateAreaByZipCode("36068").get());
        log.info("Sample Plan : " + planRepository.findByPlanId("47011NC7752714").get());
    }

}
