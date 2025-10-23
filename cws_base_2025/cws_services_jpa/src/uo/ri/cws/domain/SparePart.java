package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.util.assertion.ArgumentChecks;

public class SparePart {
    // natural attributes
    private String code;
    private String description;
    private double price;
    private int stock;
    private int minStock;
    private int maxStock;

    // accidental attributes
    private Set<Substitution> substitutions = new HashSet<>();

    public SparePart(String code, String description, double price) {
        this(code, description, price, 0, 0, 0);
    }

    public SparePart(String code, String description, double price, int stock,
            int minStock, int maxStock) {
        ArgumentChecks.isNotBlank(code);
        ArgumentChecks.isNotBlank(description);
        ArgumentChecks.isTrue(price >= 0);
        ArgumentChecks.isTrue(stock >= 0);
        ArgumentChecks.isTrue(minStock >= 0);
        ArgumentChecks.isTrue(maxStock >= 0);
        ArgumentChecks.isTrue(maxStock >= minStock);

        this.code = code;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.minStock = minStock;
        this.maxStock = maxStock;
    }

    public Set<Substitution> getSubstitutions() {
        return new HashSet<>(substitutions);
    }

    Set<Substitution> _getSubstitutions() {
        return substitutions;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SparePart other = (SparePart) obj;
        return Objects.equals(code, other.code);
    }

    @Override
    public String toString() {
        return "SparePart [code=" + code + ", description=" + description
                + ", price=" + price + ", stock=" + stock + ", minStock="
                + minStock + ", maxStock=" + maxStock + "]";
    }

}
