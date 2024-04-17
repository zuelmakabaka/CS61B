package hashmap;

import java.util.*;

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
    // You should probably define some more!
    private int size;
    private double maxLoad = 0.75;
    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
        for(int index = 0; index <= 15; index++){
            buckets[index] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize );
        for(int index = 0; index <= initialSize - 1; index++){
            buckets[index] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        for(int index = 0; index <= initialSize - 1; index++){
            buckets[index] = createBucket();
        }
        this.maxLoad = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new  Node(key,value);
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
        return new ArrayList<>();
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

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear(){
        size = 0;
        for(int index = 0; index <= buckets.length - 1; index++){
            buckets[index] = createBucket();
        }

    }
    @Override
    public boolean containsKey(K key){
        if(get(key) != null){
            return true;
        }
        return false;
    }
    @Override
    public int size(){
        return size;
    }
    @Override
    public void put(K key, V value){
        int h = key.hashCode()% buckets.length;
        while (h < 0){
            h = h + buckets.length;
        }
        Iterator it = buckets[h].iterator();
        while(it.hasNext()){
            Node s = (Node)it.next();
            if(s.key.equals(key)){
                s.value = value;
                return;
            }
        }
        Node newnode = createNode(key, value);
        buckets[h].add(newnode);
        size += 1;

        }

    @Override
    public Set<K> keySet(){
        Set<K> myset = new HashSet<>();
        for(int index = 0; index <= buckets.length - 1; index++){
            Iterator it = buckets[index].iterator();
            while(it.hasNext()){
                Node s = (Node)it.next();
                if(!myset.contains(s.key)){
                    myset.add(s.key);
                }
            }
        }
        return myset;
    }
    @Override
    public V get(K key){
        int h = key.hashCode()%buckets.length;
        while (h < 0){
            h = h + buckets.length;
        }
        Iterator it = buckets[h].iterator();
        while(it.hasNext()){
            Node s = (Node)it.next();
            if(s.key.equals(key)){
                return s.value;
            }
        }
        return null;
    }
    @Override
    public Iterator<K> iterator(){
        return (Iterator<K>) createBucket().iterator();
    }
    @Override
    public V remove(K key){
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }
}
