package com.file;

import java.util.List;

public class LinuxBundle {

    private List<String> core;  //  List
    private List<String> java;  //  List
    private List<String> snmp;  //  List
    private List<String> remote;  //  List

    // getters , setters, some boring stuff

    public List<String> getCore() {
        return core;
    }

    public void setCore(List<String> core) {
        this.core = core;
    }

    public List<String> getJava() {
        return java;
    }

    public void setJava(List<String> java) {
        this.java = java;
    }

    public List<String> getSnmp() {
        return snmp;
    }

    public void setSnmp(List<String> snmp) {
        this.snmp = snmp;
    }

    public List<String> getRemote() {
        return remote;
    }

    public void setRemote(List<String> remote) {
        this.remote = remote;
    }
}
