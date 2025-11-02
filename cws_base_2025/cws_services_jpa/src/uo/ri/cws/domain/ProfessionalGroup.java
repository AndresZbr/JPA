package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TPROFESSIONALGROUPS")
public class ProfessionalGroup extends BaseEntity {
    // natural attributes
    @Column(unique = true)
    private String name;
    private double trienniumSalary;
    private double productivityRate;

    // accidental attributes
    @OneToMany(mappedBy = "professionalGroup")
    private Set<Contract> contracts = new HashSet<>();

    ProfessionalGroup() {
    }

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

    @Override
    public String toString() {
        return "ProfessionalGroup [name=" + name + ", trienniumSalary="
                + trienniumSalary + ", productivityRate=" + productivityRate
                + "]";
    }

}