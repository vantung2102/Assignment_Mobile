package com.example.hrm.Model;

import com.example.hrm.Response.RequestPropertyAttributes;
import com.example.hrm.Response.StaffAttributes;

public class RequestWithPosAttributes {
    RequestPropertyAttributes property;
    RequestPropertyAttributes old;
    int positon;
    int action;

    public RequestWithPosAttributes(RequestPropertyAttributes property, RequestPropertyAttributes old,int positon,int action) {
        this.old=old;
        this.action=action;
        this.property = property;
        this.positon = positon;
    }

    public RequestPropertyAttributes getLeaveApp() {
        return property;
    }

    public void setLeaveApp(RequestPropertyAttributes property) {
        this.property = property;
    }

    public int getPositon() {
        return positon;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public RequestPropertyAttributes getOldData() {
        return this.old;
    }
}
