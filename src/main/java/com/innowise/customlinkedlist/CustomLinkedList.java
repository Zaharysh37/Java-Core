package com.innowise.customlinkedlist;

import java.util.NoSuchElementException;

public class CustomLinkedList<T> {
    private CustomNode<T> head;
    private CustomNode<T> tail;
    private int size;

    public CustomLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T value) {
        CustomNode<T> tNode = new CustomNode<>(value);
        if (isEmpty()) {
            head = tail = tNode;
        } else {
            tNode.next = head;
            head.prev = tNode;
            head = tNode;
        }
        size++;
    }

    public void addLast(T value) {
        CustomNode<T> tNode = new CustomNode<>(value);
        if (isEmpty()) {
            head = tail = tNode;
        } else {
            tNode.prev = tail;
            tail.next = tNode;
            tail = tNode;
        }
        size++;
    }

    private CustomNode<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        CustomNode<T> tNode;
        if (index < size / 2) {
            tNode = head;
            for (int i = 0; i < index; i++) {
                tNode = tNode.next;
            }
        } else {
            tNode = tail;
            for (int i = size - 1; i > index; i--) {
                tNode = tNode.prev;
            }
        }
        return tNode;
    }

    public void add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            addFirst(value);
        } else if (index == size) {
            addLast(value);
        } else {
            CustomNode<T> tNode = new CustomNode<>(value);
            CustomNode<T> current = getNode(index);

            tNode.next = current;
            tNode.prev = current.prev;
            current.prev.next = tNode;
            current.prev = tNode;
            size++;
        }
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    public T get(int index) {
        return getNode(index).data;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }

        T data = head.data;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size --;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }

        T data = tail.data;
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            CustomNode<T> tNode = getNode(index);
            tNode.prev.next = tNode.next;
            tNode.next.prev = tNode.prev;
            size--;
            return tNode.data;
        }
    }
}
