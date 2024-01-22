package com.et.repository;

import com.et.domain.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the OrderInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    List<OrderInfo> findByShopAndOrderIdIn(String shop, List<Long> orderId);

    Page<OrderInfo> findAllByShop(String shop, Pageable pageable);

    void deleteByShopAndOrderId(String shop, Long orderId);

}
