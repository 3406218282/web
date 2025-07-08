package com.restaurant.orderingsystem.repository;

import com.restaurant.orderingsystem.entity.Order;
import com.restaurant.orderingsystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByOrder(Order order);
    
    Optional<Payment> findByOrderAndPaymentStatus(Order order, Payment.PaymentStatus paymentStatus);
    
    List<Payment> findByPaymentStatus(Payment.PaymentStatus paymentStatus);
} 