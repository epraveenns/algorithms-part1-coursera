/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first = null;
    private int size;

    public Deque()                           // construct an empty deque
    {
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return first == null;
    }

    public int size()                        // return the number of items on the deque
    {
        return size;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> itemNode = new Node<Item>();
        itemNode.value = item;
        itemNode.next = first;
        first = itemNode;
        size++;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> itemNode = new Node<Item>();
        itemNode.value = item;
        itemNode.next = null;
        if (first == null) {
            first = itemNode;
        }
        else {
            Node<Item> currentNode = first;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = itemNode;
        }
        size++;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> current = first;
        first = first.next;
        size--;
        return current.value;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> current = first;
        Item value;
        if (current.next == null) {
            first = null;
            value = current.value;
        }
        else {
            while (current.next.next != null) {
                current = current.next;
            }
            value = current.next.value;
            current.next = null;
        }
        size--;
        return value;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DeqIter();
    }

    public static void main(String[] args)   // unit testing (optional)
    {

    }

    private class Node<Item> {
        Item value;
        Node<Item> next;
    }

    private class DeqIter implements Iterator<Item> {
        private Node<Item> cursor = first;

        @Override
        public boolean hasNext() {
            return cursor != null && cursor.next != null;
        }

        @Override
        public Item next() {
            if (cursor == null) {
                throw new NoSuchElementException();
            }
            cursor = cursor.next;
            return cursor.next.value;
        }
    }
}