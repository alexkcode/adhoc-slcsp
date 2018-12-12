package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.ZipCodeRateArea;

import java.io.IOException;
import java.util.List;

public interface ZipCodeService {
    List<ZipCodeRateArea> parseCsv(String path) throws IOException;

    void saveCsv(String path);

    Integer getRateAreaByZipCode(String zipCode);
}
