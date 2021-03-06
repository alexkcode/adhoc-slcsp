package com.adhoc.slcsp.Models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class ZipCodeRateArea implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String zipCode;

    private String state;

    @Column(name = "county_code")
    private String countyCode;

    private String name;

    @Column(name = "rate_area")
    private Integer rateArea;

    public ZipCodeRateArea() {}

}
