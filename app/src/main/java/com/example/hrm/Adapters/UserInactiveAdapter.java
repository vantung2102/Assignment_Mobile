package com.example.hrm.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrm.Common;
import com.example.hrm.Constant;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Fragments.Staff.StaffFragmentView;
import com.example.hrm.R;
import com.example.hrm.Response.DatumStaff;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.databinding.InactiveStaffItemBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInactiveAdapter extends RecyclerView.Adapter<UserInactiveAdapter.StaffViewHolder>{
    List<DatumStaff> data;
    private LayoutInflater layoutInflater;
    Context mContext;
    String type;
    HomeActivity homeActivity; StaffFragmentView view;
    private boolean isShowCurrentList=false;
    private List<DatumStaff> currentList;
    private String showLikeKey;

    public UserInactiveAdapter(StaffFragmentView view) {
        this.view = view;
    }

    public UserInactiveAdapter() {
    }

    public void setData(List<DatumStaff> desDepartments, Context mContext, HomeActivity homeActivity){
        this.homeActivity=homeActivity;
        this.type=type;
        this.data=desDepartments;
        this.mContext=mContext;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InactiveStaffItemBinding staffItemBinding=InactiveStaffItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return  new StaffViewHolder(staffItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {

        StaffAttributes att;
        if(!isShowCurrentList)
        {
            att=data.get(position).getAttributes();
        } else {
            att=currentList.get(position).getAttributes();
        }
        String type=data.get(position).getType();
        if(att== null) return;
        if(type.equals(Constant.DEPARTMENT_TYPE)){
            //holder.staffItemBinding.imgAvar.setText(att.getName());
        }
        else if(type.equals(Constant.JOBTITLE_TYPE)){

        }
        else if(type.equals(Constant.POSITION_TYPE)){

        }
        holder.staffItemBinding.txtName.setText(att.getFullname());

        holder.staffItemBinding.txtIndex.setText(String.valueOf(position));
        holder.staffItemBinding.txtId.setText(String.valueOf(att.getId()));

        holder.staffItemBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverStaff(att,holder.getAdapterPosition());
            }
        });
        holder.staffItemBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelete(att);
            }
        });
    }

    private void removeItem(StaffAttributes att, int position) {
        Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
        data.remove(position);
        notifyItemRemoved(position);
    }

    private void recoverStaff(StaffAttributes att,int pos) {
        Call<ResponseBody> call = APIService.getService().recover_staff(Common.getToken(), att.getId());
        try {
            Response response=call.execute();
            if(response.isSuccessful()){
                data.remove(pos);
                notifyItemRemoved(pos);
                homeActivity.showToast(true,"Successfully!");
            } else homeActivity.showToast(false,"Failed!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void showDelete(StaffAttributes department) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Delete Inactive Staff");
        String str="Are you sure delete '"+department.getFullname()+"' ?";
        alertDialog.setMessage(str);
        alertDialog.setIcon(R.drawable.deletetrash);
        alertDialog.setCancelable(true);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permanceDestroy(department);
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void permanceDestroy(StaffAttributes department) {
        Call<ResponseBody> call = APIService.getService().permanceDestroy(Common.getToken(), department.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    homeActivity.showToast(true,"Delete Successfully!");
                } else homeActivity.showToast(false,"Delete Failed!");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void showEditForm(StaffAttributes staff) {
        view.showFormAddDepartment(staff);
    }

    @Override
    public int getItemCount() {
        if(isShowCurrentList) {
            if(currentList!=null) return currentList.size();
            return 0;
        }
        if(data!=null) return data.size();
        return 0;
    }

    public void showLike(String staffName,String desName,String posName,String jobName) {
        isShowCurrentList=true;
        List<DatumStaff> list=new ArrayList<>();
        this.data.forEach(item->{
            if(item.getAttributes().getFullname().contains(staffName)&&item.getAttributes().getPosition().getName().contains(posName)&&item.getAttributes().getJobTitle().getTitle().contains(jobName)&&item.getAttributes().getDepartment().getName().contains(desName)){
                list.add(item);
            }
        });
        this.currentList=list;
        notifyDataSetChanged();
    }

    public void showAll() {
        this.isShowCurrentList=false;
        notifyDataSetChanged();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder{
        InactiveStaffItemBinding staffItemBinding;
        public StaffViewHolder(@NonNull InactiveStaffItemBinding staffItemBinding) {
            super(staffItemBinding.getRoot());
            this.staffItemBinding=staffItemBinding;
        }
        //DepartmentItemBinding departmentItemBinding;

    }
}
