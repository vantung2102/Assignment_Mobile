package com.example.hrm.Fragments.Staff;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrm.Adapters.StaffInfoAdapter;
import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Model.StaffWithPosAttributes;
import com.example.hrm.R;
import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.ViewModel.StaffInActiveShareViewModel;
import com.example.hrm.ViewModel.StaffShareViewModel;
import com.example.hrm.databinding.FragmentStaffInfoBinding;
import com.example.hrm.viewmodel.NewEmployeeViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffInfoFragment extends Fragment {
    public  static final String MY_TAG= "Staff Info";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StaffInfoFragment() {
        // Required empty public constructor
    }
    private StaffAttributes staffAttributes;
    private int pos;
    public StaffInfoFragment(StaffAttributes staffAttributes,int pos) {
        // Required empty public constructor
        this.staffAttributes=staffAttributes;
        this.pos=pos;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffInfoFragment newInstance(String param1, String param2) {
        StaffInfoFragment fragment = new StaffInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    StaffInActiveShareViewModel staffShareViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        staffShareViewModel = new ViewModelProvider(getActivity()).get(StaffInActiveShareViewModel.class);
    }
    ViewPager2 viewPager2;
    StaffInfoAdapter staffInfoAdapter;
    View mView;
    TextView txtName,txtId;
    FragmentStaffInfoBinding fragmentStaffInfoBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_staff_info, container, false);
        txtName=mView.findViewById(R.id.txt_name);
        txtId=mView.findViewById(R.id.txt_staff_id);
        fragmentStaffInfoBinding=FragmentStaffInfoBinding.inflate(inflater);
        fragmentStaffInfoBinding.btnInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staffAttributes.getLowerLevels()==null||staffAttributes.getLowerLevels().isEmpty()) showConfirmDialog(staffAttributes,getContext(),pos);
                else showConfirmTranManager(staffAttributes,getContext(),pos);
            }
        });
        staffInfoAdapter=new StaffInfoAdapter(this,this.staffAttributes);
        fragmentStaffInfoBinding.pager.setAdapter(staffInfoAdapter);
        new TabLayoutMediator(fragmentStaffInfoBinding.tabLayout, fragmentStaffInfoBinding.pager,
                (tab, position) -> {
                    String str="";
                    switch (position){
                        case 0: str="Profile"; break;
                        case 1: str="TIME OFF"; break;
                        case 2: str="ONBOARDING"; break;
                        default: str="Profile"; break;
                    }
                    tab.setText(str);
                }
        ).attach();
        Log.d("staffAttributes",staffAttributes.toString());
        fragmentStaffInfoBinding.txtName.setText(staffAttributes.getFullname());
        fragmentStaffInfoBinding.txtStaffId.setText(staffAttributes.getId().toString());
        fragmentStaffInfoBinding.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "cc", Toast.LENGTH_SHORT).show();
            }
        });
//        txtName.setText(staffAttributes.getFullname());
//        txtId.setText(staffAttributes.getId().stoString());
        //fragmentStaffInfoBinding.txtId.setText(staffAttributes.getFullname());
        return fragmentStaffInfoBinding.getRoot();
    }
    String managerName="";
    private void showConfirmTranManager(StaffAttributes att, Context context, int pos) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.layout_select_manager, null);
        Button ok_btn=view2.findViewById(R.id.btn_ok);
        ImageView close_btn=view2.findViewById(R.id.btn_close);
        AutoCompleteTextView managers=view2.findViewById(R.id.AutoCompleteTextView);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, Common.getStaffNames());
        managers.setAdapter(adapter);
        managers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                managers.showDropDown();
                return false;
            }
        });
        managers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                managerName=adapterView.getItemAtPosition(i).toString();
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaffAttributes att=Common.findStaffByName(managerName);
                Log.d("staffName",att.toString());
                if(!managerName.equals("")&&att!=null){
                    Log.d("staffName",managerName);
                    JSONObject data=new JSONObject();
                    try {
                        data.put("boss_id",att.getId());
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data.toString());
                        Call<ResponseBody> call = APIService.getService().destroy_and_update_staff_boss(Common.getToken(), body,staffAttributes.getId());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    ((HomeActivity)getActivity()).showToast(true,"Successfully!");
                                } else ((HomeActivity)getActivity()).showToast(false,"Failed!");
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        alertDialog.dismiss();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setIcon(R.drawable.deletetrash);
        alertDialog.setCancelable(true);
        alertDialog.setView(view2);

        alertDialog.show();
    }

    private void showConfirmDialog(StaffAttributes att, Context context,int pos) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Inactive Account");
        String str="Are you sure ?";
        alertDialog.setMessage(str);
        alertDialog.setIcon(R.drawable.deletetrash);
        alertDialog.setCancelable(true);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<ResponseBody> call = APIService.getService().deleteStaff(Common.getToken(), att.getId());

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.isSuccessful()){
                            ((HomeActivity)getActivity()).showToast(true,"Delete Staff Successfully!");
//                            data.remove(att);
//                            notifyItemRemoved(position);
                                updateButton(true);
                            StaffWithPosAttributes s=new StaffWithPosAttributes(att,pos);
                            staffShareViewModel.setLeaveApp(s);
                        } else {
                            ((HomeActivity)getActivity()).showToast(true,"Delete Staff Failed!");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
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

    private void updateButton(boolean b) {
        if(b){
            fragmentStaffInfoBinding.btnInactive.setVisibility(View.GONE);
            fragmentStaffInfoBinding.btnActive.setVisibility(View.VISIBLE);
        } else {
            fragmentStaffInfoBinding.btnActive.setVisibility(View.GONE);
            fragmentStaffInfoBinding.btnInactive.setVisibility(View.VISIBLE);
        }
    }
}