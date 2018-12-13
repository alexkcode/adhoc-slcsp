package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.Plan;
import com.adhoc.slcsp.Models.ZipCodeRateArea;
import com.adhoc.slcsp.Repositories.PlanRepository;
import com.adhoc.slcsp.Repositories.ZipCodeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    public static final String SILVER = "Silver";
    Logger log = LoggerFactory.getLogger(PlanServiceImpl.class);

    @Autowired
    PlanRepository planRepository;

    @Autowired
    ZipCodeRepository zipCodeRepository;

    public List<Plan> parseCsv(String path) throws IOException {
        Reader in = new FileReader(path);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

        List<Plan> parsed = new ArrayList<>();
        for (CSVRecord record : records) {
            Plan plan = new Plan();
            plan.setPlanId(record.get("plan_id"));
            plan.setState(record.get("state"));
            plan.setMetalLevel(record.get("metal_level"));
            plan.setRate(Double.parseDouble(record.get("rate")));
            plan.setRateArea(Integer.parseInt(record.get("rate_area")));
            if (checkNotEmpty(plan)) {
                log.warn("The following record read in to the parser has an empty column: "
                                 + plan);
            }
            parsed.add(plan);
        }

        return parsed;
    }

    private Boolean checkNotEmpty(Plan plan) {
        return plan.getPlanId().isEmpty() ||
                plan.getMetalLevel().isEmpty() ||
                plan.getRate() == null ||
                plan.getRateArea() == null ||
                plan.getState().isEmpty();
    }

    @Override
    public void saveCsv(String path) {
        try {
            List<Plan> parsed = parseCsv(path);
            parsed.stream().forEach(zip -> planRepository.save(zip));
        } catch (IOException e) {
            log.error("CSV was not parsed correctly, did you put in the correct path?\n" + e.getMessage());
        }
    }

    public List<Plan> getPlansByZipCodeAndMetalLevel(String zipCode, String metalLevel) {
        // potentially too much coupling?
        List<ZipCodeRateArea> rateAreas = zipCodeRepository.findAllRateAreasByZipCode(zipCode);

        if (rateAreas.size() == 1) {
            if (rateAreas.get(0).getRateArea() != null) {
                return planRepository.findByRateAreaAndMetalLevelOrderByRateAsc(
                        rateAreas.get(0).getRateArea(), metalLevel);
            }
        }

        return Collections.EMPTY_LIST;
    }

    public Double getSecondLowestSilverRate(String zipCode) {
        List<Plan> plans = getPlansByZipCodeAndMetalLevel(zipCode, SILVER);
        return plans.get(1).getRate();
    }

}
