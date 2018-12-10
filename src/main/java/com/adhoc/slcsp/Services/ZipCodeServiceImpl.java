package com.adhoc.slcsp.Services;

import com.adhoc.slcsp.Models.ZipCodeRateArea;
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
public class ZipCodeServiceImpl implements ZipCodeService {

    Logger log = LoggerFactory.getLogger(ZipCodeServiceImpl.class);

    @Autowired
    private ZipCodeRepository zipRepo;

    public List<ZipCodeRateArea> parseCsv(String path) throws IOException {
        Reader in = new FileReader(path);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        // appending at the end is O(1), just not for random access
        List<ZipCodeRateArea> parsed = new ArrayList<>();
        for (CSVRecord record : records) {
            ZipCodeRateArea zipCodeRateArea = ZipCodeRateArea.builder()
                    .zipCode(record.get("zipcode"))
                    .state(record.get("state"))
                    .countyCode(record.get("county_code"))
                    .name(record.get("name"))
                    .rateArea(Integer.parseInt(record.get("rate_area")))
                    .build();
            if (checkNotEmpty(zipCodeRateArea)) {
                log.warn("The following record read in to the parser has an empty column: "
                                 + zipCodeRateArea);
            }
            parsed.add(zipCodeRateArea);
        }

        return parsed;
    }

    private Boolean checkNotEmpty(ZipCodeRateArea zipCodeRateArea) {
        return zipCodeRateArea.getZipCode().isEmpty() ||
                zipCodeRateArea.getCountyCode().isEmpty() ||
                zipCodeRateArea.getName().isEmpty() ||
                zipCodeRateArea.getRateArea() == null ||
                zipCodeRateArea.getState().isEmpty();
    }

    public void saveCsv(String path) {
        try {
            List<ZipCodeRateArea> parsed = parseCsv(path);
            parsed.stream().forEach(zip -> zipRepo.save(zip));
        } catch (IOException e) {
            log.error("CSV was not parsed correctly, did you put in the correct path?\n" + e.getMessage());
        }
    }

    public String getRateAreaByZipCode(String zipCode) {


        return null;
    }

}
