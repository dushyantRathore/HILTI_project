package com.fingerprint.sample;

/**
 * Created by brainbreaker on 14/02/18.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DecoderActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    private ViewGroup mainLayout;

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private CheckBox flashlightCheckBox;
    private CheckBox enableDecodingCheckBox;
    private PointsOverlayView pointsOverlayView;
    final boolean[] displayed = new boolean[1];

    public static final String URL = "http://e0c823a2.ngrok.io/transactions/new";
    SweetAlertDialog pDialog;
    Gson gson;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_decoder);
        gson = new Gson();
        displayed[0] = false;
        mainLayout = (ViewGroup) findViewById(R.id.main_layout);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }
    }

    @Override protected void onResume() {
        super.onResume();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    @Override protected void onPause() {
        super.onPause();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed

    @Override public void onQRCodeRead(final String text, PointF[] points) {
        resultTextView.setText(text);
        pointsOverlayView.setPoints(points);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!displayed[0]) {
                    sweetAlert(text);
                    displayed[0] = true;
                }
            }
        }, 2000);
    }

    public void sweetAlert(final String text) {
        final String txnHash = "76a91424f046543cfce2354562bf60876ceb";
        new SweetAlertDialog(DecoderActivity.this)
                .setTitleText("Is data correct?")
                .setContentText(text)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        QRResult qrResult = gson.fromJson(text, QRResult.class);
                        sendJson(qrResult.item_id, qrResult.item_name, "$31.52", qrResult.quantity);
                    }
                })
                .show();
    }

    protected void sendJson(final String item_id, final String item_name, final String item_amount, final String quantity) {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost(URL);
                    json.put("item_id", item_id);
                    json.put("item_name", item_name);
                    json.put("item_amount", item_amount);
                    json.put("item_quantity", quantity);
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    /*Checking response */
                    if(response!=null){
                        final InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        final String jsonResponse = convertStreamToString(in);
                        new SweetAlertDialog(DecoderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Smart Contract Initiated")
                                .setContentText("Your Unique txn hash - " + gson.fromJson(jsonResponse, PayResponse.class).hash)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Toast.makeText(DecoderActivity.this, "Hash copied to Clipboard", Toast.LENGTH_LONG).show();
                                        sendLongSMS(gson.fromJson(jsonResponse, PayResponse.class).hash);
                                        finish();
                                    }
                                })
                                .show();
                    }


                } catch(Exception e) {
                    e.printStackTrace();
                    new SweetAlertDialog(DecoderActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void sendLongSMS(String text) {

        String phoneNumber = "9971328639";
        String message = "Your transaction with hash - " + text + " corresponding to your supply has been validated.";

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
        System.out.println("TEXT MESSAGE SENT");
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override public void onClick(View view) {
                    ActivityCompat.requestPermissions(DecoderActivity.this, new String[] {
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void initQRCodeReaderView() {
        View content = getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);

        qrCodeReaderView = (QRCodeReaderView) content.findViewById(R.id.qrdecoderview);
        resultTextView = (TextView) content.findViewById(R.id.result_text_view);
        flashlightCheckBox = (CheckBox) content.findViewById(R.id.flashlight_checkbox);
        enableDecodingCheckBox = (CheckBox) content.findViewById(R.id.enable_decoding_checkbox);
        pointsOverlayView = (PointsOverlayView) content.findViewById(R.id.points_overlay_view);

        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        flashlightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setTorchEnabled(isChecked);
            }
        });
        enableDecodingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setQRDecodingEnabled(isChecked);
            }
        });
        qrCodeReaderView.startCamera();
    }
}