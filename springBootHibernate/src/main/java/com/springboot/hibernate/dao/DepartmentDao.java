package com.springboot.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.hibernate.pojos.Department;

@Component
public class DepartmentDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void SaveDepartment(Department obj) {
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
	}
}
