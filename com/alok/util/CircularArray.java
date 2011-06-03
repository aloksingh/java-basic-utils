package com.alok.util;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CircularArray<T> implements Iterable<T> {
    private final int size;
    private final Object[] array;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int head = 0;

    public CircularArray(int maxSize){
        this.size = maxSize;
        this.array = new Object[maxSize];
        this.head = 0;
    }

    public Iterator<T> iterator() {
        Object[] items = new Object[size];
        lock.readLock().lock();
        System.arraycopy(array, 0, items, 0, array.length);
        ReverseIterator<T> iterator = new ReverseIterator<T>(items, head);
        lock.readLock().unlock();
        return iterator;
    }

    public synchronized  T add(T item) {
        lock.writeLock().lock();
        T i = (T) array[head];
        array[head++] = item;
        if(head == size){
            head = 0;
        }
        lock.writeLock().unlock();
        return i;
    }

    public int size(){
        return size;
    }

    public T head() {
        T item = null;
        lock.readLock().lock();
        if(head == 0){
            item = (T) array[size -1];
        }else{
            item = (T) array[head -1];
        }
        lock.readLock().unlock();
        return item;
    }

    private class ReverseIterator<T> implements Iterator<T>{
        private final Object[] items;
        private final int head;
        private int stepCount = 0;

        public ReverseIterator(final Object[] items, final int cursor){
            this.items = items;
            this.head = cursor > 0 ? cursor -1 : items.length -1;
            this.stepCount = 0;
        }
        public boolean hasNext() {
            return stepCount < items.length;
        }

        public T next() {
            int index = head - stepCount;
            if(index < 0){
                index = items.length + index;
            }
            stepCount++;
            return (T) items[index];
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }
}

