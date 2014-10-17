package com.os1.common.model;


import org.json.JSONException;
import org.json.JSONObject;

public class IuserEntity {
    private int iuserId;

    private String logineName;

    private String displayName;
    private String userDesc;
    private String instagramUser;
    private String createTime;
private String lastUpdateTime;
    private String password;


    public void fromJson(JSONObject o) throws JSONException{
    /* {
        "iuserId": 2,
        "logineName": "postman_123",
        "displayName": null,
        "userDesc": null,
        "instagramUser": null,
        "createTime": "Tue Oct 07 23:40:31 EDT 2014",
        "lastUpdateTime": "Tue Oct 07 23:40:31 EDT 2014",
        "password": 123
    }*/


        iuserId = o.getInt("iuserId");
        logineName = o.getString("logineName");
        displayName = o.getString("displayName");
        userDesc = o.getString("userDesc");
        instagramUser = o.getString("instagramUser");
        createTime = o.getString("createTime");
        lastUpdateTime = o.getString("lastUpdateTime");
        password = o.getString("password");


    }
    public int getIuserId() {
        return iuserId;
    }

    public void setIuserId(int iuserId) {
        this.iuserId = iuserId;
    }



    public String getLogineName() {
        return logineName;
    }

    public void setLogineName(String logineName) {
        this.logineName = logineName;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }


    public String getInstagramUser() {
        return instagramUser;
    }

    public void setInstagramUser(String instagramUser) {
        this.instagramUser = instagramUser;
    }

    public String getCreateTime() {
        return createTime;
    }

public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IuserEntity that = (IuserEntity) o;

        if (iuserId != that.iuserId) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (instagramUser != null ? !instagramUser.equals(that.instagramUser) : that.instagramUser != null)
            return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
            return false;
        if (logineName != null ? !logineName.equals(that.logineName) : that.logineName != null) return false;
        if (userDesc != null ? !userDesc.equals(that.userDesc) : that.userDesc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = iuserId;
        result = 31 * result + (logineName != null ? logineName.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (userDesc != null ? userDesc.hashCode() : 0);
        result = 31 * result + (instagramUser != null ? instagramUser.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
