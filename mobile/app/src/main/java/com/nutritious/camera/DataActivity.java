package com.nutritious.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DataActivity extends AppCompatActivity {
    EditText name;
    EditText age;
    EditText weight;
    EditText height;
    RadioButton male;
    RadioButton female;
    RadioGroup gender;

    private SQLiteDatabaseHandler db;

    public void onSubmit(View view) {
        db.addUser(new User(name.getText().toString(), Integer.parseInt(age.getText().toString()), Double.parseDouble(weight.getText().toString()), Double.parseDouble(height.getText().toString()), gender.getCheckedRadioButtonId() == male.getId() ? 'M' : 'F'));
        Log.i("Rohit2", name.getText() + " " + age.getText() + " " + weight.getText());
            startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        name = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        weight = (EditText)findViewById(R.id.weight);
        height = (EditText)findViewById(R.id.height);
        gender = (RadioGroup) findViewById(R.id.radioSex);
        male = (RadioButton) findViewById(R.id.radioMale);
        female = (RadioButton) findViewById(R.id.radioFemale);

        db = new SQLiteDatabaseHandler(this);
        if (db.getUser() != null)
            startActivity(new Intent(this, MainActivity.class));
    }
}
