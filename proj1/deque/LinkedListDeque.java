package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private Node sentinel;
    private class Node {
        private T value;
        private Node prev;
        private Node next;
        public Node(T x) {
            value = x;
            prev = this;
            next = this;
        }
    }
    public LinkedListDeque() {
        sentinel = new Node(null);
        size = 0;
    }
    private T getRecursiveHelper(int index, Node tmp) {
        if (tmp == sentinel) {
            return null;
        } else if (index == 0) {
            return tmp.value;
        } else {
            return getRecursiveHelper(index - 1, tmp.next);
        }
    }
    public T getRecursive(int index) {
        return getRecursiveHelper(index, sentinel.next);
    }
    @Override
    public void addFirst(T x) {
        Node tmp = new Node(x);
        tmp.next = sentinel.next;
        tmp.next.prev = tmp;
        sentinel.next = tmp;
        tmp.prev = sentinel;
        size += 1;
    }
    @Override
    public void addLast(T x) {
        Node tmp = new Node(x);
        tmp.prev = sentinel.prev;
        tmp.next = sentinel;
        tmp.prev.next = tmp;
        sentinel.prev = tmp;
        size += 1;
    }
    //    @Override
    //    public boolean isEmpty(){
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
        Node p = sentinel.next;
        while (p != sentinel) {
            if (p.next == sentinel) {
                System.out.print(p.value);
            } else {
                System.out.print(p.value + " ");
            }
            p = p.next;
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = sentinel.next.value;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T item = sentinel.prev.value;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return item;
    }

    @Override
    public T get(int index) {
        if (index > size) {
            return null;
        }
        Node p = sentinel.next;
        int i = 0;
        while (i != index) {
            p = p.next;
            i += 1;
        }
        return p.value;
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node p;
        private int pos;
        public LinkedListDequeIterator() {
            p = sentinel.next;
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            if (hasNext()) {
                T returnItem = p.value;
                pos += 1;
                p = p.next;
                return returnItem;
            }
            return null;
        }
    }
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
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
