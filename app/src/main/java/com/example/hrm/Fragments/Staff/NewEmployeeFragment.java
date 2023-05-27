package com.example.hrm.Fragments.Staff;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Response.Attributes;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.ViewModel.StaffShareViewModel;
import com.example.hrm.databinding.FragmentNewEmployeeBinding;
import com.example.hrm.viewmodel.NewEmployeeViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEmployeeFragment extends Fragment {
    public  static final String MY_TAG= "New Employee";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewEmployeeFragment() {
        // Required empty public constructor
    }
    private  StaffAttributes staff;
    public NewEmployeeFragment(StaffAttributes staff) {
    this.staff=staff;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewEmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEmployeeFragment newInstance(String param1, String param2) {
        NewEmployeeFragment fragment = new NewEmployeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    List<DatumTemplate<Attributes>> departmentsAtts,jobtitlesAtts,positionsAtts;
    List<DatumTemplate<StaffAttributes>> staffAtts;
    String[] departmentNames,jobtitleNames,positionNames,staffNames;
    ArrayAdapter<String> departmentAdapter,jobtitleAdapter,positionAdapter,staffAdapter;
    int posId,jobId,deId,staffId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    FragmentNewEmployeeBinding fragmentNewEmployeeBinding;
    private StaffShareViewModel staffShareViewModel;
    boolean showPass=false;
    boolean showconfirmPass=false;
    NewEmployeeViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        staffShareViewModel = new ViewModelProvider(getActivity()).get(StaffShareViewModel.class);

        viewModel=new NewEmployeeViewModel(this.staff);
        fragmentNewEmployeeBinding=FragmentNewEmployeeBinding.inflate(inflater);
        fragmentNewEmployeeBinding.setNewEmployeeViewModel(viewModel);
        // Inflate the layout for this fragment
        fragmentNewEmployeeBinding.txtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (fragmentNewEmployeeBinding.txtPassword.getRight() - fragmentNewEmployeeBinding.txtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        //Toast.makeText(LoginActivity.this, "SHow di ba gia", Toast.LENGTH_SHORT).show();
                        if(!showPass){
                            fragmentNewEmployeeBinding.txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            showPass=true;}
                        else  {
                            showPass=false;
                            fragmentNewEmployeeBinding.txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        fragmentNewEmployeeBinding.txtConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (fragmentNewEmployeeBinding.txtConfirmPassword.getRight() - fragmentNewEmployeeBinding.txtConfirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        //Toast.makeText(LoginActivity.this, "SHow di ba gia", Toast.LENGTH_SHORT).show();
                        if(!showconfirmPass){
                            fragmentNewEmployeeBinding.txtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            showconfirmPass=true;}
                        else  {
                            showconfirmPass=false;
                            fragmentNewEmployeeBinding.txtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        if (getArguments() != null) {
            Log.d("getArguments","!null");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            departmentsAtts= (List<DatumTemplate<Attributes>>) getArguments().getSerializable("departmentAtts");
            jobtitlesAtts= (List<DatumTemplate<Attributes>>) getArguments().getSerializable("jobtitleAtts");
            positionsAtts= (List<DatumTemplate<Attributes>>) getArguments().getSerializable("positionAtts");
            staffAtts= (List<DatumTemplate<StaffAttributes>>) getArguments().getSerializable("staffAtts");
            departmentNames=getArguments().getStringArray("departmentNames");
            jobtitleNames=getArguments().getStringArray("jobtitleNames");
            positionNames=getArguments().getStringArray("positionNames");
            staffNames=getArguments().getStringArray("staffNames");
            //
            departmentAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, departmentNames);
            jobtitleAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, jobtitleNames);
            positionAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, positionNames);
            staffAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, staffNames);
        } else  Log.d("getArguments","null");



        fragmentNewEmployeeBinding.edtDepartments.setAdapter(departmentAdapter);

        fragmentNewEmployeeBinding.edtDepartments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("onTouch","showDropDown");
                Log.d("onTouch","departmentNames Size:"+departmentNames.length);
                fragmentNewEmployeeBinding.edtDepartments.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deId=departmentsAtts.get(i).getAttributes().getId();
            }
        });
        fragmentNewEmployeeBinding.edtJobtitle.setAdapter(jobtitleAdapter);
        fragmentNewEmployeeBinding.edtJobtitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtJobtitle.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtJobtitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jobId=jobtitlesAtts.get(i).getAttributes().getId();
            }
        });
        fragmentNewEmployeeBinding.edtPositions.setAdapter(positionAdapter);
        fragmentNewEmployeeBinding.edtPositions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtPositions.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtPositions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posId=positionsAtts.get(i).getAttributes().getId();
            }
        });
        fragmentNewEmployeeBinding.edtManager.setAdapter(staffAdapter);
        fragmentNewEmployeeBinding.edtManager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentNewEmployeeBinding.edtManager.showDropDown();
                return false;
            }
        });
        fragmentNewEmployeeBinding.edtManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                staffId=staffAtts.get(i).getAttributes().getId();
                for(int k=0;k<staffAtts.size();k++){
                    if (staffAtts.get(k).getAttributes().getFullname().equals(fragmentNewEmployeeBinding.edtManager.getText().toString())) {
                        staffId=staffAtts.get(k).getAttributes().getId();
                        Log.d("staffId", String.valueOf(staffId));
                        break;
                    }}
            }
        });
        //
        final EditText edt_birth_day = fragmentNewEmployeeBinding.idEdtDateOfBirth;
        //TextView txt_message= view2.findViewById(R.id.txt_message);
        edt_birth_day.setText("");
        edt_birth_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date =  (++i1) +"-" +i2 + "-" + i;
                        edt_birth_day.setText(date);
                    }
                }, LocalDate.now().getYear(),LocalDate.now().getMonth().getValue()-1,LocalDate.now().getDayOfMonth());
                datePickerDialog.show();
            }
        });
        edt_birth_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String dateTo=edt_birth_day.getText().toString();
                if(dateTo.equals("")) return;
                String[] tmp2=dateTo.split("-");
                int fromM,fromD,fromY;
                fromM= Integer.parseInt(tmp2[0]);
                fromD= Integer.parseInt(tmp2[1]);
                fromY= Integer.parseInt(tmp2[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 13);
                calendar.set(Calendar.HOUR, 7);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.MONTH, fromM-1);
                calendar.set(Calendar.DAY_OF_MONTH, fromD);
                calendar.set(Calendar.YEAR, fromY);
                viewModel.setCalendarBirthDate(calendar);
            }
        });
        final EditText edt_join_day= fragmentNewEmployeeBinding.idEdtJoinDate;
        edt_join_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date =  (++i1) +"-" +i2 + "-" + i;
                        edt_join_day.setText(date);
                    }
                },LocalDate.now().getYear(),LocalDate.now().getMonth().getValue()-1,LocalDate.now().getDayOfMonth());
                datePickerDialog.show();
            }
        });
        edt_join_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String dateTo=edt_join_day.getText().toString();
                if(dateTo.equals("")) return;
                String[] tmp2=dateTo.split("-");
                int fromM,fromD,fromY;
                fromM= Integer.parseInt(tmp2[0]);
                fromD= Integer.parseInt(tmp2[1]);
                fromY= Integer.parseInt(tmp2[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 13);
                calendar.set(Calendar.HOUR, 7);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.MONTH, fromM-1);
                calendar.set(Calendar.DAY_OF_MONTH, fromD);
                calendar.set(Calendar.YEAR, fromY);
                viewModel.setCalendarJoinDate(calendar);
            }
        });
        fragmentNewEmployeeBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setSubmited(true);
                if(viewModel.checkAll()){
                    sendData();
                    Log.d("isSenddata","true");
                } else {
                    Log.d("isSenddata","false");
                }
            }
        });
        if(this.staff!=null){
            //posId,jobId,deId,staffId
            //hide password
            fragmentNewEmployeeBinding.txtPassword.setVisibility(View.GONE);
            fragmentNewEmployeeBinding.txtTitlePassword.setVisibility(View.GONE);
            fragmentNewEmployeeBinding.txtTitleConfirmPassword.setVisibility(View.GONE);
            fragmentNewEmployeeBinding.txtMessConfirmPassword.setVisibility(View.GONE);
            fragmentNewEmployeeBinding.txtMessPassword.setVisibility(View.GONE);
            fragmentNewEmployeeBinding.txtConfirmPassword.setVisibility(View.GONE);
            viewModel.setCheckPassword(false);
            if(staff.getPosition()!=null) {
                fragmentNewEmployeeBinding.edtPositions.setText(staff.getPosition().getName(),false);
                viewModel.setPosition(staff.getPosition().getName());
                posId=staff.getPosition().getId();

            }
            if(staff.getJobTitle()!=null) {
                fragmentNewEmployeeBinding.edtJobtitle.setText(staff.getJobTitle().getTitle(),false);
                viewModel.setJobtitle(staff.getJobTitle().getTitle());
                jobId=staff.getJobTitle().getId();
            }
            if(staff.getDepartment()!=null) {
                fragmentNewEmployeeBinding.edtDepartments.setText(staff.getDepartment().getName(),false);
                viewModel.setDepartment(staff.getDepartment().getName());
                deId=staff.getDepartment().getId();
            }
            if(staff.getUpperLevel()!=null) {
                fragmentNewEmployeeBinding.edtManager.setText(staff.getUpperLevel().getFullname(),false);
                viewModel.setManager(staff.getUpperLevel().getFullname());
                staffId=staff.getUpperLevel().getId();
            }
            if(staff.getDateOfBirth()!=null) edt_birth_day.setText(staff.getDateOfBirth());
            if(staff.getJoinDate()!=null) edt_birth_day.setText(staff.getJoinDate());

        }
        return fragmentNewEmployeeBinding.getRoot();
    }

    private void sendData() {
        String tag="";
        if(staff==null) tag="Create";else tag="Update";
        JSONObject dataParent=new JSONObject();
        JSONObject dataChild=new JSONObject();
        try {
            String birthday,joinDate;
            DateFormat simple = new SimpleDateFormat(
                    "yyyy-MM-dd");
            birthday=simple.format(new Date(viewModel.getCalendarBirthDate().getTimeInMillis()));
            joinDate=simple.format(new Date(viewModel.getCalendarJoinDate().getTimeInMillis()));
            dataChild.put("address", fragmentNewEmployeeBinding.txtAddress.getText().toString().trim());
            dataChild.put("date_of_birth", birthday);
            dataChild.put("department_id", deId);
            dataChild.put("email", fragmentNewEmployeeBinding.txtEmail.getText().toString().trim());
            dataChild.put("fullname", fragmentNewEmployeeBinding.txtFullname.getText().toString().trim());
            dataChild.put("gender", fragmentNewEmployeeBinding.txtGender.getText().toString().trim());
            dataChild.put("job_title_id", jobId);
            dataChild.put("join_date", joinDate);
            dataChild.put("password", fragmentNewEmployeeBinding.txtPassword.getText().toString().trim());
            dataChild.put("phone", fragmentNewEmployeeBinding.txtPhone.getText().toString().trim());
            dataChild.put("position_id", posId);
            dataChild.put("staff_id",staffId);
            dataParent.put("staff",dataChild);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), dataParent.toString());
            Call call=null;
            if(staff==null) call=APIService.getServiceJson().addStaff(body, Common.getToken());
            else call=APIService.getService().updateStaff(Common.getToken(),staff.getId(),body);
            Response response=call.execute();
            boolean isSuccess=false;
            if(response.isSuccessful()){
                isSuccess=true;
                Gson gson= (new GsonBuilder()).setPrettyPrinting().create();
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(response.body().toString());// response will be the json String
                DatumTemplate<StaffAttributes> data = gson.fromJson(object.get("data"), new TypeToken<DatumTemplate<StaffAttributes>>() {}.getType());
                StaffAttributes staffRes=data.getAttributes();
                staffShareViewModel.setStaff(staffRes);
                Bundle bundle=new Bundle();
                bundle.putSerializable("staff",staffRes);
                getParentFragmentManager().setFragmentResult("staffFragment",bundle);
            }
            else {
                staffShareViewModel.setStaff(null);
                Bundle bundle=new Bundle();
                bundle.putSerializable("staff",null);
                getParentFragmentManager().setFragmentResult("staffFragment",bundle);
            }
            ((HomeActivity)getActivity()).onBackPressed();
//            StaffFragment fragment=new StaffFragment();
//            final Bundle args = new Bundle();
//            args.putString("TAG", StaffFragment.MY_TAG);
//            args.putString("Action", tag);
//            args.putBoolean("isSuccess",isSuccess);
//            fragment.setArguments(args);
//            ((HomeActivity)getActivity()).relaceFragment(fragment);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void checkIsEmpty() {

    }
}