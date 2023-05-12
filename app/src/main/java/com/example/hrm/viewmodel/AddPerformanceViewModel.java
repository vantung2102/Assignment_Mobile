package com.example.hrm.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.hrm.BR;

import java.util.Calendar;

public class AddPerformanceViewModel extends BaseObservable {
    private Calendar calendarStart;
    private Calendar calendarEnd;

    public Calendar getCalendarStart() {
        return calendarStart;
    }

    public void setCalendarStart(Calendar calendarStart) {
        this.calendarStart = calendarStart;
    }

    public Calendar getCalendarEnd() {
        return calendarEnd;
    }

    public void setCalendarEnd(Calendar calendarEnd) {
        this.calendarEnd = calendarEnd;
    }

    private  String leaveType;
    private  String startDate;
    private  String dueDay;
    private  String numberDay;
    private  String reason;
    private  final int leaveTypeTYPE=1;
    private  final int startDateTYPE=2;
    private  final int  dueDayTYPE=3;
    private  final int numberDayTYPE=4;
    private  final  int reasonTYPE=5;
    private  boolean leaveTypeVi;
    private  boolean startDateVi;
    private  boolean dueDayVi;
    private  boolean numberDayVi;
    private  boolean reasonVi;
    private boolean isSubmited=false;
    void check(int type){
        switch (type){
            case startDateTYPE:
                if(TextUtils.isEmpty(this.startDate)) setStartDateVi(true); else setStartDateVi(false);
                break;
            case dueDayTYPE:
                Log.d("checkAll","dueDayTYPE");
                if(TextUtils.isEmpty(this.dueDay)) setDueDayVi(true); else setDueDayVi(false);break;
        }
    }
    @Bindable
    public boolean isLeaveTypeVi() {
        return leaveTypeVi;
    }

    public boolean isSubmited() {
        return isSubmited;
    }

    public void setSubmited(boolean submited) {
        isSubmited = submited;
    }

    public void setLeaveTypeVi(boolean leaveTypeVi) {
        this.leaveTypeVi = leaveTypeVi;
        notifyPropertyChanged(BR.leaveTypeVi);
    }
    @Bindable
    public boolean isStartDateVi() {
        return startDateVi;
    }

    public void setStartDateVi(boolean startDateVi) {
        this.startDateVi = startDateVi;
        notifyPropertyChanged(BR.startDateVi);
    }
    @Bindable
    public boolean isDueDayVi() {
        return dueDayVi;
    }

    public void setDueDayVi(boolean dueDayVi) {
        Log.d("checkAll","setDueDayVi");
        this.dueDayVi = dueDayVi;
        notifyPropertyChanged(BR.dueDayVi);
    }
    @Bindable
    public boolean isNumberDayVi() {
        return numberDayVi;
    }

    public void setNumberDayVi(boolean numberDayVi) {
        this.numberDayVi = numberDayVi;
        notifyPropertyChanged(BR.dueDayVi);
    }
    @Bindable
    public boolean isReasonVi() {
        return reasonVi;
    }

    public void setReasonVi(boolean reasonVi) {
        this.reasonVi = reasonVi;
        notifyPropertyChanged(BR.reasonVi);
    }

    public AddPerformanceViewModel(String leaveType, String startDate, String dueDay, String numberDay, String reason) {
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.dueDay = dueDay;
        this.numberDay = numberDay;
        this.reason = reason;
    }

    public AddPerformanceViewModel() {
    }
    @Bindable
    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
        notifyPropertyChanged(BR.leaveType);
        check(leaveTypeTYPE);
    }
    @Bindable
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        notifyPropertyChanged(BR.startDate);
        check(startDateTYPE);
        Log.d("checkAll","setStartDate");

    }
    @Bindable
    public String getDueDay() {
        return dueDay;
    }

    public void setDueDay(String dueDay) {
        if(dueDay.isEmpty()) return;
        this.dueDay = dueDay;
        notifyPropertyChanged(BR.dueDay);
        Log.d("checkAll","setDueDay");
        check(dueDayTYPE);
    }
    @Bindable
    public String getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(String numberDay) {
        this.numberDay = numberDay;
        check(numberDayTYPE);
    }
    @Bindable
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
        notifyPropertyChanged(BR.reason);
        check(reasonTYPE);
    }

    public boolean checkAll() {
        Log.d("checkAll",this.toString());
        boolean result=true;
        if(TextUtils.isEmpty(this.startDate)) {setStartDateVi(true);result=false;} else setStartDateVi(false);
        if(TextUtils.isEmpty(this.dueDay)) {setDueDayVi(true);result=false;} else setDueDayVi(false);
        return result;
    }

    @Override
    public String toString() {
        return "AddLeaveViewModel{" +
                "leaveType='" + leaveType + '\'' +
                ", startDate='" + startDate + '\'' +
                ", dueDay='" + dueDay + '\'' +
                ", numberDay='" + numberDay + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
