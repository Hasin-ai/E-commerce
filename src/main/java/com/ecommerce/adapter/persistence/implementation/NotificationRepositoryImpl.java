package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.notification.NotificationJpaDataRepository;
import com.ecommerce.adapter.persistence.mapper.NotificationEntityMapper;
import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaDataRepository notificationJpaDataRepository;
    private final NotificationEntityMapper notificationEntityMapper;

    @Autowired
    public NotificationRepositoryImpl(NotificationJpaDataRepository notificationJpaDataRepository,
                                    NotificationEntityMapper notificationEntityMapper) {
        this.notificationJpaDataRepository = notificationJpaDataRepository;
        this.notificationEntityMapper = notificationEntityMapper;
    }

    @Override
    public Notification save(Notification notification) {
        Notification jpaEntity = notificationEntityMapper.toJpaEntity(notification);
        Notification savedEntity = notificationJpaDataRepository.save(jpaEntity);
        return notificationEntityMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaDataRepository.findById(id)
            .map(notificationEntityMapper::toDomainEntity);
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return notificationJpaDataRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return notificationJpaDataRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
            .map(notificationEntityMapper::toDomainEntity);
    }

    @Override
    public List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId) {
        return notificationJpaDataRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
            .stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public Long countUnreadByUserId(Long userId) {
        return notificationJpaDataRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    public List<Notification> findByStatusAndCreatedAtBefore(Notification.NotificationStatus status, LocalDateTime dateTime) {
        return notificationJpaDataRepository.findByStatusAndCreatedAtBefore(status, dateTime)
            .stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public List<Notification> findByTypeAndUserId(Notification.NotificationType type, Long userId) {
        return notificationJpaDataRepository.findByTypeAndUserId(type, userId)
            .stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public List<Notification> findByUserIdAndStatus(Long userId, Notification.NotificationStatus status) {
        return notificationJpaDataRepository.findByUserIdAndStatus(userId, status)
            .stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public List<Notification> findAll() {
        return notificationJpaDataRepository.findAll()
            .stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        notificationJpaDataRepository.deleteById(id);
    }

    @Override
    public void delete(Notification notification) {
        notificationJpaDataRepository.delete(notification);
    }

    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        List<Notification> jpaEntities = notifications.stream()
            .map(notificationEntityMapper::toJpaEntity)
            .toList();
        List<Notification> savedEntities = notificationJpaDataRepository.saveAll(jpaEntities);
        return savedEntities.stream()
            .map(notificationEntityMapper::toDomainEntity)
            .toList();
    }

    @Override
    public long count() {
        return notificationJpaDataRepository.count();
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationJpaDataRepository.markAsRead(notificationId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationJpaDataRepository.markAllAsRead(userId, LocalDateTime.now());
    }
}
