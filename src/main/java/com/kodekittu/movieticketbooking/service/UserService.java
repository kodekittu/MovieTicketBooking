package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.dto.common.PageResponse;
import com.kodekittu.movieticketbooking.dto.request.UpdateUserStatusRequest;
import com.kodekittu.movieticketbooking.dto.response.UserResponse;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.mapper.UserMapper;
import com.kodekittu.movieticketbooking.repository.UserRepository;
import com.kodekittu.movieticketbooking.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReferenceDataService referenceDataService;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse currentUser() {
        return userMapper.toResponse(referenceDataService.user(securityUtils.currentUserId()));
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        return userMapper.toResponse(referenceDataService.user(userId));
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> listUsers(Pageable pageable) {
        return PageResponse.from(userRepository.findAll(pageable).map(userMapper::toResponse));
    }

    @Transactional
    public UserResponse updateStatus(UUID userId, UpdateUserStatusRequest request) {
        User user = referenceDataService.user(userId);
        user.setStatus(request.status());
        return userMapper.toResponse(user);
    }
}
