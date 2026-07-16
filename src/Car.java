import java.io.Serializable;

class Car implements Serializable {

    private final String carId;
    private final String brand;
    private final String model;
    private final double basePricePerDay;

    private boolean available;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.available = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public double calculatePrice(int days) {
        return basePricePerDay * days;
    }

    public void rent() {
        available = false;
    }

    public void returnCar() {
        available = true;
    }

    @Override
    public String toString() {
        return String.format("%s - %s %s ($%.2f/day)",
                carId, brand, model, basePricePerDay);
    }
}