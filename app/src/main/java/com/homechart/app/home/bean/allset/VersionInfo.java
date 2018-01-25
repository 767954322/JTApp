package com.homechart.app.home.bean.allset;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/25.
 */

public class VersionInfo implements Serializable {

    private String last_version;
    private List<String> text_content;
    private String download_url;


    public VersionInfo(String last_version, List<String> text_content, String download_url) {
        this.last_version = last_version;
        this.text_content = text_content;
        this.download_url = download_url;
    }

    public String getLast_version() {
        return last_version;
    }

    public void setLast_version(String last_version) {
        this.last_version = last_version;
    }

    public List<String> getText_content() {
        return text_content;
    }

    public void setText_content(List<String> text_content) {
        this.text_content = text_content;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "last_version='" + last_version + '\'' +
                ", text_content=" + text_content +
                ", download_url='" + download_url + '\'' +
                '}';
    }
}
