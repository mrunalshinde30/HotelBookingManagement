import java.util.*;

public class Booking {

    /* =======================
       CLASS: Reservation
    ======================= */
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
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

        public void decrementRoom(String type) {
            inventory.put(type, inventory.get(type) - 1);
        }
    }

    /* =======================
       CLASS: Booking Queue
    ======================= */
    static class BookingRequestQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll();
        }

        public boolean hasRequests() {
            return !queue.isEmpty();
        }
    }

    /* =======================
       CLASS: RoomAllocationService
    ======================= */
    static class RoomAllocationService {

        private Set<String> allocatedRoomIds = new HashSet<>();
        private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

        public void allocateRoom(Reservation reservation, RoomInventory inventory) {

            String type = reservation.getRoomType();

            // check availability
            if (inventory.getAvailableRooms(type) <= 0) {
                System.out.println("No rooms available for " + reservation.getGuestName());
                return;
            }

            // generate unique room id
            String roomId = generateRoomId(type);

            // store allocated id
            allocatedRoomIds.add(roomId);

            // map by type
            assignedRoomsByType.putIfAbsent(type, new HashSet<>());
            assignedRoomsByType.get(type).add(roomId);

            // update inventory
            inventory.decrementRoom(type);

            // confirmation
            System.out.println("Booking confirmed for Guest: "
                    + reservation.getGuestName()
                    + ", Room ID: " + roomId);
        }

        private String generateRoomId(String type) {
            int count = assignedRoomsByType.getOrDefault(type, new HashSet<>()).size() + 1;
            return type + "-" + count;
        }
    }

    /* =======================
       MAIN METHOD (UC6)
    ======================= */
    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomAllocationService service = new RoomAllocationService();

        // create requests
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // process FIFO
        while (queue.hasRequests()) {
            Reservation r = queue.getNextRequest();
            service.allocateRoom(r, inventory);
        }
    }
}