import java.io.*;
import java.util.*;

// -------- Room Inventory --------
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllRooms() {
        return rooms;
    }

    public void setRoom(String type, int count) {
        rooms.put(type, count);
    }
}

// -------- Persistence Service --------
class FilePersistenceService {

    public void saveInventory(RoomInventory inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Map.Entry<String, Integer> entry : inventory.getAllRooms().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String type = parts[0];
                    int count = Integer.parseInt(parts[1]);

                    inventory.setRoom(type, count);
                }
            }

            System.out.println("Inventory loaded successfully.");

        } catch (IOException e) {
            System.out.println("Error loading inventory.");
        }
    }
}

// -------- MAIN CLASS --------
public class Booking {

    public static void main(String[] args) {

        System.out.println("System Recovery");

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService service = new FilePersistenceService();

        // 🔄 Load previous data
        service.loadInventory(inventory, filePath);

        // If empty → initialize
        if (inventory.getAllRooms().isEmpty()) {
            inventory.addRoom("Single", 5);
            inventory.addRoom("Double", 3);
            inventory.addRoom("Suite", 2);
        }

        // 📊 Display inventory
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAllRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 💾 Save inventory
        service.saveInventory(inventory, filePath);
    }
}