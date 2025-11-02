package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import uo.ri.util.assertion.ArgumentChecks;

public class ProfessionalGroup {

    private String name;
    private double trienniumSalary;
    private double productivityRate;

    private Set<Contract> contracts = new HashSet<>();

    public ProfessionalGroup(String name, double trienniumSalary,
            double productivityRate) {
        ArgumentChecks.isNotBlank(name);
        ArgumentChecks.isTrue(trienniumSalary >= 0);
        ArgumentChecks.isTrue(productivityRate >= 0);

        this.name = name;
        this.trienniumSalary = trienniumSalary;
        this.productivityRate = productivityRate;
    }

    public Set<Contract> getContracts() {
        return new HashSet<>(contracts);
    }

    Set<Contract> _getContracts() {
        return contracts;
    }

    public String getName() {
        return name;
    }

    public double getTrienniumSalary() {
        return trienniumSalary;
    }

    public double getProductivityRate() {
        return productivityRate;
    }

}
