package com.skml.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skml.entity.Leads;

public interface LeadsRepo extends JpaRepository<Leads,Long>{

}
