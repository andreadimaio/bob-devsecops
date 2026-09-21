package com.ibm.secure.demo;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends PanacheEntity {

    @Column(name = "full_name")
    public String fullName;
    
    public String email;
    
    @Column(name = "phone_number")
    public String phoneNumber;
    
    @Column(name = "tax_id")
    public String taxId;
    
    public String iban;
    
    public String notes;
}