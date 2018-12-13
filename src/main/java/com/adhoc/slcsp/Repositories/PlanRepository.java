package com.adhoc.slcsp.Repositories;

import com.adhoc.slcsp.Models.Plan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {

    public List<Plan> findAllPlansByRateArea(Integer rateArea);

    public List<Plan> findByRateAreaAndMetalLevelOrderByRateAsc(Integer rateArea, String metalLevel);

}
