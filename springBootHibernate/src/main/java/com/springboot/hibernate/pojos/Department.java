package com.springboot.hibernate.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * NOTE : @Column annotation is not mandatory. If we don't specify it, then the actual property name will be used as the column name.
 */
@Entity
@Table(name="Department Table")
public class Department {

	@Id
	@Column(name="DepartMent ID")
	int id;
	
	@Column(name="Department Name")
	String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
