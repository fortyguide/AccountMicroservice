package com.example.demo.entities;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "operations")
@AllArgsConstructor @NoArgsConstructor
public class Operation {

    @Id
    @Column(name = "ID")
    @Getter @Setter
    private String id;

    @Column(name = "DATE")
    @Getter @Setter
    private Date date;

    @Column(name = "DESCRIPTION")
    @Getter @Setter
    private String description;

    @Column(name = "AMOUNT")
    @Getter @Setter
    private Double value;

    @Column(name = "FK_ACCOUNT1")
    @Getter @Setter
    private String fkAccount1;

    @Column(name = "FK_ACCOUNT2")
    @Getter @Setter
    private String fkAccount2;

    /* @PrePersist serve per settare la data direttamente dal sistema */
    @PrePersist
    void getTimeOperation(){
        this.date = new Date();
    }
}
