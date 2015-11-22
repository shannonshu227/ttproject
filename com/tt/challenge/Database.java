package com.tt.challenge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shannon on 11/21/15.
 */
public class Database {
    Map<String, Integer> data;
    ValueFreq vf;
    private Database prev;

    public Database() {
        this.data = new HashMap<>();
        this.prev = null;
        this.vf = ValueFreq.getInstance();

    }

    public Database(Database prev) {
        this();
        this.prev = prev;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public Database getPrev() {
        return prev;
    }

    public Integer get(String key) {

        return getCurrent(key);
    }

    public void set(String key, int value) {
        boolean exist = false;

        Integer v = getCurrent(key);
        if (v != null) {
            exist = true;
        }

        data.put(key, value);

        if (exist) { //update curr val's freq
            vf.decrease(v);
        }


        //update new value's freq
        vf.increase(value);
    }

    public void unset(String key) {
        Integer v = getCurrent(key);
        data.put(key, null);
        if (v == null) {
            return;
        } else {
            //update curr val's freq
            vf.decrease(v);
        }
    }

    public Integer numEqualsTo(int value) {
        if (!vf.getFreq().containsKey(value)) {
            return 0;
        }
        return vf.getFreq().get(value);
    }

    public Database commit() {
        Database curr = this;
        Database prev = this.prev;

        Database p = null;
        while (prev != null) {
            p = prev;
            Map<String, Integer> currDb = curr.getData();
            Map<String, Integer> prevDb = prev.getData();

            for (String s : currDb.keySet()) {
                prevDb.put(s, currDb.get(s));
            }

            curr = prev;
            prev = prev.prev;
        }

        return p;
    }

    public Database rollback() {
        vf.rollback(this);
        return prev;
    }

    public Integer getCurrent(String key) {
        Integer v = null;
        if (!data.containsKey(key) && prev != null) {
            Database curr = prev;
            while (curr != null) {
                if (curr.data.containsKey(key)) {
                    v = curr.data.get(key);
                    break;
                }
                curr = prev.prev;
            }

            return v;
        } else {
            return data.get(key);
        }
    }

}
