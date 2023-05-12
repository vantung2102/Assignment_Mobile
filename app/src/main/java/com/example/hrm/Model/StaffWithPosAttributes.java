package com.example.hrm.Model;

import com.example.hrm.Response.LeaveApplicationAttributes;
import com.example.hrm.Response.StaffAttributes;

public class StaffWithPosAttributes {
    StaffAttributes property;
    int positon;

    public StaffWithPosAttributes(StaffAttributes property, int positon) {
        this.property = property;
        this.positon = positon;
    }

    public StaffAttributes getLeaveApp() {
        return property;
    }

    public void setLeaveApp(StaffAttributes property) {
        this.property = property;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }
}
