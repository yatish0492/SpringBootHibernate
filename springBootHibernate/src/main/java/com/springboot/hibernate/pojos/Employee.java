package com.springboot.hibernate.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * NOTE : @Column annotation is not mandatory. If we don't specify it, then the actual property name will be used as the column name.
 */
@Entity
@Table(name="Employee")
public class Employee {
	
	@Id
	@Column(name="employee id")
	private Integer id;
	
	@Column(name="employee name")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
