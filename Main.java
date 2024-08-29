import java.util.*;

abstract class Vehicle {
    private String vehicleId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnVehicle() {
        isAvailable = true;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    @Override
    public String toString() {
        return vehicleId + " - " + brand + " " + model;
    }
}

class Car extends Vehicle {
    public Car(String carId, String brand, String model, double basePricePerDay) {
        super(carId, brand, model, basePricePerDay);
    }

    @Override
    public double calculatePrice(int rentalDays) {
        // Can add specific pricing logic for cars if needed
        return super.calculatePrice(rentalDays);
    }
}

class Bike extends Vehicle {
    public Bike(String bikeId, String brand, String model, double basePricePerDay) {
        super(bikeId, brand, model, basePricePerDay);
    }

    @Override
    public double calculatePrice(int rentalDays) {
        // Can add specific pricing logic for bikes if needed
        return super.calculatePrice(rentalDays);
    }
}

class Customer {
    private String name;
    private String customerId;

    public Customer(String customerId, String name) {
        this.name = name;
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Vehicle vehicle;
    private Customer customer;
    private int days;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class VehicleRentalSystem {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<Rental> rentals;

    public VehicleRentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, int days) {
        if (vehicle.isAvailable()) {
            vehicle.rent();
            rentals.add(new Rental(vehicle, customer, days));
        } else {
            System.out.println("Vehicle is not available for rent");
        }
    }

    public void returnVehicle(Vehicle vehicle) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getVehicle() == vehicle) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            vehicle.returnVehicle();
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Vehicle was not rented");
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("***** Vehicle Rental System *****");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.println("\n== Rent a Vehicle ==\n");
                System.out.print("Enter your Name: ");
                String customerName = sc.nextLine();
                System.out.println("\nAvailable Vehicles: ");
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.isAvailable()) {
                        System.out.println(vehicle);
                    }
                }

                System.out.println("\nEnter the vehicle ID you want to rent: ");
                String vehicleId = sc.nextLine();

                System.out.println("Enter the number of days for rental: ");
                int rentalDays = sc.nextInt();
                sc.nextLine();

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Vehicle selectedVehicle = null;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailable()) {
                        selectedVehicle = vehicle;
                        break;
                    }
                }

                if (selectedVehicle != null) {
                    double totalPrice = selectedVehicle.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Vehicle: " + selectedVehicle.getBrand() + " " + selectedVehicle.getModel());
                    System.out.printf("Total Price: $%.2f%n", totalPrice);
                    System.out.println("\nConfirm rental (Y/N): ");
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentVehicle(selectedVehicle, newCustomer, rentalDays);
                        System.out.println("\nVehicle rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid ID or vehicle not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Vehicle ==\n");
                System.out.println("Enter the vehicle ID you want to return: ");
                String vehicleId = sc.nextLine();

                Vehicle vehicleToReturn = null;
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getVehicleId().equals(vehicleId) && !vehicle.isAvailable()) {
                        vehicleToReturn = vehicle;
                        break;
                    }
                }

                if (vehicleToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getVehicle() == vehicleToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnVehicle(vehicleToReturn);
                        System.out.println("Vehicle returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Vehicle was not rented or rental information is missing");
                    }
                } else {
                    System.out.println("Invalid vehicle ID or vehicle is not rented");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using Vehicle Rental System.");
    }
}

public class Main {
    public static void main(String[] args) {
        VehicleRentalSystem rentalSystem = new VehicleRentalSystem();

        // Adding Cars and Bikes to the system
        Car car1 = new Car("C001", "Toyota", "Camry", 115.50);
        Car car2 = new Car("C002", "Honda", "Accord", 125.50);
        Bike bike1 = new Bike("B001", "Yamaha", "YZF-R3", 50.00);
        Bike bike2 = new Bike("B002", "Royal Enfield", "Classic 350", 70.00);

        rentalSystem.addVehicle(car1);
        rentalSystem.addVehicle(car2);
        rentalSystem.addVehicle(bike1);
        rentalSystem.addVehicle(bike2);

        rentalSystem.menu();
    }
}
