package com.example.hrm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hrm.Model.RequestWithPosAttributes;
import com.example.hrm.Model.StaffWithPosAttributes;
import com.example.hrm.Response.StaffAttributes;

public class RequestShareViewModel extends ViewModel {
    MutableLiveData<RequestWithPosAttributes> propertyData=new MutableLiveData<>();
    MutableLiveData<Integer> positionData=new MutableLiveData<>();
    public LiveData<RequestWithPosAttributes> getLeaveApp() {
        return propertyData;
    }
    public void setLeaveApp(RequestWithPosAttributes staff) {
        propertyData.setValue(staff);
    }
}
