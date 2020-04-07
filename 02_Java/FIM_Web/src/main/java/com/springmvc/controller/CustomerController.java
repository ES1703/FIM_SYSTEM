package com.springmvc.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springmvc.entity.Customer;
import com.springmvc.entity.SenDht11;
import com.springmvc.entity.SenDht11PK;
import com.springmvc.entity.SenMach;
import com.springmvc.entity.SenMod;
import com.springmvc.entity.SenRespLog;
import com.springmvc.entity.SenRespLogPK;
import com.springmvc.service.CustomerService;
import com.springmvc.service.SenDht11Service;
import com.springmvc.service.SenMachService;
import com.springmvc.service.SenModService;
import com.springmvc.service.SenRespLogService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SenMachService senMachService;
	
	@Autowired
	private SenDht11Service senDht11PKService;
	
	@Autowired
	private SenRespLogService senRespLogService;
	
	@Autowired
	private SenModService senModService;

	@GetMapping("/list")
	public String listCustomers(Model theModel) {
		List<Customer> theCustomers = customerService.getCustomers();
		theModel.addAttribute("customers", theCustomers);
		senMachService.findAll();
		
		SenMach entity = new SenMach();
		entity.setIp("12421");
		entity.setMachName("test");
		senMachService.create(entity);
		
		SenMod senMod = new SenMod();
		senMod.setMachCode("123");
		senMod.setMachName("test");
		senModService.create(senMod);
		
		SenDht11PK senDht11PK = new SenDht11PK();
		senDht11PK.setSenMach(entity);
		senDht11PKService.findAll();
		SenDht11 senDht11 = new SenDht11();
		senDht11.setHumidity(BigDecimal.ZERO);
		senDht11.setTempCal(BigDecimal.ZERO);
		senDht11.setTempFah(BigDecimal.ZERO);
		senDht11.setSenDht11PK(senDht11PK);
		
		senDht11PKService.create(senDht11);
		
		SenRespLogPK senRespLogPK = new SenRespLogPK();
		senRespLogPK.setSenMach(entity);
		
		//將資料寫入
		SenRespLog senRespLog = new SenRespLog();
		senRespLog.setSenRespLogPK(senRespLogPK);
		senRespLog.setSucStatus(true);
		senRespLog.setRespMessage("test");
		senRespLogService.create(senRespLog);
		
		senRespLogService.createRespLog(entity, true, "test");
		
		return "list-customers";
	}

	@GetMapping("/showForm")
	public String showFormForAdd(Model theModel) {
		Customer theCustomer = new Customer();
		theModel.addAttribute("customer", theCustomer);
		return "customer-form";
	}

	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {
		customerService.saveCustomer(theCustomer);
		return "redirect:/customer/list";
	}

	@GetMapping("/updateForm")
	public String showFormForUpdate(@RequestParam("customerId") int theId, Model theModel) {
		Customer theCustomer = customerService.getCustomer(theId);
		theModel.addAttribute("customer", theCustomer);
		return "customer-form";
	}

	@GetMapping("/delete")
	public String deleteCustomer(@RequestParam("customerId") int theId) {
		// customerService.delete(theId);
		return "redirect:/customer/list";
	}
}
