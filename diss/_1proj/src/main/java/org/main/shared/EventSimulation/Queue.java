package org.main.shared.EventSimulation;

import org.main.shared.Statistics.AverageQueueLength;

import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Queue<I, T> {

    final private HashMap<I, T> lockedQueue = new HashMap<>();
    final private PriorityQueue <T> queue = new PriorityQueue<>();
    private Long capacity = null;
    public Queue(long capacity) {
        this.capacity = capacity;
    }
    public Queue() { }
    public Long getCapacity() {
        return capacity;
    }
    private AverageQueueLength averageQueueBeforeSTK = null;

    /**
     * return number of element in queue
     */
    public long getSize() {
        return queue.size() + lockedQueue.size();
    }

    /**
     * return number of not locked element
     */
    public long getReadySize() {
        return queue.size();
    }

    /**
     * add element to queue, skipping locked queue
     */
    public void addQueue(T element) {
        if (capacity != null && capacity <= getSize()) {
            System.out.println("error: queue capacity overflow");
        }
        queue.add(element);
        countStatistics();
    }

    /**
     * add element to locked queue
     */
    public void addQueueLocked(I id, T element) {
        if (capacity != null && capacity <= getSize()) {
            System.out.println("error: queue capacity overflow");
        }
        lockedQueue.put(id, element);
    }

    /**
     * move element from locked queue to queue
     */
    public void move (I id) {
        final T element = lockedQueue.get(id);
        if (element == null) {
            System.out.println("error: queue move not existing element");
            return;
        }
        lockedQueue.remove(id);
        queue.add(element);

    }

    /**
     * Retrieves and removes the head of this queue
     */
    public T poll() {
        final T element = queue.poll();
        countStatistics();
        return element;
    }

    /**
     * Retrieves, but does not remove, the head of this queue
     */
    public T peek() {
        return queue.peek();
    }

    public Collection<T> getQueue() {
        return queue;
    }
    public HashMap<I, T> getLockedQueue() {
        return lockedQueue;
    }

    public boolean isSpaceInQueue() {
        if (capacity == null) return true;
        return getSize() < capacity;
    }
    public void assignStatistics(AverageQueueLength averageQueueBeforeSTK) {
        this.averageQueueBeforeSTK = averageQueueBeforeSTK;
    }

    private void countStatistics() {
        if (averageQueueBeforeSTK != null) {
            averageQueueBeforeSTK.countAverageQueueLength();
        }
    }

    public void clear(){
        queue.clear();
        lockedQueue.clear();
    }
}
