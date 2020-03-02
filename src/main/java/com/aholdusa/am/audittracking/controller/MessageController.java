package com.aholdusa.am.audittracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aholdusa.am.audittracking.service.AMService;
import com.aholdusa.am.audittracking.service.MessageService;

/**
 * Controller for Lanes. Uses the abstract controller methods to handle the
 * CRUD/Fetch operations on lanes.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.controller.AbstractController.java
 */
@Controller
@RequestMapping("messages")
public class MessageController extends AMController {
	@Autowired
	private MessageService messageService;

	@Override
	public AMService getService() {
		// TODO Auto-generated method stub
		return this.messageService;
	}
}