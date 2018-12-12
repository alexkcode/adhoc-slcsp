package com.adhoc.slcsp;

import com.adhoc.slcsp.Services.PlanService;
import com.adhoc.slcsp.Services.PlanServiceImpl;
import com.adhoc.slcsp.Services.ZipCodeService;
import com.adhoc.slcsp.Services.ZipCodeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PlanService planService() { return new PlanServiceImpl(); }

    @Bean
    public ZipCodeService zipCodeService() {
        return new ZipCodeServiceImpl();
    }

}
