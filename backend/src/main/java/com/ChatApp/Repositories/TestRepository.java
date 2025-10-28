package com.ChatApp.Repositories;

import org.springframework.stereotype.Repository;
import com.ChatApp.Entities.Test;
import com.ChatApp.IRepositories.ITestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository
public class TestRepository {

    private final ITestRepository jpaRepo;

    @Autowired
    public TestRepository(ITestRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    public List<Test> findAll() {
        return jpaRepo.findAll();
    }

    public Test findById(Integer id) {
        return jpaRepo.findById(id).orElse(null); // Optional handled here
    }

    public Test save(Test entity) {
        return jpaRepo.save(entity);
    }

    public void deleteById(Integer id) {
        jpaRepo.deleteById(id);
    }

    public void deleteAll(){
        jpaRepo.deleteAll();
    }
}
