package com.et.repository;

import com.et.domain.CancellationManagement;
import com.et.domain.OnboardingInfo;

import com.et.service.dto.OnboardingInfoDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the OnboardingInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OnboardingInfoRepository extends JpaRepository<OnboardingInfo, Long> {

    Optional<OnboardingInfo> findByShop(String shop);

    @Modifying
    @Query("delete from OnboardingInfo onboarding where onboarding.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
