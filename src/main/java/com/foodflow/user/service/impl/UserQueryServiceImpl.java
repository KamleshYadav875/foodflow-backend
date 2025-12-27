package com.foodflow.user.service.impl;

import com.foodflow.user.entity.User;
import com.foodflow.user.repository.UserRepository;
import com.foodflow.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
