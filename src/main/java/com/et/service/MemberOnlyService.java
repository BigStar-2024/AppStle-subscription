package com.et.service;

import com.et.service.dto.MemberOnlyDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.MemberOnly}.
 */
public interface MemberOnlyService {

    /**
     * Save a memberOnly.
     *
     * @param memberOnlyDTO the entity to save.
     * @return the persisted entity.
     */
    MemberOnlyDTO save(MemberOnlyDTO memberOnlyDTO);

    /**
     * Get all the memberOnlies.
     *
     * @return the list of entities.
     */
    List<MemberOnlyDTO> findAll();


    @Transactional(readOnly = true)
    List<MemberOnlyDTO> findAllByShop(String shop);

    /**
     * Get the "id" memberOnly.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemberOnlyDTO> findOne(Long id);

    /**
     * Delete the "id" memberOnly.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
