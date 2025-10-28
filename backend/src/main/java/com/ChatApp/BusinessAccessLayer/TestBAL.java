package com.ChatApp.BusinessAccessLayer;

import org.springframework.stereotype.Service;
import com.ChatApp.IBusinessAccessLayer.ITestBAL;
import com.ChatApp.Entities.Test;
import com.ChatApp.IRepositories.ITestRepository;

import java.util.List;

@Service
public class TestBAL implements ITestBAL {

    private final ITestRepository testRepository;

    public TestBAL(ITestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public List<Test> getAllTests() {
        List<Test> values= testRepository.findAll();
    }

    @Override
    public Test getTestById(Integer id) {
        return testRepository.findById(id).orElse(null);
    }

    @Override
    public Test saveTest(Test entity) {
        return testRepository.save(entity);
    }

    @Override
    public void deleteTest(Integer id) {
        testRepository.deleteById(id);
    }
    
    @Override
    public void deleteAllTest(){
        testRepository.deleteAll();
    }
}
