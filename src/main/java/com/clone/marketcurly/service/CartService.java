package com.clone.marketcurly.service;


import com.clone.marketcurly.dto.cartDto.CartResponseDto;
import com.clone.marketcurly.dto.productDto.ProductResponseDto;
import com.clone.marketcurly.model.Cart;
import com.clone.marketcurly.dto.cartDto.CartRequestDto;
import com.clone.marketcurly.model.Product;
import com.clone.marketcurly.model.User;
import com.clone.marketcurly.repository.CartRepository;
import com.clone.marketcurly.repository.ProductRepository;
import com.clone.marketcurly.repository.UserRepository;
import com.clone.marketcurly.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    // 장바구니 담기
    @Transactional
    public void saveCart(Long productId, CartRequestDto cartRequestDto, UserDetailsImpl userDetails) {

        // 장바구니에 담긴 한 상품의 수량
        int quantity = cartRequestDto.getQuantity();

        // 장바구니에 담긴 한 상품의 총가격(수량 x 가격)
        int sum = cartRequestDto.getSum();

        User user = userDetails.getUser();

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("제품이 존재하지 않습니다.")
                );

        // 장바구니 제품 갯수 유효성검사
        if (quantity == 0) {
            throw new IllegalArgumentException("장바구니에 담을 갯수를 정해주세요");
        }

        //유저와 상품아이디로 장바구니 하나를 찾아냄
        List<Cart> cartCheck = cartRepository.findAllByUserIdAndProductId(userDetails.getUser().getId(),productId);

        Cart cart = new Cart();

        //장바구니에 이미 해당 상품이 담겨있는 경우
        if (cartCheck.size() > 0) {
            for(Cart carts : cartCheck) {
                //메인 화면에서 장바구니 모양 눌렀을 경우(기존 수량에 고른 갯수만큼 더 더해줌)
                carts.setQuantity(carts.getQuantity() + quantity);
                carts.setSum(carts.getSum() + sum);
                cartRepository.save(carts);
            }
        } else {
            cart.setUser(user);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setSum(sum);
            cartRepository.save(cart);
        }
    }

    /*유저의 장바구니 조회*/
    public List<CartResponseDto> showCart(UserDetailsImpl userDetails) {
        // cartResponsDto에 brand, imgUrl, name, price, quntity를 보내줌
        List<CartResponseDto> cartResponseDtolist = new ArrayList<>();

//        // 로그인한 유저의 1개 장바구니 찾기
//        Cart cart = userDetails.getUser().getCart();

        // 장바구니 전체를 불러와 리스트에 저장한다.
        List<Cart> cartList = cartRepository.findAllByUserId(userDetails.getUser().getId());

        //for문을 돌면서 각 정보를 추거해
        for (Cart eachCart : cartList) {

            // productId를 이용해서 제품을 찾기
            Long productId = eachCart.getProductId();
            Product product = productRepository.findById((productId))
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            ProductResponseDto productResponseDto = new ProductResponseDto(product);

            // 수량 찾기
            int quantity = eachCart.getQuantity();

            // CartResponseDto에 넣기
            CartResponseDto cartResponseDto = new CartResponseDto(productResponseDto, quantity);

            // 반환할 리스트에 하나씩 넣어준다.
            cartResponseDtolist.add(cartResponseDto);
        }
        return cartResponseDtolist;
    }

    // 장바구니 삭제
    @Transactional
    public void deleteCart(Long productId, UserDetailsImpl userDetails) {
        Cart cart = cartRepository.findByUserIdAndProductId(userDetails.getUser().getId(), productId);
        if (cart == null){
            throw new IllegalArgumentException("장바구니에서 상품을 찾을 수 없습니다.");
        }
        // 장바구니 삭제
        cartRepository.delete(cart);
    }


}



