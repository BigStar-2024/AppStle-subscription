package com.et.repository;

import com.et.domain.BundleRule;

import com.et.domain.enumeration.BundleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BundleRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BundleRuleRepository extends JpaRepository<BundleRule, Long> {

    Page<BundleRule> findAllByShopOrderBySequenceNo(String shop, Pageable pageable);

    List<BundleRule> findAllByShop(String shop);

    List<BundleRule> findAllByShopAndStatus(String shop, BundleStatus status);

    @Modifying
    @Query("UPDATE BundleRule SET sequenceNo = sequenceNo + 1 WHERE sequenceNo > :destinationIndex")
    void updateNextIndexesToMoveDown(@Param("destinationIndex") Integer destinationIndex);

    @Modifying
    @Query("UPDATE BundleRule SET sequenceNo = :destinationIndex + 1 WHERE id = :id")
    void updateIndexToMoveDown(@Param("id") Long id, @Param("destinationIndex") Integer destinationIndex);

    @Modifying
    @Query("UPDATE BundleRule SET sequenceNo = sequenceNo + 1 WHERE sequenceNo >= :destinationIndex")
    void updateNextIndexesToMoveUp(@Param("destinationIndex") Integer destinationIndex);

    @Modifying
    @Query("UPDATE BundleRule SET sequenceNo = :destinationIndex WHERE id = :id")
    void updateIndexToMoveUp(@Param("id") Long id, @Param("destinationIndex") Integer destinationIndex);

    @Modifying
    @Query("UPDATE BundleRule SET status = :status WHERE id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") BundleStatus status);

    @Query("SELECT coalesce(MAX(o.sequenceNo), 0) FROM BundleRule o")
    Integer getMaxIndex();

}
