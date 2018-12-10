package com.adhoc.slcsp.Models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
public class ZipCodeRateArea implements Serializable {

    private String zipCode;

    private String state;

    @Column(name = "county_code")
    private String countyCode;

    private String name;

    @Column(name = "rate_area")
    private Integer rateArea;

}
