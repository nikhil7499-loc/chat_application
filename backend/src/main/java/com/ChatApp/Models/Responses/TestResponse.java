package com.ChatApp.Models.Responses;

public class TestResponse {
    private Integer id;
    private String val;

    // Constructor
    public TestResponse(Integer id, String val) {
        this.id = id;
        this.val = val;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
