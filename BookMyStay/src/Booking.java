import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

class RoomAllocationService {
    private Map<String, Integer> counter = new HashMap<>();

    public void allocateRoom(Reservation r, RoomInventory inventory) {
        String type = r.getRoomType();

        if (inventory.getAvailability(type) <= 0) {
            System.out.println("No rooms available for " + type);
            return;
        }

        int num = counter.getOrDefault(type, 0) + 1;
        counter.put(type, num);

        String roomId = type + "-" + num;

        inventory.reduceRoom(type);

        System.out.println("Booking confirmed for Guest: "
                + r.getGuestName() + ", Room ID: " + roomId);
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue queue;
    private RoomInventory inventory;
    private RoomAllocationService service;

    public ConcurrentBookingProcessor(BookingRequestQueue q,
                                      RoomInventory i,
                                      RoomAllocationService s) {
        queue = q;
        inventory = i;
        service = s;
    }

    public void run() {
        while (true) {
            Reservation r;

            synchronized (queue) {
                if (!queue.hasPendingRequests()) break;
                r = queue.getNextRequest();
            }

            synchronized (inventory) {
                service.allocateRoom(r, inventory);
            }
        }
    }
}

public class Booking {
    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 2);
        inventory.addRoom("Suite", 2);

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Double"));
        queue.addRequest(new Reservation("Kural", "Suite"));

        RoomAllocationService service = new RoomAllocationService();

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            System.out.println("Thread interrupted");
        }

        System.out.println("\nRemaining Inventory:");
        System.out.println("Single: " + inventory.getAvailability("Single"));
        System.out.println("Double: " + inventory.getAvailability("Double"));
        System.out.println("Suite: " + inventory.getAvailability("Suite"));
    }
}