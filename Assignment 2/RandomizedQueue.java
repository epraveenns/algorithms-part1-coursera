/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int size, last, first;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        array = (Item[]) new Object[16];
    }

    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return size == 0;
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return size;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = item;
                break;
            }
        }
        size++;
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index;
        while (true) {
            index = StdRandom.uniform(array.length);
            Item item = array[index];
            if (item != null) {
                array[index] = null;
                size--;
                if (size == array.length / 4) {
                    array = Arrays.copyOf(array, array.length / 4);
                }
                return item;
            }
        }
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index;
        while (true) {
            index = StdRandom.uniform(array.length);
            if (array[index] != null) {
                return array[index];
            }
        }
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new randItr();
    }

    public static void main(String[] args)   // unit testing (optional)
    {

    }

    private class randItr implements Iterator<Item> {
        private boolean[] itrList;
        private int count;

        public randItr() {
            itrList = new boolean[size];
        }

        public boolean hasNext() {
            return count != size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int index = StdRandom.uniform(size);
            while (itrList[index] || array[index] == null) {
                index = StdRandom.uniform(size);
            }
            itrList[index] = true;
            count++;
            return array[index];
        }
    }
}