package com.example.hrm.Fragments.LeaveApplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Model.LeaveAppWithPosAttributes;
import com.example.hrm.Model.PropertyWithPosAttributes;
import com.example.hrm.R;
import com.example.hrm.Response.DataResponse;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.LeaveApplicationAttributes;
import com.example.hrm.Response.Staff;
import com.example.hrm.Services.APIService;
import com.example.hrm.ViewModel.LeaveAppShareViewModel;
import com.example.hrm.ViewModel.PropertyShareViewModel;
import com.example.hrm.databinding.FragmentDetailLeaveApplicationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailLeaveApplicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailLeaveApplicationFragment extends Fragment {
    public  static final String MY_TAG= "Detail Leave Application";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailLeaveApplicationFragment() {
        // Required empty public constructor
    }
    private LeaveApplicationAttributes att;
    private String type;
    private  int pos;
    public DetailLeaveApplicationFragment(LeaveApplicationAttributes att,int pos) {
    this.pos=pos;
        this.att=att;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailLeaveApplicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailLeaveApplicationFragment newInstance(String param1, String param2) {
        DetailLeaveApplicationFragment fragment = new DetailLeaveApplicationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            this.type=getArguments().getString("TYPE", Common.STATUS_PENDING);
            Log.d("type",type);
        }
    }
    FragmentDetailLeaveApplicationBinding binding;
    LeaveAppShareViewModel leaveAppShareViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
         leaveAppShareViewModel = new ViewModelProvider(getActivity()).get(LeaveAppShareViewModel.class);
        binding=FragmentDetailLeaveApplicationBinding.inflate(inflater);
        binding.txtStartDate.setText(att.getStartDay());
        binding.txtEndDate.setText(att.getEndDay());
        binding.txtLeaveType.setText(att.getLeaveType());
        binding.txtReason.setText(att.getDescription());
        binding.txtNumberOfDay.setText(att.getNumberOfDaysOff().toString());
        binding.txtRequester.setText(att.getStaff().getFullname());
        Staff staff= (Staff) att.getApprover();
        binding.txtApprover.setText(att.getApprover()==null?"None": staff.getFullname());
        binding.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseLeaveApplication(true);
            }
        });
        binding.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseLeaveApplication(false);
            }
        });
        updateButton();
        return binding.getRoot();
    }

    private void updateButton() {
        if(this.type.equals(Common.STATUS_APPROVED)){
            binding.txtStatus.setBackground(getContext().getDrawable(R.drawable.layout_rounded_border_green));
            binding.txtStatus.setTextColor(getContext().getColor(R.color.toast_success_bold));
            binding.txtStatus.setText(Common.STATUS_APPROVED);
            binding.btnCancle.setVisibility(View.GONE);
            binding.btnApprove.setVisibility(View.GONE);
        }
        else if(this.type.equals(Common.STATUS_CANCLED)){
            binding.txtStatus.setBackground(getContext().getDrawable(R.drawable.layout_rounded_border_red));
            binding.txtStatus.setTextColor(getContext().getColor(R.color.toast_failed_bold));
            binding.txtStatus.setText(Common.STATUS_CANCLED);
            binding.btnCancle.setVisibility(View.GONE);
            binding.btnApprove.setVisibility(View.GONE);
        } else if(this.type.equals(Common.STATUS_PENDING)){
            binding.txtStatus.setBackground(getContext().getDrawable(R.drawable.layout_rounded_border_yellow));
            binding.txtStatus.setTextColor(getContext().getColor(R.color.pending));
        }

    }

    private void responseLeaveApplication(boolean b) {
        JSONObject parent=new JSONObject();
        JSONObject child=new JSONObject();
        try {
            child.put("status",b?1:2);
            parent.put("leave_application",child);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
            Call<DataResponse<DatumTemplate<LeaveApplicationAttributes>>> call= APIService.getService().respond_to_leave_application(att.getId(),Common.getToken(),body);
            call.enqueue(new Callback<DataResponse<DatumTemplate<LeaveApplicationAttributes>>>() {
                @Override
                public void onResponse(Call<DataResponse<DatumTemplate<LeaveApplicationAttributes>>> call, Response<DataResponse<DatumTemplate<LeaveApplicationAttributes>>> response) {
                    if(response.isSuccessful()){
                        Log.d("response","approved");
                        if(response.body().getData().getAttributes().getStatus().equals(Common.STATUS_APPROVED)){
                            Log.d("doSome","STATUS_APPROVED");
                                UpdateButton(true);

                            ((HomeActivity)getActivity()).showToast(true,"Approve Leave Application Success!");
                        } else if(response.body().getData().getAttributes().getStatus().equals(Common.STATUS_CANCLED)){
                            Log.d("doSome","STATUS_CANCLED");
                            UpdateButton(false);
                            ((HomeActivity)getActivity()).showToast(true,"Cancle Leave Application Success!");
                        }
                        LeaveAppWithPosAttributes propertyWithPosAttributes=new LeaveAppWithPosAttributes(response.body().getData().getAttributes(),pos);
                        leaveAppShareViewModel.setLeaveApp(propertyWithPosAttributes);
                    } else {
                        Log.d("response", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<DataResponse<DatumTemplate<LeaveApplicationAttributes>>> call, Throwable t) {
                    Log.d("response", t.getMessage());
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public interface IDoStuff{
        void doSome();
    }
    private void UpdateButton(boolean b) {
        IDoStuff iDoStuff=new IDoStuff() {
            @Override
            public void doSome() {
                Log.d("doSome","doSome");
                Update(b);
            }
        };
        if(Common.getStaff()==null) Common.getCurrentUser(iDoStuff);
        else {
            Update(b);
        }

    }

    private void Update(boolean b) {
        binding.btnCancle.setVisibility(View.GONE);
        binding.btnApprove.setVisibility(View.GONE);
        binding.txtApprover.setText(Common.getStaff().getFullname());
        if(b){
            binding.txtStatus.setText(Common.STATUS_APPROVED);
        } else {
            binding.txtStatus.setText(Common.STATUS_CANCLED);
        }
    }
}