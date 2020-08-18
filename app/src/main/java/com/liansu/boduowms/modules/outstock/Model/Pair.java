package com.liansu.boduowms.modules.outstock.Model;


//键值对 下拉框
 public class Pair {
    public String key;
    public String value;

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return key;
    }


}
