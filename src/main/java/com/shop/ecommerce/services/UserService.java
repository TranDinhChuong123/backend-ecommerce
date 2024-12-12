package com.shop.ecommerce.services;


import com.shop.ecommerce.models.Address;
import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.OrderProduct;
import com.shop.ecommerce.models.User;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.requests.AddressRequest;

import java.util.List;


public interface UserService {
    boolean userExistsByEmail(String email);
    List<Address> findAddressesByUsername(String email);

    boolean UpdateStateAddressByUser(String email, String selectedAddress);

    boolean addAddressToUser(String username, AddressRequest request);

    List<Order> getListOrderStatusByUserId(String username, OrderStatus status);

//    List<Order> getListOrderCanceledAndPaymentStatus(String username, OrderStatus status);

    int getWalletUser(String userId);

    List<User> findAllUser();


    User getUserByUserId(String userId);

    User updateUser(User User);


}

