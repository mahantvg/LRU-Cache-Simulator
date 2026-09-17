public class Node {

    String key;
    Node prev;
    Node next;

    public Node(String key) {
        this.key = key;
        prev = null;
        next = null;
    }
}