package com.springboot.hibernate.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.hibernate.pojos.Department;
import com.springboot.hibernate.pojos.Employee;

@Repository
public class EmployeeDao_Transactional_annotation_tutorial {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DepartmentDao departementDao;
	
	@Autowired
	EntityManager entiryManager;
	
	/*
	 * NOTE: @Transactional is Spring feature not from Hibernate.
	 * 
	 * 
	 * Spring will implicitly create an @Around AOP for the methods which are declared with @Transaction annotation. The AOP will something like as follows,
	 * 		Session session = sessionFactory.getCurrentSession();
	 * 		EntityTransaction transaction = entiryManager.getTransaction(); // if propagation is 'Requires_new' otherwise it will be 'EntityTransaction transaction = session.getTransaction().begin()'
	 * 		try {
	 * 			***** here it will call the actual method 'saveEmployee' **************
	 *    		transaction.commit();
	 *    	}
	 *    	catch(Exception e) {
	 *    		transaction.rollback();
	 *    	}
	 *    
	 *  It will automatically create transaction and do commit. we need not manually create a transaction and call 'commit()'. One more advantage it has is, it will also check if there is any 
	 *  exception is thrown during execution the of the function and if there is any exception occured, then it will do 'rollback()' as well.
	 *  
	 *  
	 *  @Transactional also accepts 2 parameters as well like as follow,
	 *  1) Propagation
	 *  2) Isolation
	 *  
	 *  Propogation
	 *  ------------
	 *  Propogation can be assigned following values,
	 *  		a) Required
	 *  			When the 'saveEmployee' is called, it will check if there is a @Transaction declared on any of the functions in the call hierarchy of this function. 
	 *  				If present, then it will use the same transaction. If
	 *  				If not present, then it will begin a new transaction.
	 *  		b) Requires_New
	 *          When the 'saveEmployee' is called, it will not check, if there is a @Transaction declared on any of the functions in the call hierarchy of this function.
	 *          		If present or not present, irrespective of it, it will begin a new transaction.
	 *  Example,
	 *  		IN 'saveEmployee' function we are calling one more function outside of this class 'departementDao.SaveDepartment(obj)' which has '@Transactional(propagation=Propagation.REQUIRES_NEW)' in
	 *  'DepartmentDao' hence, it will create a new transaction, for executing 'departementDao.SaveDepartment(obj)' so that if there are any exception in this function, the rollback will be done 
	 *  only on actions performed in 'departementDao.SaveDepartment(obj)' i.e. saved 'Department' object will be rolled back,  the contents of 'saveEmployee' function will not be rollback i.e. saved 
	 *  'Employee' object will be rolled back because 'saveEmployee' is executed in a transaction and 'departementDao.SaveDepartment(obj)' is executed in another transaction. basically, rollback 
	 *  is called only on the current transaction.
	 *  		Consider, if 'departementDao.SaveDepartment(obj)' function didn't have '@Transactional(propagation=Propagation.REQUIRES_NEW)', then it would have executed in the same transaction in which
	 *  'saveEmployee' is running so any exception in 'departementDao.SaveDepartment(obj)' will rollback the current transaction which involves contents of 'saveEmployee' as well hence 
	 *  by default, it will be taken as '@Transactional(propagation=Propagation.REQUIRED)'. Hence both 'Department' and 'Employee' object saved will be rolled back even though there were no 
	 *  exceptions in 'saveEmployee'
	 *  
	 *  Isolation
	 *  ---------
	 *  Isolation can be assigned following values,
	 *  	a) Read Uncommitted
	 *  		This will allow Dirty Reads
	 *  b) Read Committed
	 *  		This will not allow Dirty Reads
	 *  c) Repeatable Read
	 *  		If a row is read twice in a transaction, the result will always be same.
	 *  d) Serializable
	 *  		All the transactions will be executed in sequential order.
	 *  
	 *  
	 *  What is Dirty Reads?
	 *  		thread 1   thread 2      
		      |         |
		    write(x)    |
		      |         |
		      |        read(x)
		      |         |
		    rollback    |
		      v         v 
		           value (x) is now dirty (incorrect)
	 * 	
	 * 
	 * 
	 */
	@Transactional
	public void saveEmployee(Employee e) {
		Session session = sessionFactory.openSession();
		Session session1 = sessionFactory.getCurrentSession(); // It is always better to use current session rather than opening a new session.
		//Transaction transaction = session.beginTransaction();
		session1.save(e);
		session1.persist(e);	 // persist will also do the same thing as that of 'save()' but minor difference.
		//transaction.commit();
		//transaction.rollback();	
		
		Department obj = new Department();
		obj.setId(1);
		obj.setName("R AND D");
		departementDao.SaveDepartment(obj);
		
	}
	
	
	@Transactional
	public void updateEmployee(Employee e) {
		Session session = sessionFactory.getCurrentSession();
		Department obj = new Department();
		obj.setId(1);
		obj.setName("R AND D");
		
		/*
		 * The below code will run in a separate new transaction since 'departementDao.SaveDepartment(obj)' in 'DepartmentDao' has '@Transactional(propagation=Propagation.REQUIRED)'
		 */
		departementDao.SaveDepartment(obj);
		
		/*
		 * *************************** IMPORTANT *************************************************
		 * The below code will not run in a separate new transaction even though '@Transactional(propagation=Propagation.REQUIRED)' is defined on 'public void SaveDepartment(Department obj)'!!!! ?
		 * Actually, internally like how it will create a new @Around AOP for 'saveEmployee' and 'updateEmployee' method. samewise it will create a new @AroundAOP for 'SaveDepartment' method also,
		 * but in previous case, 'SaveDepartment' was peresent in different class say 'DepartmentDao' so spring was able create AOP for it. but in this case function is within the same class so, 
		 * spring cannot create AOP. So this will execute in current transaction only instead of beginning new one.
		 */
		SaveDepartment(obj);
		
		
	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void SaveDepartment(Department obj) {
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
	}
	
	
}
