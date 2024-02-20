package org.example.request;

public class SomeRequest {

    private final String name;
    private final String position;
    private final Double annualSalary;

    public SomeRequest(String name,
                             String position,
                             Double annualSalary) {
        this.name = name;
        this.position = position;
        this.annualSalary = annualSalary;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public Double getAnnualSalary() {
        return annualSalary;
    }

    @Override
    public String toString() {
        return "SomeRequest{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", annualSalary=" + annualSalary +
                '}';
    }
}