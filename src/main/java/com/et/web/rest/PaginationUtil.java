package com.et.web.rest;
import org.springframework.data.jpa.domain.Specification;

public class PaginationUtil {
  public static <T> Specification<T> createSpecification() {
    return Specification.where(null);
  }
}
