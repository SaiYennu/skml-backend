package com.skml.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skml.entity.Leads;
import com.skml.service.LeadsService;

@RestController
@CrossOrigin
@RequestMapping("/leads")
public class LeadsController 
{

   
	@Autowired
	private LeadsService leadsService;

    
	@GetMapping("/getall")
	public List<Leads> getallleads()
	{
		return leadsService.getallleads();
	}
	
	@PostMapping("/save")
	public String savelead(@RequestBody Leads requestLeads)
	{
		return leadsService.saveleads(requestLeads);
	}
	

}
