package tw.lychee.swipecurvelayout;

import java.util.ArrayList;


class CircularArrayList<T> {
    private ArrayList<T> mItems;
    CircularArrayList() {
        mItems = new ArrayList<>();
    }

    void add(T item) {
        mItems.add(item);
    }

    T get(int idx) {
        while (idx < 0)
            idx += mItems.size();
        idx = idx % mItems.size();
        return mItems.get(idx);
    }

    ArrayList<T> getAll() {
        return mItems;
    }

    int size() {
        return mItems.size();
    }
}
