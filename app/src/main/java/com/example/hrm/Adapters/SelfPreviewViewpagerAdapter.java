package com.example.hrm.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hrm.Common;
import com.example.hrm.Fragments.Performance.SelfPreviewFinishedFragment;
import com.example.hrm.Fragments.Performance.SelfPreviewInProgressFragment;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.PerformanceAttributes;

import java.util.ArrayList;
import java.util.List;

public class SelfPreviewViewpagerAdapter extends FragmentStateAdapter {
    List<PerformanceAttributes> inProgress=new ArrayList<>();
    List<PerformanceAttributes> finished=new ArrayList<>();
    private boolean staffEdit,bossEdit;
    public SelfPreviewViewpagerAdapter(@NonNull FragmentActivity fragmentActivity, List<DatumTemplate<PerformanceAttributes>> data,boolean staffEdit,boolean bossEdit) {
        super(fragmentActivity);
        this.staffEdit=staffEdit;
        this.bossEdit=bossEdit;
        data.forEach(item->{if(item.getAttributes().getActive()!=null) inProgress.add(item.getAttributes()); else finished.add(item.getAttributes());});
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new SelfPreviewFinishedFragment(finished);
            default:
                return new SelfPreviewInProgressFragment(inProgress,staffEdit,bossEdit);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
