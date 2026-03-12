import java.util.HashMap;
import java.util.Map;

/* Room Domain Model */
class Room {

    String type;
    double price;
    String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type : " + type);
        System.out.println("Price     : $" + price);
        System.out.println("Amenities : " + amenities);
    }
}


/* Centralized Inventory */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single", 5);
        inventory.put("Double", 0);
        inventory.put("Suite", 2);
    }

    public int getAvailability(String roomType) {

        return inventory.getOrDefault(roomType, 0);
    }

    public HashMap<String, Integer> getAllRooms() {

        return inventory;
    }
}


/* Search Service */
class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomCatalog) {

        System.out.println("Available Rooms:\n");

        for (String roomType : inventory.getAllRooms().keySet()) {

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                Room room = roomCatalog.get(roomType);

                room.displayDetails();

                System.out.println("Available Rooms: " + available);
                System.out.println("---------------------------");
            }
        }
    }
}


/* Application Entry */
public class Booking {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Book My Stay - Room Search System ");
        System.out.println(" Version 4.0 ");
        System.out.println("====================================");

        RoomInventory inventory = new RoomInventory();

        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room("Single", 80, "1 Bed, Free WiFi"));
        roomCatalog.put("Double", new Room("Double", 120, "2 Beds, Free WiFi, TV"));
        roomCatalog.put("Suite", new Room("Suite", 250, "King Bed, Ocean View, Mini Bar"));

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(inventory, roomCatalog);
    }
}