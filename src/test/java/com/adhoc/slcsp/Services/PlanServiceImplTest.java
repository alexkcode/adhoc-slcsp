package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.Plan;
import com.adhoc.slcsp.Models.ZipCodeRateArea;
import com.adhoc.slcsp.Repositories.PlanRepository;
import com.adhoc.slcsp.Repositories.ZipCodeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PlanServiceImplTest {

    @TestConfiguration
    static class PlanServiceImplTestContextConfiguration {
        @Bean
        public PlanService employeeService() {
            return new PlanServiceImpl();
        }
    }

    @Autowired
    private PlanService planService;

    @MockBean
    private PlanRepository planRepository;

    @MockBean
    private ZipCodeRepository zipCodeRepository;

    @Test
    public void whenGivenPathShouldParseCsv() throws IOException {
        String path = getClass().getClassLoader().getResource("plans.csv").getPath();

        List<Plan> plans = planService.parseCsv(path);

        assertThat(plans).isNotEmpty();
        assertThat(plans).noneMatch(plan ->
                plan.getPlanId().isEmpty() ||
                plan.getMetalLevel().isEmpty() ||
                plan.getRate().isEmpty() ||
                plan.getRateArea() == null ||
                plan.getState().isEmpty()
        );
    }

    @Test
    public void whenGivenZipCodeWithTwoRateAreasShouldReturnEmpty() {
        ZipCodeRateArea zipCodeRateAreaOne = new ZipCodeRateArea();
        zipCodeRateAreaOne.setRateArea(11);
        ZipCodeRateArea zipCodeRateAreaTwo = new ZipCodeRateArea();
        zipCodeRateAreaTwo.setRateArea(13);
        List<ZipCodeRateArea> fakeReturn = Arrays.asList(zipCodeRateAreaOne, zipCodeRateAreaTwo);
        when(zipCodeRepository.findAllRateAreasByZipCode("36749")).thenReturn(fakeReturn);

        String zip = "36749";
        String found = planService.getRateByZipCode(zip);

        assertThat(found).isNotNull().isEqualTo("");
    }

    @Test
    public void whenGivenZipCodeWithEmptyRateAreasShouldReturnEmpty() {
        ZipCodeRateArea zipCodeRateArea = new ZipCodeRateArea();
        zipCodeRateArea.setRateArea(null);
        List<ZipCodeRateArea> fakeReturn = Arrays.asList(zipCodeRateArea);
        when(zipCodeRepository.findAllRateAreasByZipCode("36749")).thenReturn(fakeReturn);

        String zip = "36749";
        String found = planService.getRateByZipCode(zip);

        assertThat(found).isNotNull().isEqualTo("");
    }

    @Test
    public void whenGivenZipCodeWithOneRateAreaShouldReturnOneRate() {
        ZipCodeRateArea zipCodeRateArea = new ZipCodeRateArea();
        zipCodeRateArea.setRateArea(11);
        List<ZipCodeRateArea> fakeReturn = Arrays.asList(zipCodeRateArea);
        when(zipCodeRepository.findAllRateAreasByZipCode("36749")).thenReturn(fakeReturn);
//        Plan fakePlan = Plan.builder().rateArea(11).rate("100.1").build();
        Plan fakePlan = new Plan();
        fakePlan.setRateArea(11);
        fakePlan.setRate("100.1");
        when(planRepository.findPlanByRateArea(11)).thenReturn(fakePlan);

        String zip = "36749";
        String found = planService.getRateByZipCode(zip);

        assertThat(found).isNotNull().isEqualTo("100.1");
    }

}
