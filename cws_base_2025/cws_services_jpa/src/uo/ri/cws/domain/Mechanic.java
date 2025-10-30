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
@Table(name = "TMECHANICS")
public class Mechanic extends BaseEntity {
    // natural attributes
    @Column(unique = true)
    private String nif;
    private String surname;
    private String name;

    // accidental attributes
    @OneToMany(mappedBy = "mechanic")
    private Set<WorkOrder> assigned = new HashSet<>();
    @OneToMany(mappedBy = "mechanic")
    private Set<Intervention> interventions = new HashSet<>();

    Mechanic() {
    }

    public Mechanic(String nif, String surname, String name) {
        ArgumentChecks.isNotBlank(name);
        ArgumentChecks.isNotBlank(surname);
        ArgumentChecks.isNotBlank(nif);

        this.nif = nif;
        this.surname = surname;
        this.name = name;
        ;
    }

    public Mechanic(String nif) {
        this(nif, "no-surname", "no-name");
    }

    public Set<WorkOrder> getAssigned() {
        return new HashSet<>(assigned);
    }

    Set<WorkOrder> _getAssigned() {
        return assigned;
    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    Set<Intervention> _getInterventions() {
        return interventions;
    }

    public String getNif() {
        return nif;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Mechanic [nif=" + nif + ", surname=" + surname + ", name="
                + name + "]";
    }

}
