package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import uo.ri.util.assertion.ArgumentChecks;

public class ContractType {

    // natural attributes
    private double compensationDaysPerYear;
    private String name;

    // accidental attributes
    private Set<Contract> contracts = new HashSet<>();

    public ContractType(String name, double d) {
        ArgumentChecks.isNotBlank(name);
        ArgumentChecks.isTrue(d >= 0);

        this.compensationDaysPerYear = d;
        this.name = name;
    }

    public Set<Contract> getContracts() {
        return new HashSet<>(contracts);
    }

    Set<Contract> _getContracts() {
        return contracts;
    }

    public double getCompensationDaysPerYear() {
        return compensationDaysPerYear;
    }

    public String getName() {
        return name;
    }

}
