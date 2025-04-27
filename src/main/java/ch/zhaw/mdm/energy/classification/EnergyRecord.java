package ch.zhaw.mdm.energy.classification;

public class EnergyRecord {
    private String country;
    private String commodity;
    private String transactionType;
    private float quantityMj;
    private boolean isGreenEnergy;

    public EnergyRecord(String country, String commodity, String transactionType, float quantityMj, boolean isGreenEnergy) {
        this.country = country;
        this.commodity = commodity;
        this.transactionType = transactionType;
        this.quantityMj = quantityMj;
        this.isGreenEnergy = isGreenEnergy;
    }

    public String getCountry() {
        return country;
    }

    public String getCommodity() {
        return commodity;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public float getQuantityMj() {
        return quantityMj;
    }

    public boolean isGreenEnergy() {
        return isGreenEnergy;
    }
}
