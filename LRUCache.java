import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
public class LRUCache {
    private int capacity;
    private HashMap<String, Node> map;
    private Node head;
    private Node tail;
    private int hits;
    private int misses;
    private String lastEvicted = "None";
    private ArrayList<String> history;
    private StringBuilder logs;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        history = new ArrayList<>();
        logs = new StringBuilder();
        hits = 0;
        misses = 0;
    }
    //-------------------------
    // Insert Key
    //-------------------------
    public void put(String key) {
        if (key == null || key.trim().isEmpty())
            return;
        if (map.containsKey(key)) {
            moveToEnd(map.get(key));
            addLog("Updated Existing Key : " + key);
            return;
        }
        Node node = new Node(key);
        if (map.size() == capacity) {
            lastEvicted = head.key;
            addLog("Evicted : " + head.key);
            map.remove(head.key);
            remove(head);
        }
        addLast(node);
        map.put(key, node);
        addLog("Inserted : " + key);
    }
    //-------------------------
    // Search Key
    //-------------------------
    public boolean get(String key) {
        history.add(key);
        if (!map.containsKey(key)) {
            misses++;
            addLog("Cache Miss : " + key);
            return false;
        }
        hits++;
        moveToEnd(map.get(key));
        addLog("Cache Hit : " + key);
        return true;
    }
    //-------------------------
    // Remove Node
    //-------------------------
    private void remove(Node node) {
        if (node == null)
            return;
        if (node.prev != null)
            node.prev.next = node.next;
        else
            head = node.next;
        if (node.next != null)
            node.next.prev = node.prev;
        else
            tail = node.prev;
    }
    //-------------------------
    // Add Last
    //-------------------------
    private void addLast(Node node) {
        if (head == null) {
            head = tail = node;
            return;
        }
        tail.next = node;
        node.prev = tail;
        tail = node;
    }
    //-------------------------
    // Move to End
    //------------------------
    private void moveToEnd(Node node) {
        if (tail == node)
            return;
        remove(node);
        node.prev = null;
        node.next = null;
        addLast(node);
    }
    //-------------------------
    // Display Cache
    //-------------------------
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("Least Recently Used\n\n");
        Node temp = head;
        while (temp != null) {
            sb.append(temp.key).append("\n");
            temp = temp.next;
        }
        sb.append("\nMost Recently Used");
        return sb.toString();
    }
    //-------------------------
    // Logger
    //-------------------------
    private void addLog(String message) {
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        logs.append("[")
                .append(LocalDateTime.now().format(dtf))
                .append("] ")
                .append(message)
                .append("\n");
    }
    //-------------------------
    // Save Logs
    //-------------------------
    public void saveLogs() {
        try {
            FileWriter fw = new FileWriter("cache_logs.txt");
            fw.write(logs.toString());
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------
    // Clear Cache
    //-------------------------
    public void clear() {
        map.clear();
        head = null;
        tail = null;
        hits = 0;
        misses = 0;
        history.clear();
        logs.setLength(0);
        lastEvicted = "None";
    }
    //-------------------------
    // Statistics
    //-------------------------
    public int getHits() {
        return hits;
    }
    public int getMisses() {
        return misses;
    }
    public double getHitRatio() {
        int total = hits + misses;
        if (total == 0)
            return 0;
        return (hits * 100.0) / total;
    }
    //-------------------------
    // LRU
    //-------------------------
    public String getLRU() {
        if (head == null)
            return "None";

        return head.key;
    }
    //-------------------------
    // MRU
    //-------------------------
    public String getMRU() {
        if (tail == null)
            return "None";

        return tail.key;
    }
    //-------------------------
    // Last Evicted
    //-------------------------
    public String getLastEvicted() {

        return lastEvicted;
    }
    //-------------------------
    // Logs
    //-------------------------
    public String getLogs() {
        return logs.toString();
    }
    //-------------------------
    // Search History
    //-------------------------
    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        for (String s : history)
            sb.append(s).append("\n");
        return sb.toString();
    }
    //-------------------------
    // Cache Size
    //-------------------------
    public int getCurrentSize() {
        return map.size();
    }
    public int getCapacity() {
        return capacity;
    }
}