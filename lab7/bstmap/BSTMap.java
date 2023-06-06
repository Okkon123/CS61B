package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private int size;
    private BSTNode root;
    private Set<K> s = new HashSet<>();

    public BSTMap() {
        size = 0;
        root = null;
    }

    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        BSTNode get(K key) {
            if (this.key.equals(key)) {
                return this;
            } else {
                int cmp = key.compareTo(this.key);
                if (this.left == null && this.right == null) {
                    return null;
                }
                if (cmp > 0) {
                    if (this.right == null) {
                        return null;
                    }
                    return this.right.get(key);
                } else {
                    if (this.left == null) {
                        return null;
                    }
                    return this.left.get(key);
                }
            }
        }

        BSTNode getLeaf(K key) {
            int cmp = key.compareTo(this.key);
            if (cmp > 0) {
                if (this.right == null) {
                    return this;
                } else {
                    return this.right.getLeaf(key);
                }
            } else {
                if (this.left == null) {
                    return this;
                } else {
                    return this.left.getLeaf(key);
                }
            }
        }

        BSTNode getParent(K key) {
            if (this.left != null) {
                if (this.left.key == key) {
                    return this;
                }
            }
            if (this.right != null) {
                if (this.right.key == key) {
                    return this;
                }
            }
            int cmp = key.compareTo(this.key);
            if (cmp > 0) {
                if (this.right != null) {
                    return this.right.getParent(key);
                }
            }
            if (this.left != null) {
                return this.left.getParent(key);
            }
            return null;
        }

        BSTNode getLargest() {
            BSTNode tmp = this.left;
            while (tmp.right != null) {
                tmp = tmp.right;
            }
            return tmp;
        }

        void printInOrder() {
            if (this.left != null) {
                this.left.printInOrder();
            }
            System.out.println(this.key);
            if (this.right != null) {
                this.right.printInOrder();
            }
        }

        Set<K> getAllKey() {
            s.add(this.key);
            if (this.left != null) {
                this.left.getAllKey();
            }
            if (this.right != null) {
                this.right.getAllKey();
            }
            return s;
        }

    }
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        BSTNode tmp = root.get(key);
        return tmp != null;
    }

    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        BSTNode tmp = root.get(key);
        if (tmp != null) {
            return tmp.value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value);
            size += 1;
        } else if (containsKey(key)) {
            BSTNode tmp = root.get(key);
            tmp.value = value;
        } else {
            BSTNode tmp = root.getLeaf(key);
            int cmp = key.compareTo(tmp.key);
            if (cmp > 0) {
                tmp.right = new BSTNode(key, value);
            } else {
                tmp.left = new BSTNode(key, value);
            }
            size += 1;
        }
    }

    public void printInOrder() {
        root.printInOrder();
    }

    private void resetSet() {
        s = new HashSet<>();
    }
    @Override
    public Set<K> keySet() {
        Set<K> tmp = root.getAllKey();
        resetSet();
        return tmp;
    }

    private int lr (BSTNode child, BSTNode parent) {
        if (parent.left != null) {
            if (parent.left.key.equals(child.key)) {
                return 0;
            }
            return 1;
        }
        return 1;
    }
    private V removeNoNode(BSTNode noNode, BSTNode parNode) {
        if (parNode == null) {
            root = null;
            return noNode.value;
        }
        int l0_r1 = lr(noNode,parNode);
        if (l0_r1 == 0) {
            parNode.left = null;
        } else {
            parNode.right = null;
        }
        return noNode.value;
    }
    private V removeOneNode(BSTNode oneNode, BSTNode parNode) {
        if (parNode == null) {
            if (oneNode.left != null) {
                root = oneNode.left;
            } else {
                root = oneNode.right;
            }
            return oneNode.value;
        }
        int l0_r1 = lr(oneNode, parNode);
        if (oneNode.left != null) {
            if (l0_r1 == 0) {
                parNode.left = oneNode.left;
            } else {
                parNode.right = oneNode.left;
            }
        }
        if (oneNode.right != null) {
            if(l0_r1 == 0) {
                parNode.left = oneNode.right;
            } else {
                parNode.right = oneNode.right;
            }
        }
        return oneNode.value;
    }

    private V removeTwoNode(BSTNode twoNode, BSTNode parNode) {
        BSTNode largestNode = twoNode.getLargest();
        if (parNode == null) {
            remove(largestNode.key);
            largestNode.left = twoNode.left;
            largestNode.right = twoNode.right;
            root = largestNode;
            return twoNode.value;
        }
        int l0_r1 = lr(twoNode, parNode);
        remove(largestNode.key);
        if (l0_r1 == 0) {
            parNode.left = largestNode;
        } else {
            twoNode.right = largestNode;
        }
        largestNode.left = twoNode.left;
        largestNode.right = twoNode.right;
        return twoNode.value;
    }

    private void sizeMinus1(){
        size -= 1;
    }
    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            BSTNode tmp = root.get(key);
            BSTNode parent = root.getParent(key);
            sizeMinus1();
            if (tmp.left == null && tmp.right == null) {
                return removeNoNode(tmp, parent);
            } else if (tmp.left == null || tmp.right == null) {
                return removeOneNode(tmp, parent);
            } else {
                return removeTwoNode(tmp, parent);
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (containsKey(key)) {
            BSTNode tmp = root.get(key);
            if (tmp.value == value) {
                return remove(key);
            }
            return null;
        }
        return  null;
    }

    private class BSTMapIterator implements Iterator<K> {

        Iterator<K> s;
        BSTMapIterator() {
            s = keySet().iterator();
        }
        @Override
        public boolean hasNext() {
            return s.hasNext();
        }

        @Override
        public K next() {
            return s.next();
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

//    public static void main(String[] args) {
////        BSTMap<String, Integer> tmp = new BSTMap<>();
////        tmp.put("5", 5);
////        tmp.put("3", 3);
////        tmp.put("4", 4);
////        tmp.put("2", 2);
////        tmp.put("1", 1);
////        tmp.put("8", 8);
////        tmp.put("7", 7);
////        tmp.put("9", 9);
////        tmp.put("6", 6);
////        tmp.printInorder();
////        tmp.remove("3");
////        tmp.printInorder();
//        BSTMap rightChild = new BSTMap();
//        rightChild.put('A', 1);
//        rightChild.put('B', 2);
//        Integer result = (Integer) rightChild.remove('A');
//        for (int i = 0; i < 10; i++) {
//            rightChild.put((char) ('C'+i), 3+i);
//        }
//        rightChild.put('A', 100);
//        rightChild.remove('D');
//        rightChild.remove('G');
//        rightChild.remove('A');
//
//        BSTMap leftChild = new BSTMap();
//        leftChild.put('B', 1);
//        leftChild.put('A', 2);
//        leftChild.remove('B');
//        leftChild.get('B');
//
//        BSTMap noChild = new BSTMap();
//        noChild.put('Z', 15);
//        noChild.remove('Z');
//        noChild.size();
//        noChild.get('Z');
//
//    }
}
