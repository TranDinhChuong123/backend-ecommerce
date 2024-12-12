package com.shop.ecommerce.services;

import com.shop.ecommerce.requests.CartRemoveRequest;
import com.shop.ecommerce.requests.CartRequest;
import com.shop.ecommerce.requests.UpdateCheckedRequest;
import com.shop.ecommerce.responses.CartResponse;

import java.util.List;

public interface CartService {

    boolean updateProductCheckedStatus(String userId,List<String> cartProductIds);
    boolean addOrUpdateProductInCart(CartRequest request);

    boolean removeProductsFromCart(CartRemoveRequest request);
    boolean updateProductQuantityInCart(CartRequest request);
    CartResponse findCartByUserId(String userId);

    boolean deleteCartByUserId(String userId);
    int cartProductsLength (String userId);

    boolean addPropductBuyNow(CartRequest request);

}
