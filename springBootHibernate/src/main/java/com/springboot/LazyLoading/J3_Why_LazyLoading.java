package com.springboot.LazyLoading;

public class J3_Why_LazyLoading {

	/*
	 * First of all why do we need to do lazy loading. We can fetch the collections while loading only right rather than loading them at the time of 'get' call to get the collelctions?
	 * Consider a case, where you just need only the name property of an object and you are not accessing any of the collection properties for this logic. When you will do a get object, then 
	 * in that case we don't need all the collection properties which has huge amount of data. Simply loading them in this scenario, where i just want to get the name is in-efficient.
	 */
}
