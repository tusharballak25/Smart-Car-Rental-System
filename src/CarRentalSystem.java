import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CarRentalSystem implements Serializable {

    private final List<Car> cars;
    private final List<Customer> customers;
    private final List<Rental> rentals;
    private final List<Rental> rentalHistory;

    private int customerCounter;
    private double totalRevenue;
    private double totalDiscount;
    private int totalRentals;
    private int invoiceCounter;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        rentalHistory = new ArrayList<>();
        customerCounter = 1;
        totalRevenue = 0;
        totalDiscount = 0;
        totalRentals = 0;
        invoiceCounter = 1;
    }

    // ==========================
    // Add Methods
    // ==========================
    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    // ==========================
    // Helper Methods
    // ==========================
    private String generateCustomerId() {
        return String.format("CUS%03d", customerCounter++);
    }

    private String generateInvoiceNumber() {
        return String.format("INV%04d", invoiceCounter++);
    }

    private Car findCarById(String carId) {

        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                return car;
            }
        }

        return null;
    }

    private void displayAvailableCars() {

        System.out.println("\n========== AVAILABLE CARS ==========\n");

        boolean availableCarFound = false;

        int count = 1;
        for (Car car : cars) {

            if (car.isAvailable()) {
                System.out.println(count++ + ". " + car);
                availableCarFound = true;
            }

        }

        if (!availableCarFound) {
            System.out.println("No cars are available at the moment.");
        }

        System.out.println();
    }

    private void displayAllCars() {

        System.out.println("\n============= ALL CARS =============\n");

        if (cars.isEmpty()) {
            System.out.println("No cars found.");
            return;
        }

        System.out.printf("%-5s %-12s %-12s %-10s %-12s%n",
                "ID", "Brand", "Model", "Price", "Status");

        System.out.println("-----------------------------------------------------------");

        for (Car car : cars) {

            String status = car.isAvailable() ? "Available" : "Rented";

            System.out.printf("%-5s %-12s %-12s $%-9.2f %-12s%n",
                    car.getCarId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getBasePricePerDay(),
                    status);
        }

        System.out.println();
    }

    private void displayActiveRentals() {

        System.out.println("\n========== ACTIVE RENTALS ==========\n");

        if (rentals.isEmpty()) {
            System.out.println("No active rentals found.");
            return;
        }

        int count = 1;

        for (Rental rental : rentals) {

            Car car = rental.getCar();
            Customer customer = rental.getCustomer();

            System.out.println("Rental #" + count++);
            System.out.println("--------------------------------------");
            System.out.println("Customer ID   : " + customer.getCustomerId());
            System.out.println("Customer Name : " + customer.getName());
            System.out.println("Car ID        : " + car.getCarId());
            System.out.println("Car           : " + car.getBrand() + " " + car.getModel());
            System.out.println("Rental Days   : " + rental.getRentalDays());
            System.out.printf("Total Amount  : $%.2f%n",
                    car.calculatePrice(rental.getRentalDays()));
            System.out.println("Status        : Active");
            System.out.println("--------------------------------------\n");
        }
    }

    private void displayDashboard() {

        int availableCars = 0;
        int rentedCars = 0;

        for (Car car : cars) {

            if (car.isAvailable()) {
                availableCars++;
            } else {
                rentedCars++;
            }

        }

        System.out.println("\n====================================");
        System.out.println("            DASHBOARD");
        System.out.println("====================================");

        System.out.println("Total Cars        : " + cars.size());
        System.out.println("Available Cars    : " + availableCars);
        System.out.println("Rented Cars       : " + rentedCars);
        System.out.println("Total Customers   : " + customers.size());
        System.out.println("Active Rentals    : " + rentals.size());

        System.out.println("------------------------------------");

        System.out.println("Total Rentals     : " + totalRentals);
        System.out.printf("Total Revenue     : $%.2f%n", totalRevenue);
        System.out.printf("Total Discount    : $%.2f%n", totalDiscount);

        System.out.println("====================================");
    }

    private void displayRentalHistory() {

        System.out.println("\n========== RENTAL HISTORY ==========\n");

        if (rentalHistory.isEmpty()) {
            System.out.println("No rental history found.");
            return;
        }

        int count = 1;

        for (Rental rental : rentalHistory) {

            Car car = rental.getCar();
            Customer customer = rental.getCustomer();

            System.out.println("Rental #" + count++);
            System.out.println("--------------------------------------");
            System.out.println("Customer ID   : " + customer.getCustomerId());
            System.out.println("Customer Name : " + customer.getName());
            System.out.println("Car ID        : " + car.getCarId());
            System.out.println("Car           : " + car.getBrand() + " " + car.getModel());
            System.out.println("Rental Days   : " + rental.getRentalDays());

            System.out.printf("Original Amount : $%.2f%n",
                    rental.getOriginalAmount());

            System.out.printf("Discount        : -$%.2f%n",
                    rental.getDiscountAmount());

            System.out.printf("Final Amount    : $%.2f%n",
                    rental.getFinalAmount());

            System.out.println("Status        : Returned");
            System.out.println("--------------------------------------\n");
        }
    }

    // ==========================
    // Business Logic
    // ==========================
    public void rentCar(Car car, Customer customer, int rentalDays,
            double originalAmount,
            double discountAmount,
            double finalAmount) {

        if (!car.isAvailable()) {
            System.out.println("\nCar is already rented.");
            return;
        }

        car.rent();

        rentals.add(
                new Rental(
                        car,
                        customer,
                        rentalDays,
                        originalAmount,
                        discountAmount,
                        finalAmount
                )
        );

        totalRevenue += finalAmount;
        totalDiscount += discountAmount;
        totalRentals++;
    }

    public void returnCar(Car car) {

        Rental rentalToRemove = null;

        for (Rental rental : rentals) {

            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }

        }

        if (rentalToRemove != null) {

            car.returnCar();
            rentalHistory.add(rentalToRemove);
            rentals.remove(rentalToRemove);

        } else {

            System.out.println("\nThis car is not currently rented.");

        }
    }

    public void menu() {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n====================================");
            System.out.println("       CAR RENTAL SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Show Available Cars");
            System.out.println("4. Show All Cars");
            System.out.println("5. Active Rentals");
            System.out.println("6. Rental History");
            System.out.println("7. Dashboard");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a number.");
                continue;
            }

            switch (choice) {

                case 1 -> rentCarMenu(scanner);

                case 2 -> returnCarMenu(scanner);

                case 3 -> displayAvailableCars();

                case 4 -> displayAllCars();

                case 5 -> displayActiveRentals();

                case 6 -> displayRentalHistory();

                case 7 -> displayDashboard();

                case 8 -> {
                    FileManager.saveData("data/rentalSystem.dat", this);
                    scanner.close();

                    System.out.println("\nThank you for using Car Rental System.");
                    return;
                }

                default -> System.out.println("\nInvalid choice.");
            }
        }
    }

    private void rentCarMenu(Scanner scanner) {

        System.out.println("\n========== RENT A CAR ==========");

        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine().trim();

        if (customerName.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        displayAvailableCars();

        System.out.print("Enter Car ID: ");
        String carId = scanner.nextLine().trim();

        Car selectedCar = findCarById(carId);

        if (selectedCar == null) {
            System.out.println("Invalid Car ID.");
            return;
        }

        if (!selectedCar.isAvailable()) {
            System.out.println("Car is already rented.");
            return;
        }

        int rentalDays;

        try {

            System.out.print("Enter Rental Days: ");
            rentalDays = Integer.parseInt(scanner.nextLine());

        } catch (NumberFormatException e) {

            System.out.println("Invalid number.");
            return;

        }

        if (rentalDays <= 0) {

            System.out.println("Rental days should be greater than zero.");
            return;

        }

        double totalPrice = selectedCar.calculatePrice(rentalDays);

        double discountPercentage = 0;

        if (rentalDays >= 25) {
            discountPercentage = 15;
        } else if (rentalDays >= 16) {
            discountPercentage = 10;
        } else if (rentalDays >= 11) {
            discountPercentage = 8;
        } else if (rentalDays >= 5) {
            discountPercentage = 5;
        }

        double discountAmount = totalPrice * discountPercentage / 100;
        double finalAmount = totalPrice - discountAmount;
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateFormatter
                = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter
                = DateTimeFormatter.ofPattern("hh:mm a");

        System.out.print("\nConfirm Rental (Y/N): ");

        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {

            String invoiceNumber = generateInvoiceNumber();

            Customer customer = new Customer(generateCustomerId(), customerName);

            addCustomer(customer);

            rentCar(selectedCar, customer, rentalDays, totalPrice, discountAmount, finalAmount);

            System.out.println("\n======================================");
            System.out.println("          RENTAL RECEIPT");
            System.out.println("======================================");

            System.out.printf("Invoice No    : %s%n", invoiceNumber);
            System.out.printf("Date          : %s%n",
                    now.format(dateFormatter));
            System.out.printf("Time          : %s%n",
                    now.format(timeFormatter));
            System.out.println("--------------------------------------");
            System.out.printf("Customer Name : %s%n", customerName);
            System.out.printf("Car ID        : %s%n", selectedCar.getCarId());
            System.out.printf("Brand         : %s%n", selectedCar.getBrand());
            System.out.printf("Model         : %s%n", selectedCar.getModel());
            System.out.printf("Rental Days   : %d%n", rentalDays);
            System.out.printf("Price / Day   : $%.2f%n", selectedCar.getBasePricePerDay());
            System.out.println("--------------------------------------");
            System.out.printf("Original Amount : $%.2f%n", totalPrice);

            if (discountPercentage > 0) {
                System.out.printf("Discount (%.0f%%) : -$%.2f%n",
                        discountPercentage, discountAmount);
            } else {
                System.out.println("Discount         : No Discount Applied");
            }

            System.out.println("--------------------------------------");

            System.out.printf("Final Amount     : $%.2f%n", finalAmount);

            System.out.println("======================================");

            System.out.println("\nCar rented successfully.");

        } else {

            System.out.println("\nRental cancelled.");

        }

    }

    private void returnCarMenu(Scanner scanner) {

        System.out.println("\n========== RETURN A CAR ==========");

        System.out.print("Enter Car ID: ");
        String carId = scanner.nextLine().trim();

        Car car = findCarById(carId);

        if (car == null) {
            System.out.println("Invalid Car ID.");
            return;
        }

        if (car.isAvailable()) {
            System.out.println("This car is already available.");
            return;
        }

        Customer customer = null;

        for (Rental rental : rentals) {

            if (rental.getCar() == car) {
                customer = rental.getCustomer();
                break;
            }

        }

        returnCar(car);

        if (customer != null) {
            System.out.println("--------------------------------------");
            System.out.println("Customer : " + customer.getName());
            System.out.println("Car      : " + car.getBrand() + " " + car.getModel());
            System.out.println("Status   : Returned Successfully");
            System.out.println("--------------------------------------");
        }

    }

}