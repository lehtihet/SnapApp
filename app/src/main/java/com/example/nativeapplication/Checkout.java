package com.example.nativeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nativeapplication.model.Customer;
import com.example.nativeapplication.model.TimeSlot;
import com.example.nativeapplication.retrofit.ApiManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Checkout extends AppCompatActivity {

    public static ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        apiManager = ApiManager.getInstance();

        Bundle extras = getIntent().getExtras();
        int timeslotID = extras.getInt("timeSlotId");

        String[] leftPage = {"Details:", "", extras.getString("service"), extras.getString("bookedDate"), "Time: "+extras.getString("bookedTime"), "", "Total:"};
        String[] rightPage = {"Contact information:", "", extras.getString("name"), extras.getString("phoneNumber"), "", "", extras.getString("price")};

        ConstraintLayout c_layout = (ConstraintLayout) findViewById(R.id.mainConstraint);

        populate(findViewById(R.id.textView1), leftPage);
        populate(findViewById(R.id.textView9), rightPage);

        Button checkoutBtn = findViewById(R.id.confbutton);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(c_layout)) {
                    postData(timeslotID);
                    startActivity(new Intent(Checkout.this, OrderConfirmation.class));
                }
            }
        });
    }

    private boolean validate(ConstraintLayout layout) {
        ArrayList<EditText> editTextList = new ArrayList<EditText>();

        boolean validation = true;

        for (int i=0; i<layout.getChildCount(); i++ )
            if (layout.getChildAt(i) instanceof EditText)
                editTextList.add((EditText) layout.getChildAt(i));

        for (EditText et : editTextList)
            if (et.getText().toString().isEmpty()) {
                et.setBackgroundResource(R.drawable.edittextbackgrounderror);
                validation = false;
            }

        return validation;
    }

    private void populate(TextView description, String[] lines) {
        description.setText(String.join("\n", lines));
    }

    private void postData(int timeslotID) {
        String firstName = ((EditText) findViewById(R.id.textView3)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.textView2)).getText().toString();
        String address = ((EditText) findViewById(R.id.textView4)).getText().toString();
        String postalCode = ((EditText) findViewById(R.id.textView5)).getText().toString();
        String city = ((EditText) findViewById(R.id.textView6)).getText().toString();
        String email = ((EditText) findViewById(R.id.textView7)).getText().toString();
        String phone = ((EditText) findViewById(R.id.textView8)).getText().toString();
        String timestamp = LocalDateTime.now().toString();
        TimeSlot timeslot = new TimeSlot();
        timeslot.setId(timeslotID);

        Customer customer = new Customer(firstName, lastName, postalCode, address, city, email, phone, timestamp, timeslot);

        apiManager.createCustomer(new ApiManager.ApiCallback<Customer>() {
            @Override
            public void onSuccess(Customer response) {
                System.out.println("API posted successfully" + response.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("API not successful ");
                System.out.println(t.getMessage());
            }
        }, customer);

    }
}