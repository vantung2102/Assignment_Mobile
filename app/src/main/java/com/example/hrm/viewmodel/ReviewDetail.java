package com.example.hrm.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.hrm.BR;

public class ReviewDetail  extends BaseObservable {
    public ReviewDetail(boolean staffEdit, boolean bossEdit) {
        this.staffEdit = staffEdit;
        this.bossEdit = bossEdit;
    }

    private boolean staffEdit=false;
    private boolean bossEdit=false;
    @Bindable
    public boolean isStaffEdit() {
        return staffEdit;
    }

    public void setStaffEdit(boolean staffEdit) {
        this.staffEdit = staffEdit;
        notifyPropertyChanged(BR.staffEdit);
    }
    @Bindable
    public boolean isBossEdit() {
        return bossEdit;
    }

    public void setBossEdit(boolean bossEdit) {
        this.bossEdit = bossEdit;
        notifyPropertyChanged(BR.bossEdit);
    }
}
