package com.example.MongoData.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.MongoData.Model.ExecClass;
@Repository
public interface DemoRepository extends MongoRepository<ExecClass, String>
{
}
