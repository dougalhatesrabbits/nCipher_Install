/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.file;

import java.util.List;
import java.util.Map;

public class SecWorld {
    private String[] linuxSearch;    //  Array
    private List<String> windowsSearch;  //  List
    private Map<String, Integer> versions;       //  Map
    private Map<String, String> wget;       //  Map
    private Map<String, String> config;       //  Map

    // getters , setters, some boring stuff
    public String[] getLinuxSearch() {
        return linuxSearch;
    }

    public void setLinuxSearch(String[] linuxSearch) {
        this.linuxSearch = linuxSearch;
    }

    public List<String> getWindowsSearch() {
        return windowsSearch;
    }

    public void setWindowsSearch(List<String> windowsSearch) {
        this.windowsSearch = windowsSearch;
    }

    public Map<String, Integer> getVersions() {
        return versions;
    }

    public void setVersions(Map<String, Integer> versions) {
        this.versions = versions;
    }

    public Map<String, String> getWget() {
        return wget;
    }

    public void setWget(Map<String, String> wget) {
        this.wget = wget;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }


}
