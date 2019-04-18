package com.example.facedetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.google.android.gms.vision.face.FaceDetector;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar pb;
    int tag=0,flag=0;
    Bitmap bitmap;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, tag);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (requestcode == tag && resultcode == RESULT_OK && data != null) {
            b = data.getExtras();
            bitmap = (Bitmap) b.get("data");
            imageView.setImageBitmap(bitmap);
            flag=1;
        } else
            Toast.makeText(getApplicationContext(), "Please Click Picture Of You", Toast.LENGTH_LONG).show();

    }

    public void buverify(View view) {

        if(flag==0)
            Toast.makeText(this,"please click any picture of you",Toast.LENGTH_LONG).show();

        else {
            pb.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap mybitmap = bitmap;
            Paint rect = new Paint();
            rect.setStrokeWidth(5);
            rect.setColor(Color.RED);
            rect.setStyle(Paint.Style.STROKE);

            Bitmap tempBitmap = Bitmap.createBitmap(mybitmap.getWidth(), mybitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempcanvas = new Canvas(tempBitmap);
            tempcanvas.drawBitmap(mybitmap, 0, 0, null);

            FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .build();

            if (!faceDetector.isOperational()) {

                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(mybitmap).build();
            SparseArray<com.google.android.gms.vision.face.Face> faces = faceDetector.detect(frame);
            if (faces.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Face detected", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), "Face detected", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            }
        }
    }
}
