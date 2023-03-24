package com.example.MongoData.Controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoData.Model.ExecClass;
import com.example.MongoData.Model.StatusList;
import com.example.MongoData.Repository.DemoRepository;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

@RestController
@RequestMapping("/api")
public class DemoController 
{

			@Autowired
		    DemoRepository demoRepository;
		   
		    @Autowired
		    MongoTemplate mongoTemplate;
		    
		    @GetMapping("/collectionsEndingWithExec")
		    public List<String> getAllCollectionsEndingWithExec() 
		    {
		        List<String> collections = mongoTemplate.getCollectionNames().stream()
		                                    .filter(name -> name.endsWith("exec"))
		                                    .collect(Collectors.toList());
		        return collections;
		    }
		    @GetMapping("getCollection/{collection}")
		    public List<Document> getCollection(@PathVariable String collection) {
		        MongoCursor<Document> cursor = mongoTemplate.getCollection(collection).find().iterator();

		        List<Document> data = StreamSupport.stream(Spliterators.spliteratorUnknownSize(cursor, Spliterator.ORDERED), false)
		                .collect(Collectors.toList());

		        cursor.close();

		        return data;
		    }
		   @GetMapping("/collect/{collection}")
		    public List<ExecClass> getdata(@PathVariable String collection)
		    {
				    MongoCollection<Document> col= mongoTemplate.getCollection(collection);
				    List<ExecClass> data =new ArrayList<ExecClass>();
				    for(Document coll:col.find()) 
				    {
						    String uuid=coll.getString("uuid");
						    String version = coll.getString("version");
						    String name = coll.getString("name");
						    Query query = new Query();
						    Object statusListDocs = coll.get("statusList");
							//  ExecClass exec = new ExecClass(version,name,uuid,null,statusListDocs);
						    List<StatusList> List = ((Collection<Document>) statusListDocs).stream()
							
						   .map(doc -> new StatusList(doc.getString("stage"), doc.getDate("createdOn")))
						    .collect(Collectors.toList());
						    ExecClass exec = new ExecClass(version,name,uuid,null, List);
						    exec.setVersion(version);
						    exec.setUuid(uuid);
						    exec.setName(name);
						    exec.setStatuslist(List);
						     data.add(exec);
				    }
				    return data;
		    }
		    /*@GetMapping("/collect/{collection}")
		    public List<ExecClass> getData(@PathVariable String collection) 
		    {
		        MongoCollection<Document> col = mongoTemplate.getCollection(collection);
		        
		        List<ExecClass> data = StreamSupport.stream(col.find().spliterator(), false)
		                .map(doc -> {
		                	 
		                    String uuid = doc.getString("uuid");
		                    String version = doc.getString("version");
		                    String name = doc.getString("name");
		                    
		                    List<StatusList> statusList = ((List<Document>) doc.get("statusList")).stream()
		                            .map(statusDoc -> new StatusList(statusDoc.getString("stage"), statusDoc.getDate("createdOn")))
		                            .collect(Collectors.toList());
		                    
		                    return new ExecClass(version, name, uuid, null, statusList);
		                })
		                .collect(Collectors.toList());
		        
		        return data;
		    }*/
		    /*@GetMapping("/collect/{collection}")
		    public List<ExecClass> getData(@PathVariable String collection) 
		    {
		        MongoCollection<Document> col = mongoTemplate.getCollection(collection);
		        
		        List<ExecClass> data = new ArrayList<>();
		        
		        for(Document doc : col.find()) {
		            String uuid = doc.getString("uuid");
		            String version = doc.getString("version");
		            String name = doc.getString("name");
		            
		            List<StatusList> statusList = ((List<Document>) doc.get("statusList")).stream()
		                    .map(statusDoc -> new StatusList(statusDoc.getString("stage"), statusDoc.getDate("createdOn")))
		                    .collect(Collectors.toList());
		            
		            ExecClass execClass = new ExecClass(version, name, uuid, null, statusList);
		            execClass.getUuid();
		            execClass.getVersion();
		            execClass.getName();
		            execClass.getStatuslist();
		            data.add(execClass);
		        }
		        
		        return data;
		    }*/
		    @GetMapping("/lateststage/{collection}/{stage}")
		    public List<ExecClass> lateststage(@PathVariable String collection,@PathVariable String stage)
		    {
				    MongoCollection<Document> col = mongoTemplate.getCollection(collection);
				    List<ExecClass> data = new ArrayList<>();
				     for (Document coll : col.find()) 
				     {
				    
						   List<Document> statusListDocs = (List<Document>) coll.get("statusList");
						    List<StatusList> statusList = statusListDocs.stream()
						   .map(doc -> new StatusList(doc.getString("stage"), doc.getDate("createdOn")))
						   .collect(Collectors.toList());
						    StatusList list1 = new StatusList();
						    ExecClass exec1 = new ExecClass();
						    StatusList latest = statusList.stream()
						    		.max(Comparator.comparing(StatusList::getCreatedOn))
						    		.filter(s -> s.getStage().equals(stage))
						    		.orElse(null);
						    if (latest != null) 
						    {
								   	String stagename = latest.getStage();
								    Date latestCreatedOn = latest.getCreatedOn();
								    list1.setCreatedOn(latestCreatedOn);
								    list1.setStage(stagename);
								    String uuid = coll.getString("uuid");
								    String version = coll.getString("version");
								    String name = coll.getString("name");
								    exec1.setUuid(uuid);
								    exec1.setVersion(version);
								    exec1.setName(name);
								    exec1.setLatest(list1);
								    data.add(exec1);
						    } 
					}
				    return data;
		   	}		   
		    @GetMapping("/bystage/{collection}/{stage}")
		    public List<ExecClass> bystage(@PathVariable String collection,@PathVariable String stage)
		    {
		         MongoCollection<Document> col = mongoTemplate.getCollection(collection);
		         List<ExecClass> data = new ArrayList<>();
		         for (Document coll : col.find()) 
		         {
		                String uuid = coll.getString("uuid");
		                String version = coll.getString("version");
		                String name = coll.getString("name");
		                List<Document> statusListDocs = (List<Document>) coll.get("statusList");
		                List<StatusList> statusList = statusListDocs.stream()
		                    .map(doc -> new StatusList(doc.getString("stage"), doc.getDate("createdOn")))
		                    .collect(Collectors.toList());
		                	StatusList list1 = new StatusList();
		                	StatusList filteredStatusList = statusList.stream()
		                        .max(Comparator.comparing(StatusList::getCreatedOn))
		                        .filter(s -> s.getStage().equalsIgnoreCase(stage))
		                        .orElse(null);
		                if (filteredStatusList != null) 
		                {
		                    String stagename = filteredStatusList.getStage();
		                    Date latestCreatedOn = filteredStatusList.getCreatedOn();
		                    list1.setCreatedOn(latestCreatedOn);
		                    list1.setStage(stagename);
		                }
		               ExecClass exec1 = new ExecClass(name, version, uuid,list1);
		               data.add(exec1);
		            }
		        return data;
		    } 
		    @GetMapping("/bystagenames/{collection}/{stage}")
		    public List<ExecClass>sortbystage(@PathVariable String collection,@PathVariable String stage)
		    {
					    MongoCollection<Document> col = mongoTemplate.getCollection(collection);
					    List<ExecClass> data = new ArrayList<>();
					
					     List<Bson> pipeline = Arrays.asList(
					     Aggregates.match(Filters.elemMatch("statusList", Filters.eq("stage", stage))),
					     Aggregates.unwind("$statusList"),
					     Aggregates.match(Filters.eq("statusList.stage", stage)),
					     Aggregates.project(Projections.fields(
					     Projections.include("version", "name", "uuid", "statusList.stage", "statusList.createdOn"),
					     Projections.computed("Stage", "$statusList.stage"),
					     Projections.computed("createdOn", "$statusList.createdOn"))));
					
					     AggregateIterable<Document> result = col.aggregate(pipeline);
					  
					     for (Document doc : result) 
					     {
					    	 
							    String uuid = doc.getString("uuid");
							    String version = doc.getString("version");
							   	String name = doc.getString("name");
							   
							   	String stagename = doc.getString("Stage");
							   	Date CreatedOn = doc.getDate("createdOn");
							
							    StatusList list1 = new StatusList(stagename, CreatedOn);
							    ExecClass exec1 = new ExecClass( name,version, uuid, list1);
							    exec1.setUuid(uuid);
							    exec1.setVersion(version);
							    exec1.setName(name);
						
							    exec1.setLatest(list1);
							    data.add(exec1);
					     }
					     return data;
		    	}
		    

}
