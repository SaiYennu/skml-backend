package com.skml.service;

import java.util.List;

import com.skml.entity.Leads;

public interface LeadsService {

	List<Leads> getallleads();

	String saveleads(Leads requestLeads);

}
