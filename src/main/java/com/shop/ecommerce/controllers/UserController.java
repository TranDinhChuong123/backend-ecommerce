package com.shop.ecommerce.controllers;

import com.shop.ecommerce.models.Address;
import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.User;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.requests.AddressRequest;

import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;

    @GetMapping("/addresses")
    public ApiResponse<List<Address>> findAddressesByUsername(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ApiResponse<>(
                "Addresses User retrieved successfully",
                userService.findAddressesByUsername(userDetails.getUsername())
        );
    }

    @PostMapping("/add-address")
    public ApiResponse<Boolean> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddressRequest request
    ) {
        return new ApiResponse<>(
                "User added Address successfully",
                userService.addAddressToUser(userDetails.getUsername() , request)
        );
    }

    @GetMapping("/address/{selectedAddressId}/set-default")
    public ApiResponse<Boolean> UpdateStateAddressByUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String selectedAddressId) {
        return new ApiResponse<>(
                200,
                "Update State Address ByUser successfully",
                userService.UpdateStateAddressByUser(userDetails.getUsername() , selectedAddressId)
        );
    }
    @GetMapping("/status-orders/{orderStatus}")
    public ApiResponse<List<Order>> getListOrderStatusByUserId(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable OrderStatus orderStatus
            ) {
        return new ApiResponse<>(
                200,
                "Get List Order Status successfully",
                userService.getListOrderStatusByUserId(userDetails.getUsername(),orderStatus)
        );
    }

    @GetMapping("/wallet")
    public ApiResponse<Integer> getWalletUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ApiResponse<>(
                200,
                "Get wallet user Status successfully",
                userService.getWalletUser(userDetails.getUsername())
        );
    }

    @GetMapping("/all")
    public ApiResponse<List<User>> findAllUser() {
        return new ApiResponse<>(
                200,
                "findAllUser successfully",
                userService.findAllUser()
        );
    }

    @GetMapping("/profile")
    public ApiResponse<User> getUserByUserId(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ApiResponse<>(
                200,
                "findAllUser successfully",
                userService.getUserByUserId(userDetails.getUsername())
        );
    }
    @PostMapping("/update")
    public ApiResponse<User> updateUser(
           @RequestBody User user
    ) {
        return new ApiResponse<>(
                200,
                "findAllUser successfully",
                userService.updateUser(user)
        );
    }






}
