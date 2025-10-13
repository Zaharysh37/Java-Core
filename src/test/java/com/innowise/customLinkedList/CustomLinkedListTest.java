package com.innowise.customLinkedList;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {
    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testEmptyList() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testAddFirst() {
        list.addFirst(10);
        assertEquals(1, list.size());
        assertEquals(10, list.getFirst());

        list.addFirst(20);
        assertEquals(2, list.size());
        assertEquals(20, list.getFirst());
        assertEquals(10, list.getLast());
    }

    @Test
    void testAddLast() {
        list.addLast(10);
        assertEquals(1, list.size());
        assertEquals(10, list.getLast());

        list.addLast(20);
        assertEquals(2, list.size());
        assertEquals(10, list.getFirst());
        assertEquals(20, list.getLast());
    }

    @Test
    void testAddAtIndex() {
        list.add(0, 10); // addFirst
        list.add(1, 30); // addLast
        list.add(1, 20); // add in middle

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testAddAtIndexInvalid() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 10));
    }

    @Test
    void testGetFirstEmptyList() {
        assertThrows(NoSuchElementException.class, () -> list.getFirst());
    }

    @Test
    void testGetLastEmptyList() {
        assertThrows(NoSuchElementException.class, () -> list.getLast());
    }

    @Test
    void testGetByIndex() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);

        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testGetByIndexInvalid() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));

        list.addLast(10);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testRemoveFirst() {
        list.addLast(10);
        list.addLast(20);

        assertEquals(10, list.removeFirst());
        assertEquals(1, list.size());
        assertEquals(20, list.getFirst());

        assertEquals(20, list.removeFirst());
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testRemoveFirstEmptyList() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    @Test
    void testRemoveLast() {
        list.addLast(10);
        list.addLast(20);

        assertEquals(20, list.removeLast());
        assertEquals(1, list.size());
        assertEquals(10, list.getLast());

        assertEquals(10, list.removeLast());
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testRemoveLastEmptyList() {
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    @Test
    void testRemoveByIndex() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);

        assertEquals(20, list.remove(1));
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));

        assertEquals(10, list.remove(0)); // remove first
        assertEquals(30, list.remove(0)); // remove last (now first)
        assertEquals(0, list.size());
    }

    @Test
    void testRemoveByIndexInvalid() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));

        list.addLast(10);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }
}
