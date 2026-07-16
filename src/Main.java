public class Main {

    public static void main(String[] args) {

        CarRentalSystem rentalSystem = 
        (CarRentalSystem) FileManager.loadData("data/rentalSystem.dat");

        if (rentalSystem == null) {
            rentalSystem = new CarRentalSystem();
            rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
            rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
            rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
            rentalSystem.addCar(new Car("C004", "Hyundai", "Creta", 90.0));
            rentalSystem.addCar(new Car("C005", "Tata", "Harrier", 110.0));
        }
        rentalSystem.menu();
    }
}

