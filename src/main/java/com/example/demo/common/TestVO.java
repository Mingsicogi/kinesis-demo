package com.example.demo.common;

import lombok.Data;

import java.util.UUID;

@Data
public class TestVO {
    private String name;
    private String age;
    private String address;
    private String subject;
    private String phone;
    private String gender;

    public TestVO() {
        this.name = "name = " +UUID.randomUUID().toString();
        this.age = "age = " +UUID.randomUUID().toString();
        this.address = "address = " +UUID.randomUUID().toString();
        this.subject = "subject = " +UUID.randomUUID().toString();
        this.phone = "phone = " +UUID.randomUUID().toString();
        this.gender = "gender = " + UUID.randomUUID().toString();
    }
}
