package com.example.hrm.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Fragments.Performance.SelfPreviewDetail;
import com.example.hrm.Fragments.Staff.StaffTimeOffFragment;
import com.example.hrm.Response.PerformanceAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.databinding.PerformanceItemBinding;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class PerformanceAdpater extends RecyclerView.Adapter<PerformanceAdpater.PerformanceViewholder>{
    private List<PerformanceAttributes> data;
    private HomeActivity activity;
    public void setData(List<PerformanceAttributes> data, HomeActivity activity){
        this.data=data;
        this.activity = activity;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PerformanceViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PerformanceItemBinding binding=PerformanceItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PerformanceViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PerformanceViewholder holder, int position) {
        if (holder instanceof PerformanceViewholder) {
            PerformanceAttributes att = data.get(position);
            holder.binding.txtIndex.setText(String.valueOf(position + 1));
            holder.binding.txtName.setText(att.getStaff().getFullname());
            holder.binding.txtStatus.setText(att.getStatus());
            holder.binding.btnRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remind(att);
                }
            });
            holder.binding.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    show(att, holder.getAdapterPosition());
                }
            });
        }
    }

    private void show(PerformanceAttributes att,int pos) {
        SelfPreviewDetail SelfPreviewDetail=new SelfPreviewDetail(att,pos,false,false);
        final Bundle args = new Bundle();
        args.putString("TAG", "Review for Staff");
        SelfPreviewDetail.setArguments(args);
        activity.relaceFragment(SelfPreviewDetail);
    }

    private void remind(PerformanceAttributes att) {
        Call<ResponseBody> call = APIService.getService().remindPerformance(Common.getToken(),att.getId());
        try {
            Response<ResponseBody> res = call.execute();
            if(res.isSuccessful()){
                activity.showToast(true,"Send mail successfully");
            } else activity.showToast(false,"Send mail failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int getItemCount() {
        if(data!=null) return data.size();
        return 0;
    }

    class PerformanceViewholder extends RecyclerView.ViewHolder{
        PerformanceItemBinding binding;
        public PerformanceViewholder(@NonNull PerformanceItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
