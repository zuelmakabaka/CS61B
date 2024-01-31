package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    int nextFirst;
    int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 2;
        nextLast = 3;
        size = 0;
    }
    public void resize(int length){
        T[] a = (T[]) new Object[length];
        int first;
        if(nextFirst == items.length - 1){
            first = 0;
        }
        else{
            first = nextFirst + 1;
        }
        int last;
        if(nextLast == 0){
            last = items.length - 1;
        }
        else{
            last = nextLast - 1;
        }
        if(first < last){
            System.arraycopy(items, 0, a, 0, items.length);
            items = a;
        }
        else{
            System.arraycopy(items,0,a,0,last + 1);
            System.arraycopy(items,first,a,a.length - items.length + first,items.length - first);
        }
    }

    public void addFirst(T item){
        if(size == items.length){
            resize(2*size);
        }
        items[nextFirst] = item;
        if(nextFirst == 0){
            nextFirst = items.length - 1;
        }
        else{
            nextFirst = nextFirst - 1;
        }
        size += 1;
    }
    public void addLast(T item){
        if(size == items.length){
            resize(2*size);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1)% items.length;
        size += 1;
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
        int index = (nextFirst + 1)%items.length;
        for(int i = 0;i<size;i += 1){
            System.out.print(items[index] + " ");
            index = (index + 1)% items.length;
        }
        System.out.println();
    }
    public T removeLast(){
        if(size == 0){
            return items[0];
        }
        if (size < (items.length/4) && items.length > 16){
            resize(items.length/2);
        }
        if(nextLast == 0){
            nextLast = items.length - 1;
        }
        else{
            nextLast = nextLast - 1;
        }
        size = size - 1;
        T index = items[nextLast];
        items[nextLast] = null;
        return index;
    }
    public T removeFirst(){
        if(size == 0){
            return items[0];
        }
        if(size < (items.length/4) && items.length > 16) {
            resize(items.length / 2);
        }
        if(nextFirst == items.length - 1){
            nextFirst = 0;
        }
        else{
            nextFirst = nextFirst + 1;
        }
        size = size - 1;
        return items[nextFirst];
    }
    public T get(int index){
        return items[index];
    }
}
