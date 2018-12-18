package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.Plan;
import com.adhoc.slcsp.Repositories.PlanRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Alexis Kwan
 * @since 12-14-2018
 */
@Service
public class PlanServiceImpl implements PlanService {

    public static final String SILVER = "Silver";
    Logger log = LoggerFactory.getLogger(PlanServiceImpl.class);

    @Autowired
    PlanRepository planRepository;

    @Autowired
    ZipCodeService zipCodeService;

    public List<Plan> parseCsv(String path) throws IOException {
        Reader in = new FileReader(path);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .parse(in);

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

    public void saveCsv(String path) {
        try {
            List<Plan> parsed = parseCsv(path);
            parsed.stream().forEach(zip -> planRepository.save(zip));
        } catch (IOException e) {
            log.error("CSV was not parsed correctly, did you put in the correct path?\n" + e.getMessage());
        }
    }

    public List<Plan> getPlansByZipCodeAndMetalLevel(String zipCode, String metalLevel) {
        // potentially too much coupling
        Optional<Integer> rateAreaByZipCode = zipCodeService.getRateAreaByZipCode(zipCode);

        if (rateAreaByZipCode.isPresent())
            return planRepository.findByRateAreaAndMetalLevelOrderByRateAsc(rateAreaByZipCode.get(),
                                                                            metalLevel);

        return Collections.EMPTY_LIST;
    }

    public Optional<Double> getSecondLowestSilverRate(String zipCode) {
        List<Plan> plans = getPlansByZipCodeAndMetalLevel(zipCode, SILVER);
        if (plans.size() == 1) {
            return Optional.of(plans.get(0).getRate());
        } else if (plans.size() >= 2) {
            return Optional.of(plans.get(1).getRate());
        } else {
            return Optional.empty();
        }
    }

    public void writeSecondLowestSilverRateCsv(String path) {
        try {
            log.info("Reading file : " + path);
            Reader in = new FileReader(path);
            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
            List<CSVRecord> records = csvFormat.parse(in).getRecords();

            FileWriter fileWriter = new FileWriter(path);
            CSVPrinter csvPrinter = CSVFormat.DEFAULT.withHeader("zipcode","rate").print(fileWriter);

            log.info("Writing to file.");
            records.forEach(record -> {
                try {
                    String zipcode = record.get("zipcode");
                    Optional<Double> rate = getSecondLowestSilverRate(zipcode);
                    if (rate.isPresent())
                        csvPrinter.printRecord(zipcode, rate.get());
                    else
                        csvPrinter.printRecord(zipcode, "");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
            fileWriter.close();
            in.close();
            log.info("Finished writing to file.");
        } catch (FileNotFoundException e) {
            log.error("Please check your SLCSP file path.\n" + e.getMessage());
        } catch (IOException e) {
            log.error("Please check that the format of your SLCSP file is correct.\n" + e.getMessage());
        }

    }

}
