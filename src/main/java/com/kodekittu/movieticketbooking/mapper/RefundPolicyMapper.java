package com.kodekittu.movieticketbooking.mapper;

import com.kodekittu.movieticketbooking.dto.request.RefundPolicyRequest;
import com.kodekittu.movieticketbooking.dto.response.RefundPolicyResponse;
import com.kodekittu.movieticketbooking.entity.Movie;
import com.kodekittu.movieticketbooking.entity.RefundPolicy;
import com.kodekittu.movieticketbooking.entity.Theater;
import org.springframework.stereotype.Component;

@Component
public class RefundPolicyMapper {

    public RefundPolicy toEntity(RefundPolicyRequest request, Movie movie, Theater theater) {
        RefundPolicy policy = new RefundPolicy();
        update(policy, request, movie, theater);
        return policy;
    }

    public void update(RefundPolicy policy, RefundPolicyRequest request, Movie movie, Theater theater) {
        policy.setName(request.name());
        policy.setScope(request.scope());
        policy.setMovie(movie);
        policy.setTheater(theater);
        policy.setHoursBeforeShow(request.hoursBeforeShow());
        policy.setRefundPercentage(request.refundPercentage());
        policy.setPriority(request.priority());
        policy.setActive(request.active());
    }

    public RefundPolicyResponse toResponse(RefundPolicy policy) {
        return new RefundPolicyResponse(
                policy.getId(),
                policy.getName(),
                policy.getScope(),
                policy.getMovie() == null ? null : policy.getMovie().getId(),
                policy.getTheater() == null ? null : policy.getTheater().getId(),
                policy.getHoursBeforeShow(),
                policy.getRefundPercentage(),
                policy.getPriority(),
                policy.isActive(),
                policy.getCreatedAt(),
                policy.getUpdatedAt());
    }
}
