package com.example.greg.gpssportraker.history.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {


    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();


    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "2015.12.17.       13:01 \nDistance:           312 m \nDuration:           00:04:43"));
        addItem(new DummyItem("2", "2015.12.18.       17:41 \nDistance:           754 m \nDuration:           00:15:11"));
        addItem(new DummyItem("3", "2015.12.19.       11:34 \nDistance:           1955 m \nDuration:          01:04:31"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
