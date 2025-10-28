package com.ChatApp.IBusinessAccessLayer;

import com.ChatApp.Entities.Test;
import java.util.List;

public interface ITestBAL {

    List<Test> getAllTests();

    Test getTestById(Integer id);

    Test saveTest(Test entity);

    void deleteTest(Integer id);

    void deleteAllTest();
}
