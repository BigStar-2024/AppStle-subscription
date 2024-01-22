package com.et.repository;

import com.et.domain.DunningManagement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the DunningManagement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DunningManagementRepository extends JpaRepository<DunningManagement, Long> {

    Optional<DunningManagement> findAllByShop(String shop);

    Optional<DunningManagement> findByShop(String shop);

    @Modifying
    @Query("delete from DunningManagement dm where dm.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
