package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int num;
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;
    private static final int DOWN_SIZE_BOUND = 16;
    public ArrayDeque() {
        size = 0;
        num = 8;
        items = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int capacity) {
        T[] tmp = (T[]) new Object[capacity];
        int i = 1;
        int start = (nextFirst + 1) % num;
        while (i <= size()) {
            tmp[i] = items[start];
            i += 1;
            start = (start + 1) % num;
        }
        items = tmp;
        nextFirst = 0;
        nextLast = i;
        num = capacity;
    }
    @Override
    public void addFirst(T x) {
        if (size == num) {
            resize(num * 2);
        }
        items[nextFirst] = x;
        nextFirst = (nextFirst - 1 + num) % num;
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (size == num) {
            resize(num * 2);
        }
        items[nextLast] = x;
        nextLast = (nextLast + 1) % num;
        size += 1;
    }

    //    @Override
    //    public boolean isEmpty() {
    //        if(size==0){
    //            return true;
    //        }
    //        return false;
    //    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int start = (nextFirst + 1) % num;
        int i = 0;
        while (i < size() - 1) {
            System.out.print(items[start] + " ");
            start = (start + 1) % num;
            i += 1;
        }
        System.out.println(items[start]);
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (num >= DOWN_SIZE_BOUND) {
            if (4 * size < num) {
                resize(num / 4);
            }
        }
        nextFirst = (nextFirst + 1) % num;
        T tmp = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        return tmp;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (num >= DOWN_SIZE_BOUND) {
            if (4 * size < num) {
                resize(num / 4);
            }
        }
        nextLast = (nextLast - 1 + num) % num;
        T tmp = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return tmp;
    }

    @Override
    public T get(int index) {
        if (index >= num) {
            return null;
        } else {
            return items[(nextFirst + 1 + index) % num];
        }
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int start;
        private int passNum;
        public ArrayDequeIterator() {
            start = (nextFirst + 1) % num;
            passNum = 0;
        }
        @Override
        public boolean hasNext() {
            if (passNum < size()) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T tmp = items[start];
                start = (start + 1) % num;
                passNum += 1;
                return tmp;
            }
            return null;
        }
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Deque) {
            Deque tmp = (Deque) o;
            if (tmp.size() != size()) {
                return false;
            }
            for (int i = 0; i < size(); i++) {
                if (!(tmp.get(i).equals(get(i)))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
