package com.liansu.boduowms.bean.base;

/**
 * @ Des: 常用的方法解析结果返回类
 * @ Created by yangyiqing on 2019/8/2.
 */
public class BaseMultiResultInfo<K,V> {
    private K      headerStatus;
    private V      info;
    private String message;


    public BaseMultiResultInfo(K headerStatus){
        this.headerStatus=headerStatus;
        message="";
    }
    public BaseMultiResultInfo(){
        message="";
    }
    public K getHeaderStatus() {
        return headerStatus;
    }

    public void setHeaderStatus(K headerStatus) {
        this.headerStatus = headerStatus;
    }

    public V getInfo() {
        return info;
    }

    public void setInfo(V info) {
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
