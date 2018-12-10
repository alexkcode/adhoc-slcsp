package com.adhoc.slcsp.Models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Builder
@Entity
public class Plan implements Serializable {

    @Id
    @Column(name = "plan_id")
    private String planId;

    private String state;

    @Column(name = "metal_level")
    private String metalLevel;

    private String rate;

    // join with ZipCodeRateArea on rateArea
    @Column(name = "rate_area")
    private Integer rateArea;

}
