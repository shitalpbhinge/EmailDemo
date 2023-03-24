package com.example.MongoData.Model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

public class StatusList 
{	
	private String stage;

	private Date createdOn;
	
	public String getStage() 
	{
		return stage;
	}
	public void setStage(String stage) 
	{
		this.stage = stage;
	}
	public Date getCreatedOn() 
	{
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) 
	{
		this.createdOn = createdOn;
	}
	public StatusList(String stage, Date createdOn) 
	{
		super();
		this.stage = stage;
		this.createdOn = createdOn;
	}
	public StatusList() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() 
	{
		return "StatusList [stage=" + stage + ", createdOn=" + createdOn + "]";
	}
	
	
}
