package com.aholdusa.am.audittracking.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aholdusa.am.audittracking.service.AMService;
import com.aholdusa.am.audittracking.service.StoreService;

/**
 * Controller for Store. Uses the abstract controller methods to handle the
 * CRUD/Fetch operations on stores.
 * 
 * @author nusxr42
 * 
 * @see com.aholdusa.core.base.controller.AbstractController.java
 */
@Controller
@RequestMapping("store")
public class StoreController extends AMController {
	@Autowired
	private StoreService storeService;
	private Logger logger = Logger.getLogger(this.getClass());
	@Override
	public AMService getService() {
		// TODO Auto-generated method stub
		return this.storeService;
	}

}
