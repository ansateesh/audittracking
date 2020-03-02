package com.aholdusa.am.audittracking.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aholdusa.am.audittracking.dao.MessageDAO;
import com.aholdusa.am.audittracking.entity.Messages;

@Service("messageService")
public class MessageServiceImpl extends AMServiceImpl<Messages> implements
		MessageService {

	@Autowired
	private MessageDAO dao;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private MessageDAO messageDao;

	public List findByJsonObject(String jsonString) {
		return null;
	}
}