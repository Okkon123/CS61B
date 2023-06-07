package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private int initialSize = 16;
    private double loadFactor = 0.75;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize);
        size = 0;
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(initialSize);
        size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(initialSize);
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Your code won't compile until you do so!

    private int pos(int hashCode) {
        return Math.floorMod(hashCode, initialSize);
    }
    @Override
    public void clear() {
        size = 0;
        buckets = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (buckets == null) {
            return false;
        }
        int pos = pos(key.hashCode());
        Collection<Node> tmp = buckets[pos];
        if (tmp != null) {
            for (Node x : tmp) {
                if (x.key.equals(key)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            int pos = pos(key.hashCode());
            Collection<Node> tmp = buckets[pos];
            for (Node x : tmp) {
                if (x.key.equals(key)) {
                    return x.value;
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private boolean isNeedResize() {
        double nowFactor = (double) size / initialSize;
        return this.loadFactor <= nowFactor;
    }

    private void resize() {
        initialSize = initialSize * 2;
        size = 0;
        Collection<Node>[] tmp = buckets;
        buckets = createTable(initialSize);
        for (int i = 0; i < tmp.length; i += 1) {
            if (tmp[i] != null) {
                for (Node x : tmp[i]) {
                    put(x.key, x.value);
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (isNeedResize()) {
            resize();
        }
        int pos = pos(key.hashCode());
        Collection<Node> tmp = buckets[pos];
        if (containsKey(key)) {
            for (Node x : tmp) {
                if (x.key.equals(key)) {
                    x.value = value;
                }
            }
        } else {
            Node newNode = createNode(key, value);
            if (tmp == null) {
                buckets[pos] = createBucket();
                buckets[pos].add(newNode);
            } else {
                tmp.add(newNode);
            }
            size += 1;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> tmp = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node x : bucket) {
                    tmp.add(x.key);
                }
            }
        }
        return tmp;
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            int pos = pos(key.hashCode());
            V result = null;
            Collection<Node> tmp = buckets[pos];
            Iterator<Node> i = tmp.iterator();
            while (i.hasNext()) {
                Node temp = i.next();
                if (temp.key.equals(key)) {
                    result = temp.value;
                    i.remove();
                }
            }
            size -= 1;
            return result;
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (containsKey(key)) {
            if (get(key) == value) {
                remove(key);
            }
            return null;
        }
        return null;
    }

    private class MyHashMapIterator implements Iterator<K> {

        Iterator<K> t;
        MyHashMapIterator() {
            t = keySet().iterator();
        }
        @Override
        public boolean hasNext() {
            return t.hasNext();
        }

        @Override
        public K next() {
            return t.next();
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    //    public static void main(String[] args) {
    //        MyHashMap<String, String> q = new MyHashMap<>();
    //        q.put("c", "a");
    //        q.put("b", "a");
    //        q.put("a", "a");
    //        q.put("d", "a");
    //        q.put("e", "a");                         // a b c d e
    //        q.remove("e");     // a b c d
    //        q.remove("c");      // a b d
    //        q.put("f", "a");                         // a b d f
    //        q.remove("d");      // a b f
    //
    //    }
}
