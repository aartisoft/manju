package com.ownmetrro.manjuagecy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ownmetrro.manjuagecy.R;
import com.ownmetrro.manjuagecy.activities.MainActivity;
import com.ownmetrro.manjuagecy.adapters.MessageListAdapter;

import com.ownmetrro.manjuagecy.constant.ConstantValues;
import com.ownmetrro.manjuagecy.customs.DialogLoader;
import com.ownmetrro.manjuagecy.models.messages_model.Datum;
import com.ownmetrro.manjuagecy.models.messages_model.Message_data;
import com.ownmetrro.manjuagecy.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class My_Message extends android.support.v4.app.Fragment {

    View rootView;
    String customerID;

    TextView emptyRecord;
    RecyclerView orders_recycler;
    MessageListAdapter messageListAdapter;

    DialogLoader dialogLoader;

    List<Datum> ordersList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_messages, container, false);

        MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.actionMsg));

        // Get the CustomerID from SharedPreferences
        customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");
        orders_recycler = (RecyclerView) rootView.findViewById(R.id.orders_recycler);
        emptyRecord = (TextView) rootView.findViewById(R.id.empty_record);


        // Hide some of the Views
        emptyRecord.setVisibility(View.GONE);
        dialogLoader = new DialogLoader(getContext());

        getMessages();

        return rootView;

    }

    private void getMessages() {
        dialogLoader.showProgressDialog();

        Call<Message_data> call = APIClient.getInstance()
                .getmyMessages
                        (
                                customerID,
                                ConstantValues.LANGUAGE_ID
                        );

        call.enqueue(new Callback<Message_data>() {
            @Override
            public void onResponse(Call<Message_data> call, retrofit2.Response<Message_data> response) {
                dialogLoader.hideProgressDialog();

                Log.e("message",new Gson().toJson(response.body()));
                // Check if the Response is successful
                if (response.isSuccessful()) {
                   // Toast.makeText(getActivity(),"hi",Toast.LENGTH_LONG).show();
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        showmessage(response.body());

                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        emptyRecord.setVisibility(View.VISIBLE);
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                  /*  else {
                        // Unable to get Success status
                        emptyRecord.setVisibility(View.VISIBLE);
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }*/
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message_data> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showmessage(Message_data message_data) {

        // Add Orders to ordersList from the List of OrderData
        ordersList = message_data.getData();


        // Initialize the OrdersListAdapter for RecyclerView
        messageListAdapter = new MessageListAdapter(getContext(), customerID, ordersList);

        // Set the Adapter and LayoutManager to the RecyclerView
        orders_recycler.setAdapter(messageListAdapter);
        orders_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        messageListAdapter.notifyDataSetChanged();
    }
}
