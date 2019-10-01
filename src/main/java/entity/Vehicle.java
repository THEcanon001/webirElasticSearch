package entity;

public class Vehicle {
        String Title;
        int Price;
        String Currency;
        String Condition;
        String Photos;
        String Brand;

    public Vehicle() {
    }

    public Vehicle(String title, int price, String currency, String condition, String photos, String brand) {
        Title = title;
        Price = price;
        Currency = currency;
        Condition = condition;
        Photos = photos;
        Brand = brand;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }
}
