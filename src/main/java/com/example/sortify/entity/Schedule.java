//package com.example.sortify.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//// Schedule.java
//@Entity
//public class Schedule {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "team_id")
//    private Team team;
//
//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//
//    private String title;
//    private String description;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//    private int priority;
//    private LocalDateTime createdAt;
//}
//
