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
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

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
            Plan plan = Plan.builder()
                    .planId(record.get("plan_id"))
                    .state(record.get("state"))
                    .metalLevel(record.get("metal_level"))
                    .rate(record.get("rate"))
                    .rateArea(Integer.parseInt(record.get("rate_area")))
                    .build();
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
                plan.getRate().isEmpty() ||
                plan.getRateArea() == null ||
                plan.getState().isEmpty();
    }

    @Override
    public void saveCsv(String path) {

    }

    public String getRateByZipCode(String zipCode) {
        List<String> rateAreas = zipCodeRepository.findAllRateAreasByZipCode(zipCode);

        if (rateAreas.size() == 1) {
            if (!rateAreas.get(0).isEmpty())
                return planRepository.findPlanByRateArea(rateAreas.get(0)).getRate();
        }

        return "";
    }

}
