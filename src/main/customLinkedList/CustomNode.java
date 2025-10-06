package main.customLinkedList;

public class CustomNode<T> {
    public T data;
    public CustomNode<T> next;
    public CustomNode<T> prev;

    public CustomNode(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
