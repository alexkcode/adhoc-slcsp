package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.Plan;
import com.adhoc.slcsp.Models.ZipCodeRateArea;
import com.adhoc.slcsp.Repositories.PlanRepository;
import com.adhoc.slcsp.Repositories.ZipCodeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.adhoc.slcsp.Services.PlanServiceImpl.SILVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PlanServiceImplTest {

    @TestConfiguration
    static class PlanServiceImplTestContextConfiguration {
        @Bean
        public PlanService planService() {
            return new PlanServiceImpl();
        }
    }

    @Autowired
    private PlanService planService;

    @MockBean
    private PlanRepository planRepository;

    @MockBean
    private ZipCodeService zipCodeService;

    @MockBean
    private ZipCodeRepository zipCodeRepository;

    @Test
    public void whenGivenPathShouldParseCsv() throws IOException {
        String path = getClass().getClassLoader().getResource("plans.csv").getPath();

        List<Plan> plans = planService.parseCsv(path);

        assertThat(plans).isNotEmpty();
        assertThat(plans).noneMatch(plan -> plan.getPlanId().isEmpty() ||
                plan.getMetalLevel().isEmpty() ||
                plan.getRate() == null ||
                plan.getRateArea() == null ||
                plan.getState().isEmpty()
        );
    }

    @Test
    public void whenGivenZipCodeAndMetalWithTwoRateAreasShouldReturnEmpty() {
        String zip = "36749";
        ZipCodeRateArea zipCodeRateAreaOne = new ZipCodeRateArea();
        zipCodeRateAreaOne.setRateArea(11);
        ZipCodeRateArea zipCodeRateAreaTwo = new ZipCodeRateArea();
        zipCodeRateAreaTwo.setRateArea(13);
        List<ZipCodeRateArea> fakeReturn = Arrays.asList(zipCodeRateAreaOne, zipCodeRateAreaTwo);
        when(zipCodeRepository.findAllRateAreasByZipCode(zip)).thenReturn(fakeReturn);

        List<Plan> found = planService.getPlansByZipCodeAndMetalLevel(zip, "Silver");

        assertThat(found).isNotNull().isEmpty();
    }

    @Test
    public void whenGivenZipCodeAndMetalWithEmptyRateAreasShouldReturnEmpty() {
        String zip = "36749";
        ZipCodeRateArea zipCodeRateArea = new ZipCodeRateArea();
        zipCodeRateArea.setRateArea(null);
        List<ZipCodeRateArea> fakeReturn = Arrays.asList(zipCodeRateArea);
        when(zipCodeRepository.findAllRateAreasByZipCode(zip)).thenReturn(fakeReturn);

        List<Plan> found = planService.getPlansByZipCodeAndMetalLevel(zip, "Silver");

        assertThat(found).isNotNull().isEqualTo(Collections.EMPTY_LIST);
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
        fakePlan.setRate(100.1);
        when(planRepository.findByRateAreaAndMetalLevelOrderByRateAsc(11, "Silver"))
                .thenReturn(Arrays.asList(fakePlan));

        String zip = "36749";
        List<Plan> found = planService.getPlansByZipCodeAndMetalLevel(zip, "Silver");

        assertThat(found).isNotNull().allMatch(plan -> plan.getRate() == 100.1);
    }

    @Test
    public void whenGivenZipCodeReturnSecondLowestRate() throws IOException {
        String zipCode = "01001";
        Plan planOne = new Plan();
        planOne.setRate(100.1);
        Plan planTwo = new Plan();
        planTwo.setRate(200.2);
        when(zipCodeService.getRateAreaByZipCode(zipCode)).thenReturn(Optional.of(35));
        when(planRepository.findByRateAreaAndMetalLevelOrderByRateAsc(35,
                                                                      SILVER)).thenReturn(Arrays.asList(
                planOne,
                planTwo));

        Optional<Double> secondLowestSilverRate = planService.getSecondLowestSilverRate(zipCode);

        assertThat(secondLowestSilverRate.get()).isEqualTo(200.2);
    }

    @Test
    public void whenGivenZipCodeWithNoRateReturnEmpty() {

    }

}
