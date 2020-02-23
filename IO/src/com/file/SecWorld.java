package com.file;

import java.util.List;
import java.util.Map;

public class SecWorld {
    private String[]             linuxSearch;    //  Array
    private List<String>         windowsSearch;  //  List
    private Map<String, Integer> versions;       //  Map

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
}
