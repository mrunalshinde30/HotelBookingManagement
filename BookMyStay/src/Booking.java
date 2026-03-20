import java.util.*;

public class Booking {

    /* =======================
       CLASS: Custom Exception
    ======================= */
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    /* =======================
       CLASS: RoomInventory
    ======================= */
    static class RoomInventory {
        private Map<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 2);
            inventory.put("Suite", 1);
        }

        public int getAvailableRooms(String type) {
            return inventory.getOrDefault(type, 0);
        }
    }

    /* =======================
       CLASS: Validator
    ======================= */
    static class ReservationValidator {

        public void validate(String guestName, String roomType, RoomInventory inventory)
                throws InvalidBookingException {

            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            if (!roomType.equals("Single") &&
                    !roomType.equals("Double") &&
                    !roomType.equals("Suite")) {
                throw new InvalidBookingException("Invalid room type selected");
            }

            if (inventory.getAvailableRooms(roomType) <= 0) {
                throw new InvalidBookingException("No rooms available for selected type");
            }
        }
    }

    /* =======================
       MAIN METHOD (UC9)
    ======================= */
    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();

        try {
            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String type = scanner.nextLine();

            validator.validate(name, type, inventory);

            System.out.println("Booking successful!");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}