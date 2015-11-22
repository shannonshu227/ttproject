package com.tt.challenge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shannon on 11/21/15.
 */
public class ValueFreq {
    private static ValueFreq instance = null;
    private Map<Integer, Integer> freq;

    private ValueFreq() {
        this.freq = new HashMap<>();
    }

    public static ValueFreq getInstance() {
        if (instance == null) {
            synchronized (ValueFreq.class) {
                if (instance == null) {
                    instance = new ValueFreq();
                }
            }
        }
        return instance;
    }

    public Map<Integer, Integer> getFreq() {
        return freq;
    }

    public void increase(int key) {
        if (freq.containsKey(key)) {
            freq.put(key, freq.get(key) + 1);
        } else {
            freq.put(key, 1);
        }
    }

    public void decrease(int key) {
        int count = freq.get(key) - 1;
        if (count == 0) {
            freq.remove(key);
        } else {
            freq.put(key, count);
        }
    }

    public void rollback(Database database) {
        if (database.getPrev() == null) {
            System.out.println("NO TRANSACTION");
            return;
        }
        Map<String, Integer> data = database.getData();

        Database prevDb = database.getPrev();
        for (String key : data.keySet()) {
            Integer currVal = data.get(key);
            Integer prevVal = prevDb.getCurrent(key);

            if (currVal == null && prevVal != null) {
                increase(prevVal);
            } else {
                // normal case
                if (prevVal == null) {
                    decrease(currVal);
                } else {
                    if (prevVal == currVal) {
                        continue;
                    } else {
                        decrease(currVal);
                        increase(prevVal);
                    }
                }
            }
        }
    }

}
