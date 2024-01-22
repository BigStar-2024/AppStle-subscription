package com.et.repository;

import com.apollographql.apollo.api.internal.ResponseFieldMapper;
import com.et.domain.SubscriptionCustomCss;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SubscriptionCustomCss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionCustomCssRepository extends JpaRepository<SubscriptionCustomCss, Long> {
    Optional<SubscriptionCustomCss> findByShop(String shop);

    @Modifying
    @Query("delete from SubscriptionCustomCss scc where scc.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    List<SubscriptionCustomCss> findAllByShop(String shop);
}
