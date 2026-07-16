import java.io.Serializable;

class Rental implements Serializable {

    private final Car car;
    private final Customer customer;
    private final int rentalDays;

    private final double originalAmount;
    private final double discountAmount;
    private final double finalAmount;

    public Rental(Car car, Customer customer, int rentalDays,
            double originalAmount,
            double discountAmount,
            double finalAmount) {

        this.car = car;
        this.customer = customer;
        this.rentalDays = rentalDays;
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }
}