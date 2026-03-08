package com.skml.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skml.entity.Invoice;

@Repository
public interface InvoiceRepository  extends JpaRepository<Invoice, Long> {

}
