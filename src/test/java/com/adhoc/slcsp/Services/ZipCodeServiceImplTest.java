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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ZipCodeServiceImplTest {

    @TestConfiguration
    static class ZipCodeServiceImplTestContextConfiguration {
        @Bean
        public ZipCodeService zipCodeService() {
            return new ZipCodeServiceImpl();
        }
    }

    @Autowired
    private ZipCodeService zipCodeService;

    @MockBean
    private PlanRepository planRepository;

    @MockBean
    private ZipCodeRepository zipCodeRepository;

    @Test
    public void whenGivenPathShouldParseCsv() throws IOException {
        String path = getClass().getClassLoader().getResource("zips.csv").getPath();

        List<ZipCodeRateArea> rateAreas = zipCodeService.parseCsv(path);

        assertThat(rateAreas).isNotEmpty();
        assertThat(rateAreas).noneMatch(zipCodeRateArea ->
            zipCodeRateArea.getZipCode().isEmpty() ||
            zipCodeRateArea.getCountyCode().isEmpty() ||
            zipCodeRateArea.getName().isEmpty() ||
            zipCodeRateArea.getRateArea() == null ||
            zipCodeRateArea.getState().isEmpty()
        );
    }

    @Test
    public void whenGivenCorrectCsvShouldSave() {

    }

}
