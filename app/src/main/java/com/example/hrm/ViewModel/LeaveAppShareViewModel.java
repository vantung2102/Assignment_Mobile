package com.example.hrm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hrm.Model.LeaveAppWithPosAttributes;
import com.example.hrm.Model.PropertyWithPosAttributes;

public class LeaveAppShareViewModel extends ViewModel {
    MutableLiveData<LeaveAppWithPosAttributes> propertyData=new MutableLiveData<>();
    MutableLiveData<Integer> positionData=new MutableLiveData<>();
    public LiveData<LeaveAppWithPosAttributes> getLeaveApp() {
        return propertyData;
    }
    public void setLeaveApp(LeaveAppWithPosAttributes staff) {
        propertyData.setValue(staff);
    }

}
