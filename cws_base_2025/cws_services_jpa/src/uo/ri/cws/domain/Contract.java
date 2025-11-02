package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCONTRACTS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "startDate", "mechanic_id" }) })
public class Contract {
    public enum ContractState {
        IN_FORGE, TERMINATED
    }

    // natural attributes
    private double annualBaseSalary;
    private LocalDate signingDate;
    private LocalDate endDate;
    private double taxRate;
    private double settlement;
    private ContractState state = ContractState.IN_FORGE;

    // accidental attributes
    @ManyToOne
    private Mechanic mechanic;
    @ManyToOne
    private ContractType type;
    @ManyToOne
    private ProfessionalGroup group;
    @OneToMany(mappedBy = "payroll")
    private Set<Payroll> payrolls = new HashSet<>();

    public Contract() {
    }

    public Contract(Mechanic mechanic, ContractType type,
            ProfessionalGroup group, LocalDate signingDate, LocalDate endDate,
            double annualSalary) {

        ArgumentChecks.isNotNull(mechanic);
        ArgumentChecks.isNotNull(type);
        ArgumentChecks.isNotNull(group);
        ArgumentChecks.isNotNull(signingDate);
        ArgumentChecks.isTrue(annualSalary >= 0);

        boolean isFixedTerm = "FIXED_TERM".equals(type.getName());

        if (isFixedTerm) {
            ArgumentChecks.isNotNull(endDate);
            ArgumentChecks.isTrue(signingDate.isBefore(endDate));
        } else {
            endDate = null; // IGNORAR endDate para contratos indefinidos
        }

        // Ajustar fechas
        this.signingDate = signingDate.withDayOfMonth(1);

        if (isFixedTerm) {
            this.endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());
        } else {
            this.endDate = null;
        }

        this.annualBaseSalary = annualSalary;
        this.mechanic = mechanic;
        this.group = group;
        this.type = type;
        this.state = ContractState.IN_FORGE;
        this.settlement = 0.0;

        this.taxRate = calculateTaxRate(annualSalary);

        mechanic.getContractInForce()
                .ifPresent(oldContract -> oldContract.terminate(
                        signingDate.minusDays(1)));

        // Enlaces
        Associations.Binds.link(mechanic, this);
        Associations.Categorizes.link(group, this);
        Associations.Defines.link(type, this);
    }

    public Contract(Mechanic mechanic, ContractType type,
            ProfessionalGroup group, LocalDate signingDate,
            double annualSalary) {
        this(mechanic, type, group, signingDate, null, annualSalary);
    }

    public LocalDate getSigningDate() {
        return signingDate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public ContractType getType() {
        return type;
    }

    public ProfessionalGroup getGroup() {
        return group;
    }

    public double getAnnualBaseSalary() {
        return annualBaseSalary;
    }

    public LocalDate getStartDate() {
        return signingDate.withDayOfMonth(1);
    }

    public LocalDate getEndDate() {
        if (endDate == null)
            return null;
        return endDate.plusMonths(1)
                      .withDayOfMonth(1)
                      .minusDays(1);
    }

    public Double getSettlement() {
        return settlement;
    }

    public ContractType getContractType() {
        return type;
    }

    public ProfessionalGroup getProfessionalGroup() {
        return group;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public Set<Payroll> getPayrolls() {
        return new HashSet<>(payrolls);
    }

    Set<Payroll> _getPayrolls() {
        return payrolls;
    }

    public void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public void _setGroup(ProfessionalGroup group) {
        this.group = group;
    }

    public void _setType(ContractType type) {
        this.type = type;
    }

    public boolean isInForce() {
        return state.equals(ContractState.IN_FORGE);
    }

    public boolean isTerminated() {
        return state.equals(ContractState.TERMINATED);
    }

    public void terminate(LocalDate endDate) {
        ArgumentChecks.isNotNull(endDate);
        ArgumentChecks.isTrue(getStartDate().isBefore(endDate));
        if (isTerminated())
            throw new IllegalStateException();

        this.endDate = endDate;
        this.state = ContractState.TERMINATED;

        int monthsWorked = this.payrolls.size();
        int yearsWorked = monthsWorked / 12;

        double dailyGrossSalary = this.annualBaseSalary / 365.0;
        double compDays = this.type.getCompensationDaysPerYear();

        this.settlement = yearsWorked * dailyGrossSalary * compDays;
    }

    public void _addPayroll(Payroll payroll) {
        Associations.Generates.link(this, payroll);
    }

    // Calcula la tasa de impuestos en formato decimal según el salario anual.
    private double calculateTaxRate(double annualSalary) {
        if (annualSalary <= 12450)
            return 0.19;
        else if (annualSalary <= 20200)
            return 0.24;
        else if (annualSalary <= 35200)
            return 0.30;
        else if (annualSalary <= 60000)
            return 0.37;
        else if (annualSalary <= 300000)
            return 0.45;
        else
            return 0.47;
    }

    @Override
    public String toString() {
        return "Contract [annualBaseSalary=" + annualBaseSalary
                + ", signingDate=" + signingDate + ", endDate=" + endDate
                + ", taxRate=" + taxRate + ", settlement=" + settlement
                + ", state=" + state + "]";
    }

}