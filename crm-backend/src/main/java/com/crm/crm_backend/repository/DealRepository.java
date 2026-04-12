package com.crm.crm_backend.repository;

import com.crm.crm_backend.model.Deal;
import com.crm.crm_backend.model.DealStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByStage(DealStage stage);

    @Query("SELECT SUM(d.value) FROM Deal d WHERE d.stage = 'CLOSED_WON'")
    Double getTotalWonValue();

    @Query("SELECT COUNT(d) FROM Deal d WHERE d.stage = 'CLOSED_WON'")
    Long countWonDeals();

    @Query("SELECT COUNT(d) FROM Deal d WHERE d.stage = 'CLOSED_LOST'")
    Long countLostDeals();

    @Query("SELECT d.stage, COUNT(d) FROM Deal d GROUP BY d.stage")
    List<Object[]> countByStage();
}
