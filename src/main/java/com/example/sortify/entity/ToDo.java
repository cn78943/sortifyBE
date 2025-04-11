//package com.example.sortify.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//// ToDo.java
//@Entity
//public class ToDo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "schedule_id")
//    private Schedule schedule;
//
//    @ManyToOne
//    @JoinColumn(name = "assigned_to")
//    private User assignedTo;
//
//    private String task;
//    private String status;
//    private LocalDateTime createdAt;
//}
