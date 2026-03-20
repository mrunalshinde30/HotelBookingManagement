import java.util.*;

public class Booking {

    /* =======================
       CLASS: AddOnService
    ======================= */
    static class AddOnService {
        private String serviceName;
        private double cost;

        public AddOnService(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        public String getServiceName() {
            return serviceName;
        }

        public double getCost() {
            return cost;
        }
    }

    /* =======================
       CLASS: AddOnServiceManager
    ======================= */
    static class AddOnServiceManager {

        private Map<String, List<AddOnService>> servicesByReservation;

        public AddOnServiceManager() {
            servicesByReservation = new HashMap<>();
        }

        public void addService(String reservationId, AddOnService service) {
            servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());
            servicesByReservation.get(reservationId).add(service);
        }

        public double calculateTotalServiceCost(String reservationId) {
            double total = 0;

            List<AddOnService> services = servicesByReservation.get(reservationId);

            if (services != null) {
                for (AddOnService s : services) {
                    total += s.getCost();
                }
            }

            return total;
        }
    }

    /* =======================
       MAIN METHOD (UC7)
    ======================= */
    public static void main(String[] args) {

        System.out.println("Add-On Service Selection");

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "Single-1";

        // create services
        AddOnService s1 = new AddOnService("Breakfast", 500);
        AddOnService s2 = new AddOnService("Spa", 1000);

        // add services
        manager.addService(reservationId, s1);
        manager.addService(reservationId, s2);

        // calculate total cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);

        // output
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " + totalCost);
    }
}