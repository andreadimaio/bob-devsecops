package com.ibm.secure.demo;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccount extends PanacheEntity {

    @Column(name = "customer_id")
    public String customerId;

    public String username;

    @Column(name = "password")
    public String password;

    public String role;
}