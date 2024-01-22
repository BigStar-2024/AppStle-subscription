package com.et.repository;

import com.apollographql.apollo.api.internal.ResponseFieldMapper;
import com.et.domain.CancellationManagement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the CancellationManagement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CancellationManagementRepository extends JpaRepository<CancellationManagement, Long> {
    Optional<CancellationManagement> findByShop(String shop);

    @Modifying
    @Query("delete from CancellationManagement cm where cm.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
