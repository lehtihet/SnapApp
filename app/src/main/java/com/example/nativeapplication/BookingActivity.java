package com.example.nativeapplication;

import static com.example.nativeapplication.CalendarUtils.weekNumber;
import static com.example.nativeapplication.CalendarUtils.datesOfWeek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nativeapplication.model.ServiceProfessional;
import com.example.nativeapplication.model.TimeSlot;
import com.example.nativeapplication.retrofit.ApiManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private Integer serviceProfessionalId;
    private TextView currentWeek;
    private TextView dateAndYear;
    private TextView serviceSubcategoryView;
    private TextView serviceNameView;
    private TextView serviceDescriptionView;
    private TextView servicePriceView;
    private TextView servicePhoneNumberView;
    private LinearLayout bookableTimesMon;
    private LinearLayout bookableTimesTue;
    private LinearLayout bookableTimesWed;
    private LinearLayout bookableTimesThu;
    private LinearLayout bookableTimesFri;
    private LinearLayout bookableTimesSat;
    private LinearLayout bookableTimesSun;
    private TextView dateViewMon;
    private TextView dateViewMonTue;
    private TextView dateViewMonWed;
    private TextView dateViewMonThu;
    private TextView dateViewMonFri;
    private TextView dateViewMonSat;
    private TextView dateViewMonSun;
    private ImageView personAvatarView;
    private RatingBar ratingView;
    private TextView ratingTextView;
    private TextView personNameView;
    private int weekNumber;
    private LocalDateTime minDate;
    private LocalDateTime maxDate = LocalDateTime.of(2023, 8, 1, 0, 0, 0);
    private ArrayList<LocalDateTime> datesOfWeek = null;
    private TextView selectedBookableTimeView = null;
    private TimeSlot selectedTimeSlot = null;
    private String personName;
    private Integer personAvatar;
    private Double personPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initWidgets();

        personName = getIntent().getStringExtra("personName");
        personAvatar = getIntent().getIntExtra("personAvatar", 0);
        serviceProfessionalId = getIntent().getIntExtra("personId", 0);
        personPrice = getIntent().getDoubleExtra("personPrice", 0);

        Log.d("Price", personPrice.toString());

        MainActivity.apiManager.getServiceProfessionalFromId(new ApiManager.ApiCallback<ServiceProfessional>() {
            @Override
            public void onSuccess(ServiceProfessional response) {
                Log.d("SOMETHING", response.getAddress());
                showServiceDetails(response);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BookingActivity.this, "Could not load the page", Toast.LENGTH_SHORT).show();
            }
        }, serviceProfessionalId);

        Button bookButton = findViewById(R.id.button_book);
        bookButton.setOnClickListener(v -> {
            if (selectedTimeSlot != null) {
                Intent i = new Intent(BookingActivity.this, Checkout.class);
                i.putExtra("name", selectedTimeSlot.getServiceProfessional().getName());
                i.putExtra("service", selectedTimeSlot.getServiceProfessional().getServiceSubcategory());
                i.putExtra("price", selectedTimeSlot.getServiceProfessional().getPrice() + " SEK");
                i.putExtra("phoneNumber", selectedTimeSlot.getServiceProfessional().getPhoneNumber());
                i.putExtra("timeSlotId", selectedTimeSlot.getId());
                i.putExtra("bookedDate", selectedTimeSlot.getDateTime().substring(0, 10));
                i.putExtra("bookedTime", selectedTimeSlot.getDateTime().substring(11, 16));
                startActivity(i);
            }
            else {
                Toast toast = Toast.makeText(this, R.string.book_button_message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }
        });

        minDate = LocalDateTime.now();
        weekNumber = weekNumber(minDate);
        datesOfWeek = datesOfWeek(minDate);

        showWeek();
        showDateAndYear();
        showDatesOfWeek();
        showBookableTimes();
    }

    private void initWidgets() {
        currentWeek = findViewById(R.id.booking_week_number);
        dateAndYear = findViewById(R.id.booking_start_end_dates);
        serviceNameView = findViewById(R.id.booking_service_name_text);
        serviceSubcategoryView = findViewById(R.id.booking_service_subcategory_text);
        serviceDescriptionView = findViewById(R.id.booking_service_description_text);
        servicePriceView = findViewById(R.id.booking_service_price_text);
        servicePhoneNumberView = findViewById(R.id.booking_service_telephone_text);
        personAvatarView = findViewById(R.id.booking_avatar);
        ratingView = findViewById(R.id.booking_rating_bar);
        ratingTextView = findViewById(R.id.booking_rating_text);
        personNameView = findViewById(R.id.booking_person_name);
        bookableTimesMon = findViewById(R.id.bookable_times_mon);
        bookableTimesTue = findViewById(R.id.bookable_times_tue);
        bookableTimesWed = findViewById(R.id.bookable_times_wed);
        bookableTimesThu = findViewById(R.id.bookable_times_thu);
        bookableTimesFri = findViewById(R.id.bookable_times_fri);
        bookableTimesSat = findViewById(R.id.bookable_times_sat);
        bookableTimesSun = findViewById(R.id.bookable_times_sun);
        dateViewMon = findViewById(R.id.date_text_mon);
        dateViewMonTue = findViewById(R.id.date_text_tue);
        dateViewMonWed = findViewById(R.id.date_text_wed);
        dateViewMonThu = findViewById(R.id.date_text_thu);
        dateViewMonFri = findViewById(R.id.date_text_fri);
        dateViewMonSat = findViewById(R.id.date_text_sat);
        dateViewMonSun = findViewById(R.id.date_text_sun);
    }
    private void showWeek() {
        String week = "Week " + weekNumber;
        currentWeek.setText(week);
    }

    @SuppressLint("SetTextI18n")
    private void showDateAndYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
        int firstDay = datesOfWeek.get(0).getDayOfMonth();
        int lastDay = datesOfWeek.get(6).getDayOfMonth();
        String firstDayMonth = datesOfWeek.get(0).format(formatter);
        String lastDayMonth = datesOfWeek.get(6).format(formatter);

        /* Handles the case when the year is different between the first day and last day of the week */
        String year;
        if (datesOfWeek.get(0).getYear() != datesOfWeek.get(6).getYear()) {
            year = datesOfWeek.get(0).getYear() + "/" + datesOfWeek.get(6).getYear();
        } else {
            year = "" + datesOfWeek.get(0).getYear();
        }

        String dateYear = firstDay + " " + firstDayMonth + " - " + lastDay + " " + lastDayMonth + " " + year;

        dateAndYear.setText(dateYear);
    }

    private void showServiceDetails(ServiceProfessional serviceProfessionalDetails) {
        if (!personAvatar.equals(0)) {
            personAvatarView.setImageResource((Integer) personAvatar);
        }
        if (personName.length() > 15) {
            personNameView.setTextSize(16);
        }
        personNameView.setText(personName);
        ratingTextView.setText(serviceProfessionalDetails.getRating().toString());
        ratingView.setRating((Float)serviceProfessionalDetails.getRating());
        serviceSubcategoryView.setText(serviceProfessionalDetails.getServiceSubcategory());
        if (serviceProfessionalDetails.getServiceName() != null ) {
            serviceNameView.setText(serviceProfessionalDetails.getServiceName());
        } else {
            serviceNameView.setText("");
        }

        String price = "Price: " + personPrice + " SEK";
        servicePriceView.setText(price);

        serviceDescriptionView.setText(serviceProfessionalDetails.getServiceDescription());

        String phoneNumber = "Phone number: " + serviceProfessionalDetails.getPhoneNumber();
        servicePhoneNumberView.setText(phoneNumber);

    }

    private void showBookableTimes() {
        bookableTimesMon.removeAllViews();
        bookableTimesTue.removeAllViews();
        bookableTimesWed.removeAllViews();
        bookableTimesThu.removeAllViews();
        bookableTimesFri.removeAllViews();
        bookableTimesSat.removeAllViews();
        bookableTimesSun.removeAllViews();

        addBookableTimes(bookableTimesMon, datesOfWeek.get(0));
        addBookableTimes(bookableTimesTue, datesOfWeek.get(1));
        addBookableTimes(bookableTimesWed, datesOfWeek.get(2));
        addBookableTimes(bookableTimesThu, datesOfWeek.get(3));
        addBookableTimes(bookableTimesFri, datesOfWeek.get(4));
        addBookableTimes(bookableTimesSat, datesOfWeek.get(5));
        addBookableTimes(bookableTimesSun, datesOfWeek.get(6));
    }

    private void addBookableTimes(ViewGroup parent, LocalDateTime localDate) {
        LayoutInflater inflater = getLayoutInflater();
        MainActivity.apiManager.getTimeSlotsInRange(new ApiManager.ApiCallback<List<TimeSlot>>() {
            @Override
            public void onSuccess(List<TimeSlot> response) {

                for (TimeSlot timeSlot: response) {
                    LocalDateTime dateTime = LocalDateTime.parse(timeSlot.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    if (minDate.isBefore(dateTime.minusHours(2))) {
                        View bookableTimeCell = inflater.inflate(R.layout.bookable_times_cell, parent, false);
                        TextView textView = (TextView) bookableTimeCell.findViewById(R.id.bookable_time_cell);
                        textView.setText(timeSlot.getDateTime().substring(11, 16));
                        textView.setTag(timeSlot);
                        parent.addView(bookableTimeCell);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BookingActivity.this, "Can't display bookable times", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", "An error happened");
            }
        }, localDate.toLocalDate().toString(), serviceProfessionalId);
    }

    private void showDatesOfWeek() {
        String dateMon = "" + datesOfWeek.get(0).getDayOfMonth();
        String dateTue = "" + datesOfWeek.get(1).getDayOfMonth();
        String dateWed = "" + datesOfWeek.get(2).getDayOfMonth();
        String dateThu = "" + datesOfWeek.get(3).getDayOfMonth();
        String dateFri = "" + datesOfWeek.get(4).getDayOfMonth();
        String dateSat = "" + datesOfWeek.get(5).getDayOfMonth();
        String dateSun = "" + datesOfWeek.get(6).getDayOfMonth();

        dateViewMon.setText(dateMon);
        dateViewMonTue.setText(dateTue);
        dateViewMonWed.setText(dateWed);
        dateViewMonThu.setText(dateThu);
        dateViewMonFri.setText(dateFri);
        dateViewMonSat.setText(dateSat);
        dateViewMonSun.setText(dateSun);
   }

    /* Set the previous week */
    public void previousWeek(View view) {
        datesOfWeek = datesOfWeek(datesOfWeek.get(0).minusWeeks(1));
        weekNumber = weekNumber(datesOfWeek.get(0));
        showWeek();
        showDateAndYear();
        showDatesOfWeek();
        showBookableTimes();
        //disableButton();
    }

    /* Set the next week */
    public void nextWeek(View view) {
        datesOfWeek = datesOfWeek(datesOfWeek.get(0).plusWeeks(1));
        weekNumber = weekNumber(datesOfWeek.get(0));
        showWeek();
        showDateAndYear();
        showDatesOfWeek();
        showBookableTimes();
        //disableButton();
    }

    private void disableButton() {
        ImageButton prevButton = (ImageButton) findViewById(R.id.button_prev_week);
        if (minDate.isAfter(datesOfWeek.get(0))) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }

        if (maxDate.isAfter(datesOfWeek.get(0))) {
            ImageButton button = (ImageButton) findViewById(R.id.button_next_week);
            button.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void selectDate(View view) {
        if (selectedBookableTimeView != null) {
            selectedBookableTimeView.setBackground(getResources().getDrawable(R.drawable.border_bookable_times));
        }

        TextView textView = (TextView) view;
        selectedTimeSlot = (TimeSlot) textView.getTag();
        selectedBookableTimeView = textView;
        textView.setBackground(getResources().getDrawable(R.drawable.background_bookable_times));
    }
}
