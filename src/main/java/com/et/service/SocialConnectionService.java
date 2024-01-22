package com.et.service;

import com.et.domain.SocialConnection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link SocialConnection}.
 */
public interface SocialConnectionService {

    /**
     * Save a socialConnection.
     *
     * @param socialConnection the entity to save.
     * @return the persisted entity.
     */
    SocialConnection save(SocialConnection socialConnection);

    List<SocialConnection> findAll();

    /**
     * Get all the socialConnections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SocialConnection> findAll(Pageable pageable);


    /**
     * Get the "id" socialConnection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SocialConnection> findOne(Long id);

    /**
     * Delete the "id" socialConnection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<SocialConnection> findByProviderIdAndUserId(String providerId, String userId);

    Optional<SocialConnection> findByUserId(String shop);

    List<SocialConnection> findByUserIdIn(Set<String> shops);

    void removeFromCache(String shop);
}
