package com.aholdusa.am.audittracking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aholdusa.am.audittracking.entity.AMError;
import com.aholdusa.am.audittracking.service.AMService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AMController<T> {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Autowired
	//private AMService<T> service;

	
	public abstract AMService<T> getService() ;

//	public abstract void setService(AMService<T> service) ;

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void findById(@PathVariable final Long id,
			HttpServletResponse response) {
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			writer.print(this.getService().findById(id));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<T> findAll() {
		return this.getService().findAll();
	}

	@ResponseBody
	@RequestMapping(value = "/find{clazz}", method = RequestMethod.GET)
	public void find(@PathVariable final T clazz,
			final HttpServletRequest request, HttpServletResponse response) {
		String returnString = "";
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();

		try {
			Object bean = this.getService().getClazz().newInstance();
			Enumeration<String> parameters = request.getParameterNames();
			while (parameters.hasMoreElements()) {
				String name = parameters.nextElement();
				logger.info("Setting bean property : " + name + " to "
						+ request.getParameter(name));
				BeanUtils.setProperty(bean, name, request.getParameter(name));
			}
			// logger.info("Newly constructed bean: " + bean.toString());
			if (bean != null) {
				returnString = gson
						.toJson(this.getService().findByObject((T) bean));
			}
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		}
		returnString = new AMError(502, "No records found", "").toString();
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/find{clazz}ById/{id}", method = RequestMethod.GET)
	public void findById(@PathVariable final T clazz,
			@PathVariable final long id, final HttpServletRequest request,
			HttpServletResponse response) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		String returnString = gson.toJson(this.getService().findById(id));
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			writer.print(returnString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/find{clazz}FromObject", method = RequestMethod.POST)
	public void findFromObject(@PathVariable final T clazz,
			@RequestBody final Object entity, final HttpServletRequest request,
			HttpServletResponse response) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();

		List<T> foundEntities = new ArrayList<T>();
		if (entity == null) {
			Long id = Long.parseLong(request.getParameter("id"));
			if (id != null && id > 0)
				foundEntities.add((T) this.getService().findById(id));

		} else {
			LinkedHashMap<String, LinkedHashMap> objList = (LinkedHashMap<String, LinkedHashMap>) entity;
			for (Entry<String, LinkedHashMap> s : objList.entrySet()) {
				T object = gson.fromJson(gson.toJson(s.getValue()),
						(Class<T>) clazz);
				foundEntities = this.getService().findByObject((T) entity);

			}
		}

		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			writer.print(foundEntities);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	protected void afterFindById(final Long id, final T foundEntity) {
		return;
	}

	protected void beforeFindAll() {
		return;
	}

	protected List<T> implementFindAll() {
		return this.getService().findAll();
	}

	protected void afterFindAll(final List<T> foundEntities) {
		return;
	}

	protected void beforeFind(final HttpServletRequest request) {
		return;
	}

	protected void afterFind(final List<T> entities) {
		return;
	}

	protected void beforeFindOne(final HttpServletRequest request) {
		return;
	}

}