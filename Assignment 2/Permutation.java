/* *****************************************************************************
 *  Name: Erramilli Naga Surya Praveen
 *  Date: 12 Feb 2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        Integer k = Integer.valueOf(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            String dequeue = randomizedQueue.dequeue();
            System.out.println(dequeue);
        }
    }
}