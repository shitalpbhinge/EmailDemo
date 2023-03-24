package com.example.MongoData.Model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document
public class ExecClass 
{
	@Field
	private   String uuid;
	@Field
	private    String version;
	@Field
	private    String name;
	@Field
	private List<StatusList> statuslist;
	private StatusList latest;
	    
	public ExecClass(String version2, String name2, String uuid2, Object object, Object statusListDocs) 
	{
			// TODO Auto-generated constructor stub
	}
	public ExecClass() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	public ExecClass(String uuid, String version, String name, List<StatusList> statuslist, StatusList latest) 
	{
		super();
		this.uuid = uuid;
		this.version = version;
		this.name = name;
		this.statuslist = statuslist;
		this.latest = latest;
	}
	public ExecClass(String name2, String version2, String uuid2, StatusList list1) 
	{
		// TODO Auto-generated constructor stub
	}

	public String getUuid() 
	{
		return uuid;
	}
	public void setUuid(String uuid) 
	{
		this.uuid = uuid;
	}

	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getVersion() 
	{
		return version;
	}
	public void setVersion(String version) 
	{
		this.version = version;
	}
	public List<StatusList> getStatuslist() 
	{
		return statuslist;
	}
	public void setStatuslist(List<StatusList> statuslist) 
	{
		this.statuslist = statuslist;
	}

	public StatusList getLatest() 
	{
		return latest;
	}

	public void setLatest(StatusList latest) 
	{
		this.latest = latest;
	}	
}
