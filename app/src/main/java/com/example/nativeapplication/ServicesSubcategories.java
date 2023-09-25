package com.example.nativeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class ServicesSubcategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_subcategories);

        ConstraintLayout c_layout = (ConstraintLayout) findViewById(R.id.mainConstraint);
        c_layout.setBackgroundColor(Color.parseColor("#F1EDE7"));

        Bundle extras = getIntent().getExtras();

        String[] value = (extras == null) ? new String[]{"Filler value"} : extras.getString("category").split(";");
        int buttonHeight = 350;

        for (int i = 0; i < value.length; i++) {
            addButton(value[i], buttonHeight, i+1);
            Log.d("subcategory string",value[i]);
        }
    }

    private void addButton(String text, int buttonHeight, int id) {
        ConstraintLayout c_layout = (ConstraintLayout) findViewById(R.id.mainConstraint);
        ConstraintSet set = new ConstraintSet();

        Button btn1 = new Button(this);
        btn1.setId(View.generateViewId());
        btn1.setText(text);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServicesSubcategories.this, ServicePersonList.class);
                i.putExtra("type", btn1.getText());

                startActivity(i);
            }
        });

        c_layout.addView(btn1);

        // Center the buttons; but not with regards to top side of screen
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        //layoutParams.bottomToBottom = ConstraintSet.PARENT_ID;
        layoutParams.endToEnd = ConstraintSet.PARENT_ID;
        layoutParams.startToStart = ConstraintSet.PARENT_ID;
        layoutParams.topToTop = ConstraintSet.PARENT_ID;
        btn1.setLayoutParams(layoutParams);

        // Add the button
        set.clone(c_layout);
        set.connect(btn1.getId(), ConstraintSet.TOP, c_layout.getId(), ConstraintSet.TOP, buttonHeight*id);
        set.applyTo(c_layout);
    }
}