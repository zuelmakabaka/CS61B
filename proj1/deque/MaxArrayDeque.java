package deque;
import java.util.Comparator;
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T maxitem = get(0);
        for (T i : items) {
            if (cmp.compare(i, maxitem) > 0) {
                maxitem = i;
            }
        }
        return maxitem;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxitem = get(0);
        for (T i : items) {
            if (c.compare(i, maxitem) > 0) {
                maxitem = i;
            }
        }
        return maxitem;
    }
}
