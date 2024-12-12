package com.shop.ecommerce.controllers;

import com.shop.ecommerce.requests.CartRemoveRequest;
import com.shop.ecommerce.requests.CartRequest;
import com.shop.ecommerce.requests.UpdateCheckedRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.models.Cart;
import com.shop.ecommerce.responses.CartResponse;
import com.shop.ecommerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {
    private final CartService cartService;

    // Thêm sản phẩm vào giỏ hàng và cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/add-or-update")
    public ApiResponse<Boolean> addOrUpdateProductInCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartRequest request
    ) {
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "Product added to cart or update",
                cartService.addOrUpdateProductInCart(request)
        );
    }

    @PostMapping("/buy-now")
    public ApiResponse<Boolean> addPropductBuyNow(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartRequest request
    ) {
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "addPropductBuyNow success",
                cartService.addPropductBuyNow(request)
        );
    }

    @PutMapping("/update-checked-status")
    public ApiResponse<Boolean> updateCheckedStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateCheckedRequest request
    ) {
        return new ApiResponse<>(
                200,
                "Checked status updated",
                cartService.updateProductCheckedStatus(userDetails.getUsername(),request.getCartProductIds() )
        );
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove")
    public ApiResponse<Boolean> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartRemoveRequest request
    ) {

        return new ApiResponse<>(
                200,
                "Products removed from cart",
                cartService.removeProductsFromCart(request)
        );
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/update-quantity")
    public ApiResponse<Boolean> updateProductQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartRequest request
    ) {
        request.setUserId(userDetails.getUsername());
        return new ApiResponse<>(
                200,
                "Product quantity updated",
                cartService.updateProductQuantityInCart(request)
        );
    }

    // Tìm giỏ hàng theo userId
    @GetMapping("/userId")
    public ApiResponse<CartResponse> getCartByUserId(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ApiResponse<>(
                200,
                "Cart retrieved successfully",
                cartService.findCartByUserId(userDetails.getUsername())
        );
    }

    @GetMapping("/cart-products-length")
    public ApiResponse<Integer> cartProductsLength(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ApiResponse<>(
                200,
                "Cart length successfully",
                cartService.cartProductsLength(userDetails.getUsername())
        );
    }

    // Xóa toàn bộ giỏ hàng theo userId
    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Boolean> deleteCartByUserId(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String userId
    ) {
        return new ApiResponse<>(
                200,
                "Cart deleted successfully",
                cartService.deleteCartByUserId(userDetails.getUsername())
        );
    }
}
