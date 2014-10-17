package com.os1.common.net;

import com.os1.common.model.IuserEntity;
import com.os1.common.model.Users;

/**
 * Created by hanbowen on 2014/10/17.
 */
public class NetFactory {
    public static INet create(){
        IuserEntity user = Users.getCurrent();
        INet net = new InternalNet(user);
        return net;
    }
}
