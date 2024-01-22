package com.et.repository;

import com.et.domain.MemberOnly;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MemberOnly entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberOnlyRepository extends JpaRepository<MemberOnly, Long> {

    List<MemberOnly> findAllByShop(String shop);
}
