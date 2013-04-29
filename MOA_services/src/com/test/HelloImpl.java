package com.test;

import javax.jws.WebService;

@WebService(endpointInterface = "com.test.Hello")
public class HelloImpl implements Hello
{
	public String helloName(String name) 
	{
		return "Hello "+name;
	}
}
