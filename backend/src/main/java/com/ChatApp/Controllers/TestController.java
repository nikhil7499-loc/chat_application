package com.ChatApp.Controllers;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import com.ChatApp.IBusinessAccessLayer.ITestBAL;
import com.ChatApp.Entities.Test;
import com.ChatApp.Models.Requests.TestRequest;
import com.ChatApp.Models.Responses.TestResponse;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ITestBAL testBAL;

    public TestController(ITestBAL testBAL) {
        this.testBAL = testBAL;
    }

    // Simple health check
    @GetMapping("/ping")
    public String ping() {
        return "Server is running ✅";
    }

    // GET all rows
    @GetMapping
    public List<TestResponse> getAllTests() {
        return testBAL.getAllTests()
                      .stream()
                      .map(e -> new TestResponse(e.getId(), e.getVal()))
                      .collect(Collectors.toList());
    }

    // GET by ID
    @GetMapping("/{id}")
    public TestResponse getTestById(@PathVariable Integer id) {
        Test entity = testBAL.getTestById(id);
        if (entity == null) return null;
        return new TestResponse(entity.getId(), entity.getVal());
    }

    // POST / create
    @PostMapping
    public TestResponse createTest(@RequestBody TestRequest request) {
        Test entity = new Test();
        entity.setVal(request.getVal());
        Test saved = testBAL.saveTest(entity);
        return new TestResponse(saved.getId(), saved.getVal());
    }

    // PUT / update
    @PutMapping("/{id}")
    public TestResponse updateTest(@PathVariable Integer id, @RequestBody TestRequest request) {
        Test entity = testBAL.getTestById(id);
        if (entity == null) return null;
        entity.setVal(request.getVal());
        Test updated = testBAL.saveTest(entity);
        return new TestResponse(updated.getId(), updated.getVal());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteTest(@PathVariable Integer id) {
        testBAL.deleteTest(id);
    }


    // DELETE ALL
    @DeleteMapping("/all")
    public void deleteAllTest(){
        testBAL.deleteAllTest();
    }
}
