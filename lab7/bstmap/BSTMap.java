package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private int size = 0;
    private BSTnode root = null;
    BSTMap() {

    };
    private class BSTnode{
        private K key;
        private V value;
        private BSTnode left;
        private BSTnode right;
        public BSTnode(K key, BSTnode left, BSTnode right,V value) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.value = value;
        }
        public BSTnode(K key,V value) {
            this.key = key;
            this.value = value;
        }
    }
    @Override
    public void clear(){
        size = 0;
        root = null;
    }
    @Override
    public boolean containsKey(K key){
        BSTnode present = root;
        while(present != null) {
            if (present.key.compareTo(key) == 0) {
                return true;
            }
            if (present.key.compareTo(key) > 0) {
                present = present.left;
            }
            if (present.key.compareTo(key) < 0) {
                present = present.right;
            }
        }
        return false;
    }
    public V get(K key){
        BSTnode present = root;
        while(present != null) {
            if (present.key.compareTo(key) == 0) {
                return present.value;
            }
            if (present.key.compareTo(key) > 0) {
                present = present.left;
            }
            if (present.key.compareTo(key) < 0) {
                present = present.right;
            }
        }
        return null;
    }
    @Override
    public int size(){
        return size;
    }
    @Override
    public void put(K key, V value){
        BSTnode insert = new BSTnode(key, value);
        BSTnode T = root;
        if (root == null){
            root = insert;
            size += 1;
        }
        while(T != null){
            if (T.key.compareTo(key) == 0) {
                return;
            }
            if (T.key.compareTo(key) > 0 && T.left != null) {
                T = T.left;
            }
            if (T.key.compareTo(key) < 0 && T.right != null) {
                T = T.right;
            }
            if (T.key.compareTo(key) > 0 && T.left == null) {
                T.left = insert;
                size += 1;
            }
            if (T.key.compareTo(key) < 0 && T.right == null) {
                T.right = insert;
                size += 1;
            }
        }
    }

    public Set<K> keySet(){
        throw new UnsupportedOperationException();
    }
    public V remove(K key){
        throw new UnsupportedOperationException();
    }
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }
    public Iterator<K> iterator() {
        return new iterator();
    }
    private class iterator implements Iterator<K>{
        BSTnode last = root;
        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public K next(){
            throw new UnsupportedOperationException();
        }

    }
    public void printInOrder(){}
}
