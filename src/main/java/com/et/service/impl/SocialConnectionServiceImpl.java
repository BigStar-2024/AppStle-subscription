package com.et.service.impl;

import com.et.service.SocialConnectionService;
import com.et.domain.SocialConnection;
import com.et.repository.SocialConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing {@link SocialConnection}.
 */
@Service
@Transactional
public class SocialConnectionServiceImpl implements SocialConnectionService {

    private final Logger log = LoggerFactory.getLogger(SocialConnectionServiceImpl.class);

    private final SocialConnectionRepository socialConnectionRepository;

    public SocialConnectionServiceImpl(SocialConnectionRepository socialConnectionRepository) {
        this.socialConnectionRepository = socialConnectionRepository;
    }

    /**
     * Save a socialConnection.
     *
     * @param socialConnection the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SocialConnection save(SocialConnection socialConnection) {
        try {
            log.debug("Request to save SocialConnection : {}", socialConnection);
            return socialConnectionRepository.save(socialConnection);
        } catch (Exception e) {
            log.error("Error occurred while saving social connection, error= {}", e.getMessage());
            Optional<SocialConnection> oldSocialConnectionOpt = socialConnectionRepository.findByUserId(socialConnection.getUserId());
            if (oldSocialConnectionOpt.isPresent()) {
                SocialConnection oldSocialConnection = oldSocialConnectionOpt.get();
                oldSocialConnection.setProverId(socialConnection.getProverId());
                oldSocialConnection.setAccessToken(socialConnection.getAccessToken());
                //oldSocialConnection.setGraphqlRateLimit(socialConnection.getGraphqlRateLimit());
                //oldSocialConnection.setRestRateLimit(socialConnection.getRestRateLimit());
                return socialConnectionRepository.save(oldSocialConnection);
            } else {
                return null;
            }
        }
    }
    @Override
    public List<SocialConnection> findAll() {
        return socialConnectionRepository.findAll();
    }

    /**
     * Get all the socialConnections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SocialConnection> findAll(Pageable pageable) {
        log.debug("Request to get all SocialConnections");
        return socialConnectionRepository.findAll(pageable);
    }


    /**
     * Get one socialConnection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SocialConnection> findOne(Long id) {
        log.debug("Request to get SocialConnection : {}", id);
        return socialConnectionRepository.findById(id);
    }

    /**
     * Delete the socialConnection by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SocialConnection : {}", id);
        socialConnectionRepository.deleteById(id);
    }

    @Override
    public Optional<SocialConnection> findByProviderIdAndUserId(String providerId, String userId) {
        /*SocialConnection socialConnection = new SocialConnection();
        socialConnection.setProverId(providerId);
        socialConnection.setUserId(userId);
        Example<SocialConnection> example = Example.of(socialConnection);

        Optional<SocialConnection> one = socialConnectionRepository.findOne(example);
        return one;*/

        return findByUserId(userId);
    }

    @Override
    public Optional<SocialConnection> findByUserId(String shop) {
        Optional<SocialConnection> socialConnection = socialConnectionRepository.findByUserId(shop);
        return socialConnection;
    }

    @Override
    public List<SocialConnection> findByUserIdIn(Set<String> shops) {
        return socialConnectionRepository.findByUserIdIn(shops);
    }

    @Override
    public void removeFromCache(String shop) {
    }
}
