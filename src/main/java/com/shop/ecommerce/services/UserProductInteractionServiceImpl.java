package com.shop.ecommerce.services;

import com.shop.ecommerce.models.UserProductInteraction;
import com.shop.ecommerce.repositories.UserProductInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProductInteractionServiceImpl implements UserProductInteractionService{
    private final UserProductInteractionRepository userProductInteractionRepository;

    @Override
    public boolean createUserProductInteraction(UserProductInteraction userProductInteraction) {
        userProductInteractionRepository.save(userProductInteraction);
        return true;
    }

}
