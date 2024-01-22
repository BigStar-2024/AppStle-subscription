package com.et.repository;

import com.et.domain.VariantInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VariantInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariantInfoRepository extends JpaRepository<VariantInfo, Long> {

}
