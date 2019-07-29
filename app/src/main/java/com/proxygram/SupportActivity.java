package com.proxygram;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proxygram.model.Message;
import com.proxygram.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SupportActivity extends AppCompatActivity {

    private int GALLERY_REQUEST = 1;
    public static final String USER_KEY = "user";

    RecyclerView messages;
    ImageView attach, send;
    EditText message_text;
    String final_image;
    WebApiClient webApiClient = new WebApiClient();
    SwipeRefreshLayout swip;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Bundle extra = getIntent().getExtras();
        if (extra == null || extra.isEmpty())
            finish();

        user = (User) extra.get(USER_KEY);

        message_text = findViewById(R.id.support_edt_text);
        send = findViewById(R.id.support_img_send);
        attach = findViewById(R.id.support_img_attach);
        messages = findViewById(R.id.support_messages);
        swip = findViewById(R.id.support_swip);

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message_text.getText().toString().trim().isEmpty())
                    Toast.makeText(SupportActivity.this, "متن پیام نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                else {
                    webApiClient.sendMessage(user.getRow_id(), message_text.getText().toString(), SupportActivity.this,
                            new WebApiClient.callback<String>() {
                                @Override
                                public void onResponse(boolean ok, String response) {
                                    Toast.makeText(SupportActivity.this, response, Toast.LENGTH_LONG).show();
                                    final_image = "";
                                    message_text.setText("");
                                    loadMessages();
                                }

                                @Override
                                public void onFailure(String reason) {
                                    Toast.makeText(SupportActivity.this, reason, Toast.LENGTH_LONG).show();
                                    final_image = "";
                                    loadMessages();
                                }
                            });
                    message_text.setText("");
                }
            }
        });
        loadMessages();
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMessages();
            }
        });
    }

    private void loadMessages() {
        swip.setRefreshing(true);
        webApiClient.getMessages(user.getRow_id(), this, new WebApiClient.callback<List<Message>>() {
            @Override
            public void onResponse(boolean ok, List<Message> response) {
                swip.setRefreshing(false);
                MessagesAdapter adapter = new MessagesAdapter(response);
                LinearLayoutManager layoutManager = new LinearLayoutManager(SupportActivity.this, LinearLayoutManager.VERTICAL, false);
                messages.setLayoutManager(layoutManager);
                messages.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (response.size() > 0)
                    messages.scrollToPosition(response.size() - 1);
            }

            @Override
            public void onFailure(String reason) {
                swip.setRefreshing(false);
                Toast.makeText(SupportActivity.this, reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_REQUEST) {

            if (resultCode == RESULT_OK) {
                Uri image = data.getData();
                showCaptionModal(image);
            } else
                Toast.makeText(this, "خطا در دریافت عکس از گالری", Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(this, "خطا در تجزیه اطلاعات دریافتی", Toast.LENGTH_LONG).show();
    }

    private void showCaptionModal(final Uri image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_caption_layout, null, false);
        builder.setView(view);
        builder.setCancelable(true);

        final Button done = view.findViewById(R.id.alert_caption_send);
        final EditText caption = view.findViewById(R.id.alert_caption_caption);
        ImageView imageView = view.findViewById(R.id.alert_caption_image);


        imageView.setImageURI(image);
        caption.setText(message_text.getText());

        final AlertDialog dialog = builder.create();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                done.setActivated(false);
                done.setEnabled(false);
                done.setText("در حال ارسال ...");

                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(image);
                } catch (FileNotFoundException e) {
                    Toast.makeText(SupportActivity.this, "خطا در ارسال عکس", Toast.LENGTH_SHORT).show();
                    return;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmapImage = BitmapFactory.decodeStream(stream, null, options);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                byte[] byteArray = outputStream.toByteArray();
                final_image = Base64.encodeToString(byteArray, 0);
                if (caption.getText().toString().trim().isEmpty())
                    caption.setText(" ");
                webApiClient.sendMessage(user.getRow_id(), caption.getText().toString(), final_image, SupportActivity.this,
                        new WebApiClient.callback<String>() {
                            @Override
                            public void onResponse(boolean ok, String response) {
                                dialog.dismiss();
                                Toast.makeText(SupportActivity.this, response, Toast.LENGTH_LONG).show();
                                final_image = "";
                                message_text.setText("");
                                caption.setText("");
                                loadMessages();
                            }

                            @Override
                            public void onFailure(String reason) {
                                dialog.dismiss();
                                Toast.makeText(SupportActivity.this, reason, Toast.LENGTH_LONG).show();
                                final_image = "";
                                message_text.setText("");
                                caption.setText("");
                                loadMessages();
                            }
                        });
            }
        });
        dialog.show();
    }

}
