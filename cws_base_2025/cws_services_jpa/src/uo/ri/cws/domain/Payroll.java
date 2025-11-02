package uo.ri.cws.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

public class Payroll {

    private LocalDate date;
    private double baseSalary;
    private double extraSalary;
    private double productivityEarning;
    private double trienniumEarning;
    private double taxDeduction;
    private double nicDeduction;
    private double totalDeduction;
    private double netSalary;
    private double grossSalary;

    private Contract contract;

    private static final int PAYMENTS_IN_YEAR = 14;
    private static final int PAYROLLS_IN_YEAR = 12;

    public Payroll(Contract c, LocalDate payrollDate) {

        // 1) VALIDACIONES
        LocalDate start = c.getStartDate();
        LocalDate end = c.getEndDate();

        YearMonth ym = YearMonth.from(payrollDate);
        LocalDate firstDay = ym.atDay(1);
        LocalDate lastDay = ym.atEndOfMonth();

        if (lastDay.isBefore(start)) {
            throw new IllegalArgumentException("Payroll before contract start");
        }

        if (end != null && end.isBefore(firstDay)) {
            throw new IllegalArgumentException(
                    "Contract not active this month");
        }

        this.contract = c;
        this.date = lastDay;

        // 2) CÁLCULOS

        // Si el mes tiene algún día dentro de la vigencia del contrato, se paga
        // el mes completo
        double fractionOfMonth = 1.0;

        double annualSalary = c.getAnnualBaseSalary();
        ProfessionalGroup pg = c.getProfessionalGroup();
        double taxRate = c.getTaxRate();

        // Base salarial mensual prorrateada
        baseSalary = round3(annualSalary / PAYMENTS_IN_YEAR * fractionOfMonth);

        // Extra salarial (solo junio y diciembre)
        int month = payrollDate.getMonthValue();
        extraSalary = ( month == 6 || month == 12 )
                ? round3(annualSalary / PAYMENTS_IN_YEAR * fractionOfMonth)
                : 0.0;

        // Productividad: sumamos solo WO facturadas y dentro del mes
        productivityEarning = round3(c.getMechanic()
                                      .getInterventions()
                                      .stream()
                                      .filter(i -> i.getWorkOrder()
                                                    .isInvoiced())
                                      .filter(i -> YearMonth.from(
                                              i.getWorkOrder()
                                               .getDate())
                                                            .equals(ym))
                                      .mapToDouble(i -> i.getWorkOrder()
                                                         .getAmount())
                                      .sum()
                * pg.getProductivityRate() * fractionOfMonth);

        // Trienios
        long years =
                   java.time.temporal.ChronoUnit.YEARS.between(start, lastDay);
        long trienniums = years / 3;
        trienniumEarning = round3(
                trienniums * pg.getTrienniumSalary() * fractionOfMonth);

        // Total bruto
        grossSalary = round3(baseSalary + extraSalary + productivityEarning
                + trienniumEarning);

        // IRPF
        taxDeduction = round3(grossSalary * taxRate);

        // Seguridad Social = 5% anual / 12
        nicDeduction = round3(
                ( annualSalary * 0.05 / PAYROLLS_IN_YEAR ) * fractionOfMonth);

        // Total deducciones
        totalDeduction = round3(taxDeduction + nicDeduction);

        // Neto
        netSalary = round2(grossSalary - totalDeduction);

        // ENLACE CONTRATO - NÓMINA
        Associations.Generates.link(c, this);
    }

    public LocalDate getDate() {
        return date;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public double getMonthlyBaseSalary() {
        return baseSalary;
    }

    public double getExtraSalary() {
        return extraSalary;
    }

    public double getProductivityEarning() {
        return productivityEarning;
    }

    public double getTrienniumEarning() {
        return trienniumEarning;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public double getNicDeduction() {
        return nicDeduction;
    }

    public double getTotalDeduction() {
        return totalDeduction;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public Contract getContract() {
        return contract;
    }

    void _setContract(Contract c) {
        this.contract = c;
    }

    // REDONDEOS
    private double round3(double v) {
        return BigDecimal.valueOf(v)
                         .setScale(3, RoundingMode.HALF_UP)
                         .doubleValue();
    }

    private double round2(double v) {
        return BigDecimal.valueOf(v)
                         .setScale(2, RoundingMode.HALF_UP)
                         .doubleValue();
    }
}
