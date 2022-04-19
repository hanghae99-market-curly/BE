package com.clone.marketcurly.controller;



import com.clone.marketcurly.dto.OrderRequestDto.OrderRequestDto;
import com.clone.marketcurly.model.*;
import com.clone.marketcurly.repository.CartRepository;
import com.clone.marketcurly.repository.OrderCartRepository;
import com.clone.marketcurly.repository.OrderRepository;
import com.clone.marketcurly.repository.ProductRepository;
import com.clone.marketcurly.security.UserDetailsImpl;
import com.clone.marketcurly.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final CartRepository cartRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;


    //유효성 검사 아직 안함
    @PostMapping("/api/myorder")
    public List<Order> saveOrder(@RequestBody OrderRequestDto orderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //본인이 맞다면, 아니라면..?
        //로그인을 했다면 안했다면..?
        User user = userDetails.getUser();
        orderService.saveOrder(orderRequestDto, user);

        //유저아이디만 보고지워? ㅇㅇ 카트는 1회용 지웠다가 다시씀
        //cartRepository.deleteByUser(user);

        List <Order> order = orderRepository.findByUser(user);
        return order;
        //delete 문제 같음





    }
}
