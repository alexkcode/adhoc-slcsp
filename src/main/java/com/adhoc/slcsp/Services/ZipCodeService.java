package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.ZipCodeRateArea;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ZipCodeService {
    List<ZipCodeRateArea> parseCsv(String path) throws IOException;

    void saveCsv(String path);

    Optional<Integer> getRateAreaByZipCode(String zipCode);
}
