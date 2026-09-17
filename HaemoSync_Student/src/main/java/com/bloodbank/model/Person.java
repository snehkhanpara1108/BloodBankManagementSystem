package com.bloodbank.model;

public abstract class Person implements Identifiable {
    private final String id;
    private String name;
    private String phone;

    protected Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String getId() { return id; }

    public String getName() { return name; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    public abstract String getType();
}
