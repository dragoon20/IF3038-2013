package com.helper;

import javax.servlet.http.HttpSession;

public class GeneralHelper 
{
	public static Integer isLogin(HttpSession session, String token)
	{
		if (session.getAttribute(token)!=null)
		{
			return (Integer)session.getAttribute(token);
		}
		return -1;
	}
}
