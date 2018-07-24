package com.test.android.palantr.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by henriquedealmeida on 10/05/18.
 */

public class NewPostActivity extends AppCompatActivity {

    private String LOG_TAG = NewPostActivity.class.getName();

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Bitmap media = null;
    private ImageView pictureView;
    private boolean isPictureFitToScreen = false;
    private ImageView cancelPictureView;


    @RequiresApi(api = Build.VERSION_CODES.M)
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

        pictureView = findViewById(R.id.post_picture);
        pictureView.setClipToOutline(true);
        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPictureFitToScreen) {
                    isPictureFitToScreen=false;
                    pictureView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250));
                    pictureView.setAdjustViewBounds(true);
                    pictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else{
                    isPictureFitToScreen=true;
                    pictureView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    pictureView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });

        cancelPictureView = findViewById(R.id.post_cancel_picture);
        cancelPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureView.setVisibility(View.GONE);
                cancelPictureView.setVisibility(View.GONE);
                media = null;
            }
        });

        //Temporary spinner filling up
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Geral");
        spinnerArray.add("Segurança");
        spinnerArray.add("Infraestrutura");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.post_topic);
        sItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_pic:

                if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

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
        /*LocalDate date = new LocalDate();
        LocalTime time = new LocalTime();

        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthOfYear());
        String year = String.format("%d", date.getYear());
        String hour = String.format("%02d", time.getHourOfDay());
        String minute = String.format("%02d", time.getMinuteOfHour());

        String strDate = " - " + hour + ":" + minute + " · " + day + "/" + month + "/" + year;*/

        LocalDateTime dateTime = new LocalDateTime();

        Post post = new Post(id_post, creator, body, media, signature, topic, votes, dateTime.toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReference.push().setValue(post);
        Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            media = (Bitmap) data.getExtras().get("data");



            pictureView.setImageBitmap(media);
            pictureView.setVisibility(View.VISIBLE);
            cancelPictureView.setVisibility(View.VISIBLE);
        }
    }


}

