package com.hitzd.his.sso;

import java.util.Date;
import com.hitzd.his.Beans.frame.User;

public class SessionObject 
{
	private User user = null;
	private Date lastAccessTime = null;
	private String sessionID = null;
	
	public void access()
	{
		lastAccessTime = null;
		lastAccessTime = new Date();
	}
	
	public Date getLastAccessTime()
	{
		return lastAccessTime;
	}
	
	public User getUser() 
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public SessionObject(User user, String sessionID)
	{
		lastAccessTime = new Date();
		setUser(user);
		setSessionID(sessionID);
	}
	
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID()
	{
		if (sessionID != null)
		{
			return sessionID;
		}
		else
			return "";
	}
}
