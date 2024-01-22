package com.et.service.impl;

import com.et.domain.MembershipDiscountProducts;
import com.et.repository.MembershipDiscountProductsRepository;
import com.et.service.MembershipDiscountService;
import com.et.domain.MembershipDiscount;
import com.et.repository.MembershipDiscountRepository;
import com.et.service.dto.MembershipDiscountDTO;
import com.et.service.mapper.MembershipDiscountMapper;
import com.et.web.rest.vm.MembershipDiscountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MembershipDiscount}.
 */
@Service
@Transactional
public class MembershipDiscountServiceImpl implements MembershipDiscountService {

    private final Logger log = LoggerFactory.getLogger(MembershipDiscountServiceImpl.class);

    private final MembershipDiscountRepository membershipDiscountRepository;

    private final MembershipDiscountMapper membershipDiscountMapper;

    @Autowired
    private MembershipDiscountProductsRepository membershipDiscountProductsRepository;

    public MembershipDiscountServiceImpl(MembershipDiscountRepository membershipDiscountRepository, MembershipDiscountMapper membershipDiscountMapper) {
        this.membershipDiscountRepository = membershipDiscountRepository;
        this.membershipDiscountMapper = membershipDiscountMapper;
    }

    @Override
    public MembershipDiscountDTO save(MembershipDiscountDTO membershipDiscountDTO) {
        log.debug("Request to save MembershipDiscount : {}", membershipDiscountDTO);
        MembershipDiscount membershipDiscount = membershipDiscountMapper.toEntity(membershipDiscountDTO);
        membershipDiscount = membershipDiscountRepository.save(membershipDiscount);
        return membershipDiscountMapper.toDto(membershipDiscount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipDiscountDTO> findAll() {
        log.debug("Request to get all MembershipDiscounts");
        return membershipDiscountRepository.findAll().stream()
            .map(membershipDiscountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipDiscountDTO> findOne(Long id) {
        log.debug("Request to get MembershipDiscount : {}", id);
        return membershipDiscountRepository.findById(id)
            .map(membershipDiscountMapper::toDto);
    }

    @Override
    public void delete(String shop,Long id) {
        log.debug("Request to delete MembershipDiscount : {}", id);
        List<MembershipDiscountProducts> membershipDiscountProductsOlds = membershipDiscountProductsRepository.findByShopAndMembershipDiscountId(shop,id);
        if(membershipDiscountProductsOlds.size() > 0){
            membershipDiscountProductsRepository.deleteAll(membershipDiscountProductsOlds);
        }
        membershipDiscountRepository.deleteById(id);
    }

    @Override
    public List<MembershipDiscountDTO> findByShop(String shop) {
        log.debug("Request to get all MembershipDiscounts");
        return membershipDiscountRepository.findByShop(shop).stream()
            .map(membershipDiscountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public MembershipDiscountDTO createOrUpdate(String shop, MembershipDiscountRequest membershipDiscountRequest) {
        MembershipDiscount membershipDiscount = membershipDiscountRequest.getMembershipDiscount();
        membershipDiscount.setShop(shop);
        membershipDiscount = membershipDiscountRepository.save(membershipDiscount);
        if(membershipDiscountRequest.getMembershipDiscountProducts().size() > 0){
            List<MembershipDiscountProducts> membershipDiscountProductsOlds = membershipDiscountProductsRepository.findByShopAndMembershipDiscountId(shop,membershipDiscount.getId());
            if(membershipDiscountProductsOlds.size() > 0){
                for(MembershipDiscountProducts membershipDiscountProducts:membershipDiscountRequest.getMembershipDiscountProducts()){
                    Optional<MembershipDiscountProducts> membershipDiscountProductsFind = membershipDiscountProductsOlds.stream().filter(mdp->membershipDiscountProducts.getProductId().equals(mdp.getProductId())).findFirst();
                    if(!membershipDiscountProductsFind.isPresent()){
                        membershipDiscountProducts.setShop(shop);
                        membershipDiscountProducts.setMembershipDiscountId(membershipDiscount.getId());
                        membershipDiscountProductsRepository.save(membershipDiscountProducts);
                    }
                }
                for (MembershipDiscountProducts membershipDiscountProductold:membershipDiscountProductsOlds){
                    Optional<MembershipDiscountProducts> membershipDiscountProductOldFind = membershipDiscountRequest.getMembershipDiscountProducts().stream().filter(mdp->membershipDiscountProductold.getProductId().equals(mdp.getProductId())).findFirst();
                    if(!membershipDiscountProductOldFind.isPresent()){
                        membershipDiscountProductsRepository.delete(membershipDiscountProductold);
                    }
                }
            } else {
                for(MembershipDiscountProducts membershipDiscountProducts : membershipDiscountRequest.getMembershipDiscountProducts()){
                    membershipDiscountProducts.setShop(shop);
                    membershipDiscountProducts.setMembershipDiscountId(membershipDiscount.getId());
                    membershipDiscountProductsRepository.save(membershipDiscountProducts);
                }
            }
        }
        return membershipDiscountMapper.toDto(membershipDiscount);
    }
}
