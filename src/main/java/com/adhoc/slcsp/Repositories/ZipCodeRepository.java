package com.adhoc.slcsp.Repositories;

import com.adhoc.slcsp.Models.ZipCodeRateArea;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZipCodeRepository extends CrudRepository<ZipCodeRateArea, Long> {

    public List<ZipCodeRateArea> findAllRateAreasByZipCode(String zipCode);

}
