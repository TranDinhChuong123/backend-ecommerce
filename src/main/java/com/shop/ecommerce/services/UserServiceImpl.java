package com.shop.ecommerce.services;

import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.Address;
import com.shop.ecommerce.models.Order;
import com.shop.ecommerce.models.User;
import com.shop.ecommerce.models.enums.Gender;
import com.shop.ecommerce.models.enums.OrderStatus;
import com.shop.ecommerce.repositories.OrderRepository;
import com.shop.ecommerce.repositories.UserRepository;
import com.shop.ecommerce.requests.AddressRequest;

import com.shop.ecommerce.requests.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<Address> findAddressesByUsername(String email) {
        User user = userRepository.findUserByUsername(email)
                .orElseThrow(() -> new NotFoundException(("User " + email + " does not exist")));
        return user.getAddresses();
    }

    @Override
    public boolean UpdateStateAddressByUser(String email, String selectedAddress) {
        logger.debug("UpdateStateAddressByUser {} {}", email, selectedAddress);
        User user = userRepository.findUserByUsername(email).orElseThrow(() -> new NotFoundException(("User " + email + " does not")));
        for (Address addr : user.getAddresses()) {
            if (addr.getId().equals(selectedAddress)) {
                addr.setState(true);
            } else {
                addr.setState(false);
            }
        }
        userRepository.save(user);
        return true;
    }


    public boolean addAddressToUser(String username, AddressRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("User " + username + " does not exist"));
        Address address;

        if (request.getId() != null) {
            address = user.getAddresses().stream()
                    .filter(addr -> addr.getId().equals(request.getId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Address not found for ID: " + request.getId()));
            address.setName(request.getName());
            address.setPhoneNumber(request.getPhoneNumber());
            address.setStreet(request.getStreet());
            address.setState(true);
        } else {
            user.getAddresses().forEach(addr -> addr.setState(false));
            address = Address.builder()
                    .id(UUID.randomUUID().toString())
                    .name(request.getName())
                    .phoneNumber(request.getPhoneNumber())
                    .street(request.getStreet())
                    .state(true)
                    .build();
            user.getAddresses().add(address);


        }

        userRepository.save(user);
        return true;
    }

    @Override
    public List<Order> getListOrderStatusByUserId(String username ,OrderStatus orderStatus) {
        return orderRepository.findByUserIdAndOrderStatus(
                username,
                orderStatus,
                Sort.by(Sort.Direction.DESC, "createdAt")
        ).orElse(new ArrayList<>());
    }

//    @Override
//    public List<Order> getListOrderCanceledAndPaymentStatus(String username, OrderStatus status) {
//        return orderRepository.findByUserIdAndOrderStatus(
//                username,
//                orderStatus,
//                Sort.by(Sort.Direction.DESC, "createdAt")
//        ).orElse(new ArrayList<>());
//    }

    @Override
    public int getWalletUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("Wallet user not found"))
                .getWallet();
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("User not found for userId: " + userId));
    }

    @Override

    public User updateUser(User user) {
        logger.info("Update user {}", user);
        User userSaved = userRepository.findById(user.getEmail())
                .orElseThrow(() -> new NotFoundException("Wallet user not found"));
        // Cập nhật thông tin của userSaved bằng thông tin từ user
        userSaved.setName(user.getName());
        userSaved.setDob(user.getDob());
        if (user.getGender() != null) {
            userSaved.setGender(user.getGender());
        }
        userSaved.setPhoneNumber(user.getPhoneNumber());
        userSaved.setImage(user.getImage());
        userSaved.setUpdatedAt(user.getUpdatedAt());

        // Lưu lại userSaved sau khi đã cập nhật
        return userRepository.save(userSaved);
    }




}

