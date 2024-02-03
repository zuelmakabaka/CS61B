package deque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
public class LinkedListDeque <T> implements Deque<T>{
    public  class Node{
        public T item;
        public Node prev;
        public Node next;
        public Node(T i ,Node p, Node n){
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node Fsentinel;
    private Node Lsentinel;
    private int size;

    public LinkedListDeque(){
        Fsentinel = new Node(null,null,null);
        Lsentinel = new Node(null,Fsentinel,Fsentinel);
        Fsentinel.next = Lsentinel;
        Fsentinel.prev = Lsentinel;
        size = 0;
    }

    public void addFirst(T item){
        Node N = new Node(item,Fsentinel,Fsentinel.next);
        Fsentinel.next = N;
        N.next.prev = N;
        size = size + 1;
    }
    public void addLast(T item){
        Node N = new Node(item,Lsentinel.prev,Lsentinel);
        Lsentinel.prev = N;
        N.prev.next= N;
        size = size + 1;
    }
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
    public int size(){
        return this.size;
    }
    public void printDeque(){
        Node p = Fsentinel.next;
        while(p.item != null){
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }
    public T removeFirst(){
        Node r = Fsentinel.next;
        if(r == Lsentinel){
            return null;
        }
        Fsentinel.next = r.next;
        r.next.prev = Fsentinel;
        size = size - 1;
        return r.item;
    }
    public T removeLast(){
        Node r = Lsentinel.prev;
        if(r == Fsentinel){
            return null;
        }
        Lsentinel.prev = r.prev;
        r.prev.next = Lsentinel;
        size = size - 1;
        return r.item;
    }
    public T get(int index){
        if(index > size - 1){
            return null;
        }
        Node g = Fsentinel.next;
        while(index > 0){
            g = g.next;
            index -= 1;
        }
        return g.item;
    }
}
