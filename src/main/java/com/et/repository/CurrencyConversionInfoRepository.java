package com.et.repository;

import com.et.domain.CurrencyConversionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the CurrencyConversionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyConversionInfoRepository extends JpaRepository<CurrencyConversionInfo, Long>, JpaSpecificationExecutor<CurrencyConversionInfo> {

    Optional<CurrencyConversionInfo> findCurrencyConversionInfoByFrom(String from);

}
