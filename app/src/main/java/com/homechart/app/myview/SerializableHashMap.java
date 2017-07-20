package com.homechart.app.myview;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/22.
 */

public class SerializableHashMap implements Serializable{

    private Map<String,String> map;

    public SerializableHashMap() {
    }

    public SerializableHashMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "SerializableHashMap{" +
                "map=" + map +
                '}';
    }
}
