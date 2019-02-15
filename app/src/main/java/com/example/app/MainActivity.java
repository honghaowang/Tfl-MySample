package com.example.app;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.R;
import com.example.uml.tensorflow.Classifier;
import com.example.uml.tensorflow.Logger;
import com.example.uml.tensorflow.TensorFlowImageClassifier;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import static java.lang.Math.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TensorFlowImageMainActivity";
    private static final Logger LOGGER = new Logger();

    private static final String DEFALT_ASSSETS_IMAGE = "onion.jpg";
    private static final int INPUT_SIZE = 224;
    /// for mobilenet_quantized settings
    private static final int IMAGE_MEAN = 0;
    private static final float IMAGE_STD = 255;

    //Mobilenet V2
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "MobilenetV2/Predictions/Reshape_1";
    //private static final String MODEL_FILE = "file:///android_asset/food_frozen.pb"; // non-quantized model
    // quantized model is smaller, but less accurate
    private static final String MODEL_FILE = "file:///android_asset/quantized_graph.pb";

    private static final String LABEL_FILE = "file:///android_asset/labels.txt";
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);

    private Classifier classifier;
    private static Bitmap bitmap = null;
    private Bitmap croppedBitmap = null;

    private Vector<String> resVec = new Vector<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        final TextView tv = (TextView) findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String res = onTestModelPrediction();
                tv.setText(res);
            }
        });

    }


    public String onTestModelPrediction() {
        classifier =
                TensorFlowImageClassifier.create(
                        getAssets(),
                        MODEL_FILE,
                        LABEL_FILE,
                        INPUT_SIZE,
                        IMAGE_MEAN,
                        IMAGE_STD,
                        INPUT_NAME,
                        OUTPUT_NAME);
        bitmap = getBitmapFromAsset(DEFALT_ASSSETS_IMAGE);
        if (bitmap != null) {
            Log.i("TEST for image:", DEFALT_ASSSETS_IMAGE);
            Log.i(" image size:", String.valueOf(bitmap.getWidth()) + 'x' + String.valueOf(bitmap.getHeight()));
            int cropsize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            croppedBitmap = Bitmap.createBitmap(bitmap,round((bitmap.getWidth()-cropsize)/2),round(bitmap.getHeight()-cropsize), cropsize, cropsize);   //crop image to same width and height
            croppedBitmap =  Bitmap.createScaledBitmap(croppedBitmap, INPUT_SIZE,INPUT_SIZE , false);
            croppedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        } else {
            Log.i("Load for image failed! ", DEFALT_ASSSETS_IMAGE);

        }
        final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
        LOGGER.i("Detect: %s", results);
        //String res = String.format("%s", results);

        // parse
        resVec = getResVec();

        String res = "";
        for(int i = 0; i < results.size(); i++) {
            Classifier.Recognition t = results.get(i);
            int id = Integer.valueOf(t.getId());
            LOGGER.i("Result", resVec.get(id));
            res += resVec.get(id);
        }

        return res;
    }

    private Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    private Vector<String> getResVec()
    {
        String actualFilename = "result.txt";
        AssetManager assetManager =  getAssets();
        Vector<String> vec = new Vector<String>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open(actualFilename)));
            String line;
            while ((line = br.readLine()) != null) {
                vec.add(line);
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading label file!" , e);
        }
        return vec;
    }


}
