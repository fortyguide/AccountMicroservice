package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Con @@AllArgsConstructor e @NoArgsConstructor
*  Spring ci definisce in automatico i costruttori
*  della classe con e senza argomenti*/
@AllArgsConstructor @NoArgsConstructor
/* @Entity mappa una tabella con il nome
 * definito in @Table */
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "ID")
    @Getter @Setter
    private String id;

    @Column(name = "USERNAME")
    @Getter @Setter
    private String username;

    @Column(name = "PASSWORD")
    @Getter @Setter
    private String password;

    @Column(name = "PERMISSION")
    @Getter @Setter
    private String permission;
}
