package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.Plan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PlanService {
    List<Plan> parseCsv(String path) throws IOException;

    void saveCsv(String path);

    List<Plan> getPlansByZipCodeAndMetalLevel(String zipCode, String metalLevel);

    Optional<Double> getSecondLowestSilverRate(String zipCode);

    void writeSecondLowestSilverRateCsv(String path);

    void outputSecondLowestSilverRateCsv(String path);
}
