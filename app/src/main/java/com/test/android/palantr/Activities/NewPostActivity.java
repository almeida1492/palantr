package com.test.android.palantr.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by henriquedealmeida on 10/05/18.
 */

public class NewPostActivity extends AppCompatActivity {

    private String LOG_TAG = NewPostActivity.class.getName();

    private Bitmap media = null;
    private ImageView pictureView;
    private boolean isPictureFitToScreen = false;
    private ImageView cancelPictureView;

    private int RESULT_LOAD_IMAGE = 1;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_pic_from_gallery:

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            Bitmap b = BitmapFactory.decodeFile(picturePath.toString());

            pictureView.setImageBitmap(b);
        }
        else {
            Toast.makeText(this, "ops", Toast.LENGTH_SHORT).show();
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
        LocalDateTime dateTime = new LocalDateTime();

        Post post = new Post(id_post, creator, body, media, signature, topic, votes, dateTime.toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        databaseReference.push().setValue(post);
        Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show();
    }
}

