package com.ibm.secure.demo;

import java.math.BigDecimal;
import java.time.LocalDate;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROJECTS")
public class Project extends PanacheEntity {

    @Column(name = "customer_id")
    public Long customerId;

    @Column(name = "project_name")
    public String projectName;
    
    public BigDecimal budget;
    
    public BigDecimal revenue;

    @Column(name = "start_date")
    public LocalDate startDate;
    
    public String status;
}
