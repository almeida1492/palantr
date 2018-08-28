package com.test.android.palantr.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.test.android.palantr.Entities.Post;
import com.test.android.palantr.R;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by henriquedealmeida on 10/05/18.
 */

public class NewPostActivity extends AppCompatActivity {

    private String LOG_TAG = NewPostActivity.class.getName();

    private Bitmap bitmap = null;
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
                bitmap = null;
            }
        });

        //Temporary spinner filling up
        List<String> spinnerArray = new ArrayList<String>();
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


                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                Intent chooser = Intent.createChooser(galleryIntent, getString(R.string.choose_picture));
                startActivityForResult(chooser, RESULT_LOAD_IMAGE);

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
                && null != data.getData()) {
            try {
                InputStream stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                pictureView.setImageBitmap(bitmap);
                data.setData(null);
            } catch (IOException | SecurityException e) {
                Toast.makeText(this, "ops", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void sendPost() {
        int creator = new Random().nextInt();
        EditText postBodyEt = findViewById(R.id.post_body);
        String body = postBodyEt.getText().toString();

        EditText postSignatureEt = findViewById(R.id.post_signature);
        String signature = postSignatureEt.getText().toString();
        if (signature.equals("")) {
            signature = getString(R.string.anonymous_post);
        }

        Spinner postTopicSp = findViewById(R.id.post_topic);
        String topic = postTopicSp.getSelectedItem().toString();
        Long votes = 0L;

        //Treat current date string input
        JodaTimeAndroid.init(this);
        LocalDateTime dateTime = new LocalDateTime();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        final DatabaseReference postInDb = databaseReference.push();

        final Post post = new Post(postInDb.getKey(), creator, body, null, signature, topic, votes, dateTime.toString());

        if (bitmap != null) {
            final StorageReference images = FirebaseStorage.getInstance().getReference();
            byte[] bytes = convertBitmapToByteArray(bitmap);
            UploadTask uploadTask = images.child(postInDb.getKey()).putBytes(bytes);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getApplicationContext(), "Imagem enviada", Toast.LENGTH_SHORT).show();
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    String mediaUrl = images.child(postInDb.getKey()).getDownloadUrl().toString();
                    post.setMedia(mediaUrl);
                    postInDb.child("media").setValue(mediaUrl);
                    Toast.makeText(getApplicationContext(), "URL: " + mediaUrl, Toast.LENGTH_SHORT).show();
                    return images.child(postInDb.getKey()).getDownloadUrl();
                }
            });

        }

        postInDb.setValue(post);

        Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show();
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}

