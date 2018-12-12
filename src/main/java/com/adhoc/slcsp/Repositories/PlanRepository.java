package com.adhoc.slcsp.Repositories;

import com.adhoc.slcsp.Models.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {

    public Plan findPlanByRateArea(Integer rateArea);

}
