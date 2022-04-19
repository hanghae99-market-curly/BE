package com.clone.marketcurly.repository;

import com.clone.marketcurly.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    void deleteAllByProductInCartId(Long productInCartId);
}
