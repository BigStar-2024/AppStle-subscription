package com.et.repository;

import com.et.domain.SocialConnection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the SocialConnection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialConnectionRepository extends JpaRepository<SocialConnection, Long> {

    Optional<SocialConnection> findByUserId(String userId);

    List<SocialConnection> findByUserIdIn(Set<String> userIds);

    @Query("select sc from SocialConnection sc where mod(sc.id, 40) = :socialConnectionSequence")
    List<SocialConnection> findShopsWithSequence(@Param("socialConnectionSequence") Integer socialConnectionSequence);

}
