package com.ecommerce.core.domain.analytics.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class UserActivity {
    private UUID id;
    private String userId;
    private List<Event> events;
    private LocalDateTime lastActivity;

    // Analytics counters
    private int pageViews;
    private int productViews;
    private int cartAdditions;
    private int purchases;
    private int searches;
    private int otherEvents;
    private double totalRevenue;

    public UserActivity(UUID id, String userId, List<Event> events, LocalDateTime lastActivity) {
        this.id = id;
        this.userId = userId;
        this.events = events;
        this.lastActivity = lastActivity;
        this.pageViews = 0;
        this.productViews = 0;
        this.cartAdditions = 0;
        this.purchases = 0;
        this.searches = 0;
        this.otherEvents = 0;
        this.totalRevenue = 0.0;
    }

    public UserActivity(Long userId, java.time.LocalDate date) {
        this.id = UUID.randomUUID();
        this.userId = userId.toString();
        this.events = List.of();
        this.lastActivity = date.atStartOfDay();
        this.pageViews = 0;
        this.productViews = 0;
        this.cartAdditions = 0;
        this.purchases = 0;
        this.searches = 0;
        this.otherEvents = 0;
        this.totalRevenue = 0.0;
    }

    // Constructor for analytics data with all counters
    public UserActivity(UUID id, String userId, List<Event> events, LocalDateTime lastActivity,
                       int pageViews, int productViews, int cartAdditions, int purchases,
                       int searches, int otherEvents, double totalRevenue) {
        this.id = id;
        this.userId = userId;
        this.events = events;
        this.lastActivity = lastActivity;
        this.pageViews = pageViews;
        this.productViews = productViews;
        this.cartAdditions = cartAdditions;
        this.purchases = purchases;
        this.searches = searches;
        this.otherEvents = otherEvents;
        this.totalRevenue = totalRevenue;
    }

    public void incrementPageViews() {
        this.pageViews++;
    }

    public void incrementProductViews() {
        this.productViews++;
    }

    public void incrementCartAdditions() {
        this.cartAdditions++;
    }

    public void incrementPurchases() {
        this.purchases++;
    }

    public void incrementSearches() {
        this.searches++;
    }

    public void incrementOtherEvents() {
        this.otherEvents++;
    }

    public void addRevenue(Double amount) {
        this.totalRevenue += amount;
    }

    public void updateLastActivity(LocalDateTime dateTime) {
        this.lastActivity = dateTime;
    }
}
