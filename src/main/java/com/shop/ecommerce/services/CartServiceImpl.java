package com.shop.ecommerce.services;

import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.*;
import com.shop.ecommerce.models.enums.CartState;
import com.shop.ecommerce.models.enums.InteractionType;
import com.shop.ecommerce.repositories.CartRepository;
import com.shop.ecommerce.repositories.InventoryRepository;
import com.shop.ecommerce.repositories.ProductRepository;
import com.shop.ecommerce.repositories.UserProductInteractionRepository;
import com.shop.ecommerce.requests.CartRemoveRequest;
import com.shop.ecommerce.requests.CartRequest;
import com.shop.ecommerce.requests.UpdateCheckedRequest;
import com.shop.ecommerce.responses.CartProductResponse;
import com.shop.ecommerce.responses.CartResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserProductInteractionRepository userProductInteractionRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public CartResponse findCartByUserId(String userId) {
        Cart cartFound = cartRepository.findCartByUserIdAndCartState(userId, CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        List<CartProductResponse> cartProducts = cartFound.getCartProducts().stream()
                .map(cartProduct -> {
                    return getcartProduct(
                            cartProduct.getId(),
                            cartProduct.isChecked(),
                            cartProduct.getProductId(),
                            cartProduct.getSelectedVariationId(),
                            cartProduct.getBuyQuantity()
                    );
                })
                .collect(Collectors.toList());

        // Đảo ngược danh sách
        Collections.reverse(cartProducts);

        return CartResponse.builder()
                .cartId(cartFound.getId())
                .userId(userId)
                .cartProducts(cartProducts)
                .build();
    }

    @Override
    public boolean updateProductCheckedStatus(String userId,List<String> cartProductIds) {
        Cart cart = cartRepository.findCartByUserIdAndCartState(userId, CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getCartProducts().forEach(cartProduct -> {
            cartProduct.setChecked(cartProductIds.contains(cartProduct.getId()));
        });
        saveCart(cart);
        return true;
    }

    @Override
    public boolean addOrUpdateProductInCart(CartRequest request) {
        Cart cart = cartRepository.findCartByUserIdAndCartState(request.getUserId(), CartState.ACTIVE)
                .orElseGet(() -> createNewActiveCart(request.getUserId()));

        boolean productExists = cart.getCartProducts().stream()
                .anyMatch(product -> product.getProductId().equals(request.getCartProduct().getProductId())
                        && product.getSelectedVariationId().equals(request.getCartProduct().getSelectedVariationId()));

        int quantityInStock =  inventoryRepository
                .findByVariationId(request.getCartProduct().getSelectedVariationId())
                .orElseThrow()
                .getQuantityInStock();

        if (productExists) {
            cart.getCartProducts().forEach(cartProduct -> {
                if (cartProduct.getProductId().equals(request.getCartProduct().getProductId())
                        && cartProduct.getSelectedVariationId().equals(request.getCartProduct().getSelectedVariationId())
                ) {
                    int totalQuantity = cartProduct.getBuyQuantity() + request.getCartProduct().getBuyQuantity();
                    if (totalQuantity > quantityInStock) {
                        throw new RuntimeException("Số lượng sản phẩm trong kho không đủ.");
                    }
                    cartProduct.setBuyQuantity(totalQuantity);
                }
            });
        } else {
            if (request.getCartProduct().getBuyQuantity() > quantityInStock) {
                throw new RuntimeException("Số lượng sản phẩm trong kho không đủ.");
            }
            cart.getCartProducts().add(request.getCartProduct());
        }
        if(request.getUserId()!= null && request.getCartProduct().getProductId() != null){
            UserProductInteraction interaction = UserProductInteraction.builder()
                    .userId(request.getUserId())
                    .productId(request.getCartProduct().getProductId())
                    .interactionType(InteractionType.ADDED_TO_CART)
                    .interactionTime(LocalDateTime.now())
                    .build();
            userProductInteractionRepository.save(interaction);
        }

        saveCart(cart);
        return true;
    }



    @Override
    public boolean updateProductQuantityInCart(CartRequest request) {

        Cart cart= cartRepository.findCartByUserIdAndCartState(request.getUserId(), CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        // Update product quantity
        cart.getCartProducts().forEach(cartProduct -> {
            if (cartProduct.getId().equals(request.getCartProduct().getId())) {
                cartProduct.setBuyQuantity(request.getCartProduct().getBuyQuantity());
            }
        });
        saveCart(cart);
        return true;
    }


    @Override
    public boolean removeProductsFromCart(CartRemoveRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        List<String> productsToRemove = request.getCartProductIds(); // Giả sử bạn có danh sách sản phẩm cần xóa
        cart.getCartProducts().removeIf(
                cartProduct -> productsToRemove.stream().anyMatch(toRemove ->
                        cartProduct.getId().equals(toRemove)
                )
        );
        saveCart(cart);
        return true;
    }


    @Override
    public boolean deleteCartByUserId(String userId) {
        Cart cart = cartRepository.findCartByUserIdAndCartState(userId, CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        cartRepository.delete(cart);
        return true;
    }

    @Override
    public int cartProductsLength(String userId) {
        Cart cart = cartRepository.findCartByUserIdAndCartState(userId, CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        return cart.getCartProducts().size();
    }

    @Override
    public boolean addPropductBuyNow(CartRequest request) {

        boolean isAdd = addOrUpdateProductInCart(request);

        Cart cart = cartRepository.findCartByUserIdAndCartState(request.getUserId(), CartState.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cart active not found !"));


        cart.getCartProducts().forEach(cartProduct ->
                cartProduct.setChecked(
                        cartProduct.getSelectedVariationId().equals(
                                request.getCartProduct().getSelectedVariationId())));

        cartRepository.save(cart);

        return true;
    }

    private Cart createNewActiveCart(String userId) {
        Cart newCart = Cart.builder()
                .userId(userId)
                .cartState(CartState.ACTIVE)
                .cartProducts(new ArrayList<>())
                .build();
        return saveCart(newCart);
    }


    private Cart saveCart(Cart cart) {
        Cart savedCart = cartRepository.save(cart);
        if (savedCart.getId() == null) {
            throw new NotFoundException("Failed to save cart");
        }
        return savedCart;
    }

    public CartProductResponse getcartProduct(String cartProductId, boolean isChecked, String productId, String variationId, int buyQuantity) {
        Product product = productRepository.findById(productId).orElseThrow();

        ProductVariation selectedVariation = product.getProductVariations().stream()
                .filter(v -> v.getId().equals(variationId))
                .findFirst()
                .orElse(product.getProductVariations().get(0));

        Image image = product.getImages().stream()
                .filter(i -> i.getColor() != null && !i.getColor().isEmpty() && i.getColor().equals(selectedVariation.getColor()))
                .findFirst()
                .orElseGet(() -> product.getImages().isEmpty() ? null : product.getImages().get(0));

        int quantityInStock =  inventoryRepository
                .findByVariationId(variationId)
                .orElseThrow()
                .getQuantityInStock();

        return CartProductResponse.builder()
                .id(cartProductId)
                .isChecked(isChecked)
                .productId(productId)
                .selectedVariationId(variationId)
                .name(product.getName())
                .slug(product.getSlug())
                .urlImage(image.getUrlImage())
                .color(selectedVariation.getColor())
                .capacity(selectedVariation.getCapacity())
                .size(selectedVariation.getSize())
                .price(selectedVariation.getPrice())
                .discountPercent(selectedVariation.getDiscountPercent())
                .quantity(quantityInStock)
                .buyQuantity(buyQuantity)
                .build();
    }


}

