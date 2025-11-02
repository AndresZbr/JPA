package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCONTRACTYPES")
public class ContractType {

    // natural attributes
    @Column(unique = true)
    private String name;
    private double compensationDaysPerYear;

    // accidental attributes
    @OneToMany(mappedBy = "contractType")
    private Set<Contract> contracts = new HashSet<>();

    public ContractType() {
    }

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

    @Override
    public String toString() {
        return "ContractType [name=" + name + ", compensationDaysPerYear="
                + compensationDaysPerYear + "]";
    }

}
