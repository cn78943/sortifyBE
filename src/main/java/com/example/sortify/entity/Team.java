//package com.example.sortify.entity;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//
//// Team.java
//@Entity
//public class Team {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private LocalDateTime createdAt;
//
//    @OneToMany(mappedBy = "team")
//    private List<Schedule> schedules = new ArrayList<>();
//}
