package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.Plan;

import java.io.IOException;
import java.util.List;

public interface PlanService {
    List<Plan> parseCsv(String path) throws IOException;

    void saveCsv(String path);

    String getRateByZipCode(String zipCode);
}
