package com.example.hrm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hrm.Model.LeaveAppWithPosAttributes;
import com.example.hrm.Model.StaffWithPosAttributes;
import com.example.hrm.Response.StaffAttributes;

public class StaffInActiveShareViewModel extends ViewModel {
    MutableLiveData<StaffWithPosAttributes> propertyData=new MutableLiveData<>();
    MutableLiveData<Integer> positionData=new MutableLiveData<>();
    public LiveData<StaffWithPosAttributes> getLeaveApp() {
        return propertyData;
    }
    public void setLeaveApp(StaffWithPosAttributes staff) {
        propertyData.setValue(staff);
    }
}
