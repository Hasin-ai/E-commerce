package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.adapter.persistence.jpa.entity.NotificationJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {

    List<NotificationJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<NotificationJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<NotificationJpaEntity> findByStatusAndCreatedAtBefore(String status, LocalDateTime dateTime);

    List<NotificationJpaEntity> findByTypeAndUserId(String type, Long userId);

    @Query("SELECT n FROM NotificationJpaEntity n WHERE n.userId = :userId AND n.status = :status ORDER BY n.createdAt DESC")
    List<NotificationJpaEntity> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
