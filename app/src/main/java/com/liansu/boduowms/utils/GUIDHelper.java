package com.liansu.boduowms.utils;

import java.util.UUID;

public class GUIDHelper {
    protected String                       mUuid   = null;//每次进入界面只存在一个guiid
    protected boolean Return;
    protected  boolean isPost;//是否点击过过账按钮   该参数重置于返回方法里面，用来判断过账是否连接超时

    public GUIDHelper(){
        initGUID();
    }

    public String getmUuid() {
        return mUuid;
    }

    public void setmUuid(String mUuid) {
        this.mUuid = mUuid;
    }

    public boolean isReturn() {
        return Return;
    }
    //-99 可以退出  ，成功可以退出  失败不允许退出
    public void setReturn(boolean aReturn) {
        Return = aReturn;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }


    public void  initGUID(){
        mUuid= UUID.randomUUID().toString();
        Return=true;
        isPost=false;
    }

  public void createUUID(){
      mUuid= UUID.randomUUID().toString();
  }


}
