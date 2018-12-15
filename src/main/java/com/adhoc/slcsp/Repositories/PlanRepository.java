package com.adhoc.slcsp.Repositories;

import com.adhoc.slcsp.Models.Plan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {

    public Optional<Double> findByPlanId(String planId);

    public List<Plan> findAllPlansByRateArea(Integer rateArea);

    public List<Plan> findByRateAreaAndMetalLevelOrderByRateAsc(Integer rateArea, String metalLevel);

}
