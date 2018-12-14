package com.adhoc.slcsp.Models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class Plan implements Serializable {

    @Id
    @Column(name = "plan_id")
    private String planId;

    private String state;

    @Column(name = "metal_level")
    private String metalLevel;

    private Double rate;

    @Column(name = "rate_area")
    private Integer rateArea;

    public Plan() {}
}
