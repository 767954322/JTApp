package com.homechart.app.home.bean.allset;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/25.
 */

public class AllSetBean implements Serializable {

    private ConfigInfoBean config_info;
    private VersionInfo version_info;

    public AllSetBean(ConfigInfoBean config_info, VersionInfo version_info) {
        this.config_info = config_info;
        this.version_info = version_info;
    }

    public ConfigInfoBean getConfig_info() {
        return config_info;
    }

    public void setConfig_info(ConfigInfoBean config_info) {
        this.config_info = config_info;
    }

    public VersionInfo getVersion_info() {
        return version_info;
    }

    public void setVersion_info(VersionInfo version_info) {
        this.version_info = version_info;
    }

    @Override
    public String toString() {
        return "AllSetBean{" +
                "config_info=" + config_info +
                ", version_info=" + version_info +
                '}';
    }
}
