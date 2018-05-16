package com.test.android.palantr.Activities;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by henriquedealmeida on 10/05/18.
 */

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        //Set up ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        //Temporary spinner filling up
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Geral");
        spinnerArray.add("Segurança");
        spinnerArray.add("Infraestrutura");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.post_topic);
        sItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_pic:

                return true;
            case R.id.add_signature:

                return true;
            case R.id.add_topic:

                return true;
            case R.id.send_post:
                sendPost();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendPost() {
        int id_post = new Random().nextInt();
        int creator = new Random().nextInt();
        EditText postBodyEt = findViewById(R.id.post_body);
        String body = postBodyEt.getText().toString();
        Bitmap media = null;

        EditText postSignatureEt = findViewById(R.id.post_signature);
        String signature = postSignatureEt.getText().toString();
        if (signature.equals("")){
            signature = getString(R.string.anonymous_post);
        }

        Spinner postTopicSp = findViewById(R.id.post_topic);
        String topic = postTopicSp.getSelectedItem().toString();
        Long votes = 0L;

        //Treat current date string input
        JodaTimeAndroid.init(this);
        LocalDate date = new LocalDate();
        LocalTime time = new LocalTime();

        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthOfYear());
        String year = String.format("%d", date.getYear());
        String hour = String.format("%02d", time.getHourOfDay());
        String minute = String.format("%02d", time.getMinuteOfHour());

        String strDate = " - " + hour + ":" + minute + " · " + day + "/" + month + "/" + year;

        Post post = new Post(id_post, creator, body, media, signature, topic, votes, strDate);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReference.push().setValue(post);
        Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show();
    }
}

