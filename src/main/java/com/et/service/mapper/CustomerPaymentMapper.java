package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.CustomerPaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerPayment} and its DTO {@link CustomerPaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerPaymentMapper extends EntityMapper<CustomerPaymentDTO, CustomerPayment> {



    default CustomerPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerPayment customerPayment = new CustomerPayment();
        customerPayment.setId(id);
        return customerPayment;
    }
}
