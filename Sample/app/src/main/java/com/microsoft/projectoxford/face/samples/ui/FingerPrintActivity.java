package com.microsoft.projectoxford.face.samples.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.microsoft.projectoxford.face.samples.R;
import com.microsoft.projectoxford.face.samples.helper.ImageHelper;

public class FingerPrintActivity extends AppCompatActivity {

    private static final String TAG = "FingerPrintActivity";

    private final int REQUEST_CODE_ADD_PROBE = 100;
    private final int REQUEST_CODE_ADD_CANDIDATE = 101;
    private final int THRESHOLD = 40;


    private Button mBtnAddProbe = null;

    private Button mBtnAddCandidate = null;

    private Button mBtnDetect = null;

    private ImageView mImageProbe = null;

    private ImageView mImageCandidate = null;

    private TextView mTextResult = null;

    private Uri mUriProbe = null;
    private Uri mUriCandidate = null;

    private Bitmap mBitmapProbe = null;
    private Bitmap mBitmapCadidate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        mBtnAddProbe = (Button) findViewById(R.id.button_add_probe);
        mBtnAddProbe.setOnClickListener(view -> {
            Intent intent = new Intent(FingerPrintActivity.this, SelectImageActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_PROBE);
        });
        mBtnAddCandidate = (Button) findViewById(R.id.button_add_candidate);
        mBtnAddCandidate.setOnClickListener(view -> {
            Intent intent = new Intent(FingerPrintActivity.this, SelectImageActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CANDIDATE);
        });
        mBtnDetect = (Button) findViewById(R.id.button_detect);
        mBtnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectFinger();
            }
        });

        mImageCandidate = (ImageView) findViewById(R.id.image_candidate);
        mImageProbe = (ImageView) findViewById(R.id.image_probe);

        mTextResult = (TextView) findViewById(R.id.text_result);
    }

    private void detectFinger() {
        if (mBitmapProbe != null && mBitmapCadidate != null) {

            //Files.readAllBytes()

//            ByteBuffer bufferProbe = ByteBuffer.allocate(mBitmapProbe.getByteCount());
//            mBitmapProbe.copyPixelsToBuffer(bufferProbe);
//
//            byte[] arrayProbe = bufferProbe.array();

            FingerprintTemplate probe = new FingerprintTemplate().create(mBitmapProbe);

//            ByteBuffer bufferCandidate = ByteBuffer.allocate(mBitmapCadidate.getByteCount());
//            mBitmapCadidate.copyPixelsToBuffer(bufferCandidate);
//
//            byte[] arrayCandidate = bufferCandidate.array();

            FingerprintTemplate candidate = new FingerprintTemplate().create(mBitmapCadidate);

            double score = new FingerprintMatcher()
                    .index(probe)
                    .match(candidate);

            boolean matches = score >= THRESHOLD;

            mTextResult.setText("score=" + score + " matches=" + matches);

        } else {
            mTextResult.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult requestCode=" + requestCode + " data=" + data);
        switch (requestCode) {
            case REQUEST_CODE_ADD_PROBE:
                if (resultCode == Activity.RESULT_OK) {

                    mUriProbe = data.getData();
                    mBitmapProbe = ImageHelper.loadSizeLimitedBitmapFromUri(
                            mUriProbe, getContentResolver());
                    if (mBitmapProbe != null && mImageProbe != null) {
                        // Show the image on screen.
                        mImageProbe.setImageBitmap(mBitmapProbe);
                    }

                }
                break;
            case REQUEST_CODE_ADD_CANDIDATE:
                if (resultCode == Activity.RESULT_OK) {

                    mUriCandidate = data.getData();
                    mBitmapCadidate = ImageHelper.loadSizeLimitedBitmapFromUri(
                            mUriCandidate, getContentResolver());

                    if (mBitmapCadidate != null && mImageCandidate != null) {
                        // Show the image on screen.
                        mImageCandidate.setImageBitmap(mBitmapCadidate);
                    }

                }
                break;
        }


    }
}
