package com.liansu.boduowms.bean.base;

/**
 * @ Des: 调用后台接口返回基类
 * @ Created by yangyiqing on 2020/7/8.
 */
public class BaseResultInfo<T> {
    public static final int    RESULT_TYPE_OK              = 1;
    public static final int    RESULT_TYPE_ACTION_CONTINUE = 2; //发起再次请求
    public static final int    RESULT_TYPE_POST_SUCCESS = 3; //过账成功
    public static final int    RESULT_TYPE_ERROR           = 0;
    public static final int    RESULT_TYPE_DEFAULT         = -1;
    private             int    Result;
    private             String ResultValue;
    private             T      Data;

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getResultValue() {
        return ResultValue;
    }

    public void setResultValue(String resultValue) {
        ResultValue = resultValue;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
