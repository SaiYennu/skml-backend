package com.skml.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skml.entity.Leads;
import com.skml.repository.LeadsRepo;
@Service
public class LeadsServiceImpl implements LeadsService
{
	@Autowired
	private LeadsRepo leadsRepo;

	@Autowired
	private EmailService emailService;
	@Override
	public List<Leads> getallleads() {
		return leadsRepo.findAll();
	}

	@Override
	public String saveleads(Leads requestLeads) {
		leadsRepo.save(requestLeads);
		 emailService.sendLeadMail(
		            "New Lead Received",
		            "Contact from filled in your website. \nName: " + requestLeads.getName()
		            + "\nPhone: " + requestLeads.getPhone()
		            + "\nMessage: " + requestLeads.getMessage()
		        );
		return "lead Saved Success";
	}
	
}
