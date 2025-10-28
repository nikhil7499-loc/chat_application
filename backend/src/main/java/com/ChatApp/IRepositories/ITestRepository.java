package com.ChatApp.IRepositories;

import com.ChatApp.Entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITestRepository extends JpaRepository<Test, Integer> {
    // No need to declare findById, save, findAll, deleteById
    // JpaRepository already provides them
}
