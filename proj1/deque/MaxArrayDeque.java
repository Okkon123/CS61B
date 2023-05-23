package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> vs;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        vs = c;
    }
    public T max() {
        T result = get(0);
        for (int i = 0; i < size(); i++) {
            if (vs.compare(get(i), result) > 0) {
                result = get(i);
            }
        }
        return result;
    }

    public T max(Comparator<T> c) {
        T result = get(0);
        for (int i = 0; i < size(); i++) {
            if (c.compare(get(i), result) > 0) {
                result = get(i);
            }
        }
        return result;
    }
}
