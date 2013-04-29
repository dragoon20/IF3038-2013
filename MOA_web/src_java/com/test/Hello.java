package com.test;

import javax.jws.WebService;

@WebService
public interface Hello 
{
	public String helloName(String name);
}
