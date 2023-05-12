package com.example.hrm.Model;

import com.example.hrm.Response.LeaveApplicationAttributes;
import com.example.hrm.Response.PropertyAttributes;

public class LeaveAppWithPosAttributes {
    LeaveApplicationAttributes property;
    int positon;

    public LeaveAppWithPosAttributes(LeaveApplicationAttributes property, int positon) {
        this.property = property;
        this.positon = positon;
    }

    public LeaveApplicationAttributes getLeaveApp() {
        return property;
    }

    public void setLeaveApp(LeaveApplicationAttributes property) {
        this.property = property;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }
}
