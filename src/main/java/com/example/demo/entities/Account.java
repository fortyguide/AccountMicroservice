package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id
    @Column(name = "ID")
    @Getter @Setter
    private String id;

    @Column(name = "FK_USER")
    @Getter @Setter
    private String fkUser;

    @Column(name = "TOTAL")
    @Getter @Setter
    private Double total;
}
