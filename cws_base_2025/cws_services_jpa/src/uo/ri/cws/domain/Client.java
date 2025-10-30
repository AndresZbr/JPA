package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCLIENTS")
public class Client extends BaseEntity {
    @Column(unique = true)
    private String nif;
    private String name;
    private String surname;
    private String email;
    private String phone;
    @Embedded
    private Address address;

    // accidental attributes
    @OneToMany(mappedBy = "Client")
    private Set<Vehicle> vehicles = new HashSet<>();
    @OneToMany(mappedBy = "Client")
    private Set<PaymentMean> payments = new HashSet<>();

    Client() {
    }

    public Client(String nif, String name, String surname) {
        this(nif, name, surname, "no-email", "no-phone",
                new Address("no-street", "no-city", "no-zipcode"));
    }

    public Client(String nif, String name, String surname, String email,
            String phone, Address address) {
        ArgumentChecks.isNotNull(address);
        ArgumentChecks.isNotBlank(name);
        ArgumentChecks.isNotBlank(surname);
        ArgumentChecks.isNotBlank(nif);
        ArgumentChecks.isNotBlank(email);
        ArgumentChecks.isNotBlank(phone);

        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Client(String nif) {
        this(nif, "no-name", "no-surname", "no-email", "no-phone",
                new Address("no-street", "no-city", "no-zipcode"));
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    Set<Vehicle> _getVehicles() {
        return vehicles;
    }

    public Set<PaymentMean> getPaymentMeans() {
        return new HashSet<>(payments);
    }

    Set<PaymentMean> _getPaymentMeans() {
        return payments;
    }

    public String getNif() {
        return nif;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Client [nif=" + nif + ", name=" + name + ", surname=" + surname
                + ", email=" + email + ", phone=" + phone + ", address="
                + address + "]";
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
