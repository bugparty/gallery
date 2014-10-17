package com.os1.common.model;

/**
 * Created by hanbowen on 2014/10/15.
 */
public class Users {
    public IuserEntity[] getUsers() {
        return users;
    }

    public void setUsers(IuserEntity[] users) {
        this.users = users;
    }

    public int getCur() {
        return cur;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    IuserEntity []users;
    int cur;
    private  Users(){
        users = new IuserEntity[2];
        users[0] = new IuserEntity();
        users[0].setIuserId(3);
        users[0].setLogineName("刘曼");
        users[1] = new IuserEntity();
        users[1].setIuserId(123);
        users[1].setLogineName("博文");
        cur = 1;
    }

    private static Users u;
    public static IuserEntity getCurrent(){
        if (u == null){
            u = new Users();
        }
        return u.getUsers()[u.getCur()];
    }
    public static IuserEntity getNext(){
        if (u == null){
            u = new Users();
        }
        u.setCur(u.getCur()+1 ==2?0:1);
        return u.getUsers()[u.getCur()];
    }

}
