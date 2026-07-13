package com.kodekittu.movieticketbooking.repository;

import com.kodekittu.movieticketbooking.entity.DiscountCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon, UUID> {

    Optional<DiscountCoupon> findByCodeIgnoreCase(String code);

    Page<DiscountCoupon> findByActiveTrue(Pageable pageable);
}
