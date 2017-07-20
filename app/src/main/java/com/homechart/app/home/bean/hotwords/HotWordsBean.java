package com.homechart.app.home.bean.hotwords;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/14.
 */

public class HotWordsBean implements Serializable {

    private List<String> hot_words;

    public HotWordsBean(List<String> hot_words) {
        this.hot_words = hot_words;
    }

    public List<String> getHot_words() {
        return hot_words;
    }

    public void setHot_words(List<String> hot_words) {
        this.hot_words = hot_words;
    }

    @Override
    public String toString() {
        return "HotWordsBean{" +
                "hot_words=" + hot_words +
                '}';
    }
}
