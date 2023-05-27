package com.example.hrm.Fragments.Performance;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.PerformanceAttributes;
import com.example.hrm.Response.PropertyAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.databinding.FragmentSelfPreviewDetailBinding;
import com.example.hrm.viewmodel.ReviewDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelfPreviewDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfPreviewDetail extends Fragment {
    public  static final String MY_TAG= "Self Preview Detail";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelfPreviewDetail() {
        // Required empty public constructor
    }
    private PerformanceAttributes att;
    private int pos;
    private boolean staffEdit,bossEdit;
    public SelfPreviewDetail(PerformanceAttributes att,int pos,boolean staffEdit,boolean bossEdit) {
        this.staffEdit=staffEdit;
        this.bossEdit=bossEdit;
        this.att=att;
        this.pos=pos;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfPreviewDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static SelfPreviewDetail newInstance(String param1, String param2) {
        SelfPreviewDetail fragment = new SelfPreviewDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FragmentSelfPreviewDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSelfPreviewDetailBinding.inflate(inflater);
        binding.setViewModel(new ReviewDetail(staffEdit,bossEdit));
        updateView();
        binding.GoalsSetStaff.setText(att.getGoalsSetStaff());
        if(att.getGoalsSetBoss()!=null) binding.GoalsSetBoss.setText(att.getGoalsSetBoss().toString());
        binding.AchievementStaff.setText(att.getAchievementStaff());
        if(att.getAchievementBoss()!=null) binding.AchievementBoss.setText(att.getAchievementBoss().toString());
        binding.GoalsWithCompanyStaff.setText(att.getGoalsWithCompanyStaff());
        if(att.getGoalsWithCompanyBoss()!=null) binding.GoalsWithCompanyBoss.setText(att.getGoalsWithCompanyBoss().toString());
        binding.ChallengingStaff.setText(att.getChallengingStaff());
        if(att.getChallengingBoss()!=null) binding.ChallengingBoss.setText(att.getChallengingBoss().toString());
        binding.LeastEnjoyStaff.setText(att.getLeastEnjoyStaff());
        if(att.getLeastEnjoyBoss()!=null) binding.LeastEnjoyBoss.setText(att.getLeastEnjoyBoss().toString());
        binding.ContributeStaff.setText(att.getContributeStaff());
        if(att.getContributeBoss()!=null) binding.ContributeBoss.setText(att.getContributeBoss().toString());
        binding.CurrentJobStaff.setText(att.getCurrentJobStaff());
        if(att.getCurrentJobBoss()!=null) binding.CurrentJobBoss.setText(att.getCurrentJobBoss().toString());
        binding.ImprovementStaff.setText(att.getImprovementStaff());
        if(att.getImprovementBoss()!=null) binding.ImprovementBoss.setText(att.getImprovementBoss().toString());
        binding.ObstructingStaff.setText(att.getObstructingStaff());
        if(att.getObstructingBoss()!=null) binding.ObstructingBoss.setText(att.getObstructingBoss().toString());
        binding.FeedbackStaff.setText(att.getFeedbackStaff());
        if(att.getFeedbackBoss()!=null) binding.FeedbackBoss.setText(att.getFeedbackBoss().toString());
        binding.DescriptionStaff.setText(att.getDescriptionStaff());
        if(att.getDescriptionBoss()!=null) binding.DescriptionBoss.setText(att.getDescriptionBoss().toString());

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMain();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMain();
            }
        });
        return binding.getRoot();
    }

    private void updateView() {
        if(att.getStatus().equals(Common.STATUS_COMPLETE)||(att.getStatus().equals(Common.STATUS_SELF_REVIEWED)&&bossEdit==false)||(!att.getStatus().equals(Common.STATUS_SELF_REVIEWED)&&bossEdit==true)){
            binding.boxSubmit.setVisibility(View.GONE);
        }
        if(bossEdit==false&&staffEdit==false) {
            binding.boxSubmit.setVisibility(View.GONE);
        }
    }

    private void saveMain() {
        if(bossEdit) saveBoss();
        else save();
    }
    private void submitMain() {
        if(bossEdit) submitBoss();
        else submit();
    }
    private void saveBoss() {
        JSONObject parent=new JSONObject();
        JSONObject child=new JSONObject();
        JSONObject child2=new JSONObject();
        try {
            child.put("achievement_boss",binding.AchievementBoss.getText());
            child.put("challenging_boss",binding.ChallengingBoss.getText());
            child.put("contribute_boss",binding.ContributeBoss.getText());
            child.put("current_job_boss",binding.CurrentJobBoss.getText());
            child.put("description_boss",binding.DescriptionBoss.getText());
            child.put("feedback_boss",binding.FeedbackBoss.getText());
            child.put("goals_set_boss",binding.GoalsSetBoss.getText());
            child.put("goals_with_company_boss", binding.GoalsWithCompanyBoss.getText());
            child.put("improvement_boss",binding.ImprovementBoss.getText());
            child.put("least_enjoy_boss",binding.LeastEnjoyBoss.getText());
            child.put("obstructing_boss",binding.ObstructingBoss.getText());
            parent.put("pa_form",child);
            child2.put("id",att.getId());
            parent.put("params",child2);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
            Call<JsonObject> call = APIService.getService().savePerformanceSelfPreview(Common.getToken(),att.getId(),body);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        ((HomeActivity)getActivity()).showToast(true,"Save Successfully");
                        Gson gson= (new GsonBuilder()).setPrettyPrinting().create();
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(response.body().toString());// response will be the json String
                        DatumTemplate<PerformanceAttributes> emp = gson.fromJson(object.get("data"), new TypeToken<DatumTemplate<PerformanceAttributes>>() {}.getType());
                        PerformanceAttributes propertyAttributes=emp.getAttributes();
                        Log.d("att",propertyAttributes.toString());
                        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
                        Intent i = new Intent("TAG_REFRESH");
                        i.putExtra("item",propertyAttributes);
                        i.putExtra("pos",pos);
                        att=propertyAttributes;
                        updateView();
                        lbm.sendBroadcast(i);
                    } else  ((HomeActivity)getActivity()).showToast(false,"Save Failed");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void save() {
        JSONObject parent=new JSONObject();
        JSONObject child=new JSONObject();
        JSONObject child2=new JSONObject();
        try {
            child.put("achievement_staff",binding.AchievementStaff.getText());
            child.put("challenging_staff",binding.ChallengingStaff.getText());
            child.put("contribute_staff",binding.ContributeStaff.getText());
            child.put("current_job_staff",binding.CurrentJobStaff.getText());
            child.put("description_staff",binding.DescriptionStaff.getText());
            child.put("feedback_staff",binding.FeedbackStaff.getText());
            child.put("goals_set_staff",binding.GoalsSetStaff.getText());
            child.put("goals_with_company_staff", binding.GoalsWithCompanyStaff.getText());
            child.put("improvement_staff",binding.ImprovementStaff.getText());
            child.put("least_enjoy_staff",binding.LeastEnjoyStaff.getText());
            child.put("obstructing_staff",binding.ObstructingStaff.getText());
            parent.put("pa_form",child);
            child2.put("id",att.getId());
            parent.put("params",child2);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
            Call<JsonObject> call = APIService.getService().savePerformanceSelfPreview(Common.getToken(),att.getId(),body);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        ((HomeActivity)getActivity()).showToast(true,"Save Successfully");
                        Gson gson= (new GsonBuilder()).setPrettyPrinting().create();
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(response.body().toString());// response will be the json String
                        DatumTemplate<PerformanceAttributes> emp = gson.fromJson(object.get("data"), new TypeToken<DatumTemplate<PerformanceAttributes>>() {}.getType());
                        PerformanceAttributes propertyAttributes=emp.getAttributes();
                        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
                        Intent i = new Intent("TAG_REFRESH");
                        i.putExtra("item",propertyAttributes);
                        i.putExtra("pos",pos);
                        att=propertyAttributes;
                        updateView();
                        lbm.sendBroadcast(i);
                    } else  ((HomeActivity)getActivity()).showToast(false,"Save Failed");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void submitBoss() {
        JSONObject parent=new JSONObject();
        JSONObject child=new JSONObject();
        JSONObject child2=new JSONObject();
        try {
            child.put("achievement_boss",binding.AchievementBoss.getText());
            child.put("challenging_boss",binding.ChallengingBoss.getText());
            child.put("contribute_boss",binding.ContributeBoss.getText());
            child.put("current_job_boss",binding.CurrentJobBoss.getText());
            child.put("description_boss",binding.DescriptionBoss.getText());
            child.put("feedback_boss",binding.FeedbackBoss.getText());
            child.put("goals_set_boss",binding.GoalsSetBoss.getText());
            child.put("goals_with_company_boss", binding.GoalsWithCompanyBoss.getText());
            child.put("improvement_boss",binding.ImprovementBoss.getText());
            child.put("least_enjoy_boss",binding.LeastEnjoyBoss.getText());
            child.put("obstructing_boss",binding.ObstructingBoss.getText());
            child.put("status","completed");
            parent.put("pa_form",child);
            child2.put("id",att.getId());
            parent.put("params",child2);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
            Call<JsonObject> call = APIService.getService().submitPerformanceSelfPreview(Common.getToken(),att.getId(),body);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        ((HomeActivity)getActivity()).showToast(true,"Save Successfully");
                        Gson gson= (new GsonBuilder()).setPrettyPrinting().create();
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(response.body().toString());// response will be the json String
                        DatumTemplate<PerformanceAttributes> emp = gson.fromJson(object.get("data"), new TypeToken<DatumTemplate<PerformanceAttributes>>() {}.getType());
                        PerformanceAttributes propertyAttributes=emp.getAttributes();
                        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
                        Intent i = new Intent("TAG_REFRESH");
                        i.putExtra("item",propertyAttributes);
                        i.putExtra("pos",pos);
                        att=propertyAttributes;
                        updateView();
                        lbm.sendBroadcast(i);
                    } else  ((HomeActivity)getActivity()).showToast(false,"Save Failed");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void submit() {
        JSONObject parent=new JSONObject();
        JSONObject child=new JSONObject();
        JSONObject child2=new JSONObject();
        try {
            child.put("achievement_staff",binding.AchievementStaff.getText());
            child.put("challenging_staff",binding.ChallengingStaff.getText());
            child.put("contribute_staff",binding.ContributeStaff.getText());
            child.put("current_job_staff",binding.CurrentJobStaff.getText());
            child.put("description_staff",binding.DescriptionStaff.getText());
            child.put("feedback_staff",binding.FeedbackStaff.getText());
            child.put("goals_set_staff",binding.GoalsSetStaff.getText());
            child.put("goals_with_company_staff", binding.GoalsWithCompanyStaff.getText());
            child.put("improvement_staff",binding.ImprovementStaff.getText());
            child.put("least_enjoy_staff",binding.LeastEnjoyStaff.getText());
            child.put("obstructing_staff",binding.ObstructingStaff.getText());
            child.put("status","self_reviewed");
            parent.put("pa_form",child);
            child2.put("id",att.getId());
            parent.put("params",child2);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
            Call<JsonObject> call = APIService.getService().submitPerformanceSelfPreview(Common.getToken(),att.getId(),body);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        ((HomeActivity)getActivity()).showToast(true,"Save Successfully");
                        Gson gson= (new GsonBuilder()).setPrettyPrinting().create();
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(response.body().toString());// response will be the json String
                        DatumTemplate<PerformanceAttributes> emp = gson.fromJson(object.get("data"), new TypeToken<DatumTemplate<PerformanceAttributes>>() {}.getType());
                        PerformanceAttributes propertyAttributes=emp.getAttributes();
                        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
                        Intent i = new Intent("TAG_REFRESH");
                        i.putExtra("item",propertyAttributes);
                        i.putExtra("pos",pos);
                        att=propertyAttributes;
                        updateView();
                        lbm.sendBroadcast(i);
                    } else  ((HomeActivity)getActivity()).showToast(false,"Save Failed");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}