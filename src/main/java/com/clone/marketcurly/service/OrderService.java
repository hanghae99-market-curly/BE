package com.clone.marketcurly.service;

import com.clone.marketcurly.dto.OrderRequestDto.OrderRequestDto;
import com.clone.marketcurly.model.*;
import com.clone.marketcurly.repository.CartRepository;
import com.clone.marketcurly.repository.OrderCartRepository;
import com.clone.marketcurly.repository.OrderRepository;
import com.clone.marketcurly.repository.ProductRepository;
import com.clone.marketcurly.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderCartRepository orderCartRepository;

    @Transactional
    public void saveOrder(OrderRequestDto orderRequestDto, UserDetailsImpl userDetails){

        List<Cart> cartList = cartRepository.findAllByUserId(userDetails.getUser().getId());
        List<OrderCart> finalOrder = new ArrayList<>();

        for (Cart cart:cartList){
            Optional<Product> product = productRepository.findById(cart.getProductId());
            String name = product.get().getName();
            int quantity = cart.getQuantity();
            String brand = product.get().getBrand();

            OrderCart orderCart = new OrderCart(brand,name,quantity);
            finalOrder.add(orderCart);
            orderCartRepository.save(orderCart);
        }
        Order order = new Order(userDetails.getUser(),orderRequestDto.getAddress(),finalOrder,orderRequestDto.getTotalPrice());
        orderRepository.save(order);
//        User user=userDetails.getUser();
//        List<Cart> cartList=cartRepository.findAllByUserId(user.getId());
//
    }
}
