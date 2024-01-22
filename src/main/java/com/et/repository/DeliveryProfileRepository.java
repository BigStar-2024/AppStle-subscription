package com.et.repository;

import com.et.domain.DeliveryProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the DeliveryProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryProfileRepository extends JpaRepository<DeliveryProfile, Long> {
    List<DeliveryProfile> findByShop(String shop);
}
