package com.example.admin.ich.modules;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.example.admin.ich.R;
import com.example.admin.ich.modules.Home.Checkout.CheckoutActivity;
import com.example.admin.ich.modules.Home.HomeActivity;
import com.example.admin.ich.modules.Login.SessionManager;
import com.example.admin.ich.retrofit.ApiClient;
import com.example.admin.ich.retrofit.ApiInterface;
import com.example.admin.ich.retrofit.model.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by admin on 6/30/2017.
 */

public class BaseActivity extends AppCompatActivity {

    Context context;
RelativeLayout rootViewsnack;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack);
        context=this;
        rootViewsnack=(RelativeLayout)findViewById(R.id.root);
        session = new SessionManager(context);

    }

    public void signinUser(final String email, String password, final String from) {
        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("loading");
        progressDialog.show();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<SignInResponse> call = apiService.Signin(email, password);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, retrofit2.Response<SignInResponse> response) {
                progressDialog.hide();
                if (response.body().getStatus()) {
                    session.setLogin(true);
                    AppController.setString(getApplicationContext(), "ich_customer_id", response.body().getCustomerId());
                    AppController.setString(getApplicationContext(), "ich_name", response.body().getCustomerName());
                    AppController.setString(getApplicationContext(), "ich_location_id", response.body().getLocation().getLocationId());
                    AppController.setString(getApplicationContext(), "ich_email", email);
                    if (from.equals("login")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    if (from.equals("checkout")) {
                        Intent intent = new Intent(context, CheckoutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }


            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                // Log error here since request failed
                //progressDialog.dismiss();

            }
        });
    }


}