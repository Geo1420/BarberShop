package com.example.barbershop;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Button timePickerButton = findViewById(R.id.time_picker_button);
        Button backButton = findViewById(R.id.booking_back_button);
        backButton.setOnClickListener(view -> {
            Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeIntent);
        });
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timePickerButton.setText(String.format("%02d:00", hourOfDay));

                    }
                }, currentHour, 0, true);

                timePickerDialog.show();
            }
        });
    }
}