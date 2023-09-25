package com.example.nativeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nativeapplication.model.ServiceProfessional;
import com.example.nativeapplication.retrofit.ApiManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PostYourBusiness extends AppCompatActivity {
    String text7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_your_business);

        //Post you business spinner shows list of services to select
        Spinner spinner = findViewById(R.id.serviceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text7 = parent.getItemAtPosition(position).toString();
                System.out.println(text7);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Please select an item");
            }

        });

        //Validation check to handle filling of mandatory fields and highlighting them
        Button myButton = findViewById(R.id.button2);
        myButton.setOnClickListener(v -> {

            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.postBusinessConstraint);

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
    
            if(validation) {
                //Functionality to redirect the page to the next page - Order confirmation
                ServiceProfessional serviceprofessional = new ServiceProfessional();
                serviceprofessional.setServiceDescription(text1);
                serviceprofessional.setName(text2);
                serviceprofessional.setAddress(text3);
                serviceprofessional.setPhoneNumber(text4);
                serviceprofessional.setEmail(text5);
                serviceprofessional.setPrice(Double.valueOf(text6));
                serviceprofessional.setServiceSubcategory(text7);
                serviceprofessional.setRating((float) 0);

                MainActivity.apiManager.saveServiceProfessional(new ApiManager.ApiCallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        Intent i = new Intent(PostYourBusiness.this, OrderConfirmation.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(PostYourBusiness.this, "Service Professional Details Save Failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(PostYourBusiness.class.getName()).log(Level.SEVERE, "Error Occurred", t);
                    }
                }, serviceprofessional);

            }
        });

    }
}