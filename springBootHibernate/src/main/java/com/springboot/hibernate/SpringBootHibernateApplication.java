package com.springboot.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.springboot.hibernate.dao.EmployeeDao_Transactional_annotation_tutorial;
import com.springboot.hibernate.pojos.Employee;

/*
 * How to create a springBoot project with Hibernate?
 * While creating the springBoot project select 'JPA', there is no Hibernate dependency explicitly. 'JPA' dependency will fetch you the Hibernate 5.x dependency. Along with Hibernate we have to 
 * get the corresponding DB dialect by selecting corresponding DB like SQL, MongoDB etc.
 * 
 * So once we create if we run the spring boot application by running this java program. the tomcat will run on 8080 port?
 * NO!! If we have the JPA dependency in the pom.xml, then we have to mandatorily specify the hibernate properties like URL,username,password etc. Otherwise even the tomcat will also not start with
 * default port 8080. It will give an error even if we try to run it as follows,
 * 	***************************
	APPLICATION FAILED TO START
	***************************
	
	Description:
	
	Failed to auto-configure a DataSource: 'spring.datasource.url' is not specified and no embedded datasource could be auto-configured.
	
	Reason: Failed to determine a suitable driver class

 * 
 * In Stand-alone hibernate, we specify the configuration details in the xml file and we will create a 'Configuration' object in java and load that xml file, how do we do it here?
 * We actually provide all those configuration details in the 'appliation.properties' present in the 'src/main/resources'
 * 
 * 
 * 
 *  
 * 
 */
@EnableJpaRepositories("com.springboot")
@ComponentScan("com.springboot")
@SpringBootApplication
public class SpringBootHibernateApplication implements CommandLineRunner{

	
	@Autowired
	EmployeeDao_Transactional_annotation_tutorial employeeDao;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootHibernateApplication.class, args);
		
	}
	
	public void run(String[] args) {
		Employee obj  = new Employee();
		obj.setId(1);
		obj.setName("Yatish");
		employeeDao.saveEmployee(obj);
	}
	
	
}
