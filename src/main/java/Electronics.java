public class Electronics extends Product{
    private String brand;
    private String warrantyPeriod;

    public Electronics(String productID, String productName, int noOfAvailableItems, double price, String brand, String warrantyPeriod) {
        super(productID, productName, noOfAvailableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override
    public String toString() {
        return "{" + super.toString()+ "," + brand + ","+ warrantyPeriod + ","+ "Electronics"+"}";
    }
}



