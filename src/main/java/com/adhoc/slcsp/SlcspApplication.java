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
public class SlcspApplication implements ApplicationRunner {

    public static final String ZIPCODES_PATH = "zipcodes.path";
    public static final String PLANS_PATH = "plans.path";
    public static final String SLCSP_PATH = "slcsp.path";
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
        args.getOptionNames().stream().forEach(option -> {
            if (!args.containsOption(option)) {
                log.error(option + " is missing from the passed arguments!");
            } else if (args.getOptionValues(option).size() != 1){
                log.error("Too many values were passed to " + option + "!");
            }
        });
    }

    @Override
    public void run(ApplicationArguments args) {
        checkArgs(args);
        // no requirements were given for update logic so the DB is cleared every time the
        // application is run
        zipCodeRepository.deleteAll();
        planRepository.deleteAll();
        zipCodeService.saveCsv(args.getOptionValues(ZIPCODES_PATH).get(0));
        planService.saveCsv(args.getOptionValues(PLANS_PATH).get(0));

        planService.outputSecondLowestSilverRateCsv(args.getOptionValues(SLCSP_PATH).get(0));
    }

}
