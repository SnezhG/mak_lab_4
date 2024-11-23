package com.example.mak_lab_4;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private TextView tvDirection;
    private ImageView compassImage;

    private float[] gravity;
    private float[] geomagnetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        tvDirection = findViewById(R.id.tvDirection);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                float azimuth = (float) Math.toDegrees(orientation[0]);
                if (azimuth < 0) {
                    azimuth += 360;
                }

                String direction = getDirectionFromAzimuth(azimuth);

                tvDirection.setText("Направление: " + direction);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private String getDirectionFromAzimuth(float azimuth) {
        if (azimuth >= 337.5 || azimuth < 22.5) {
            return "Север";
        } else if (azimuth >= 22.5 && azimuth < 67.5) {
            return "Северо-Восток";
        } else if (azimuth >= 67.5 && azimuth < 112.5) {
            return "Восток";
        } else if (azimuth >= 112.5 && azimuth < 157.5) {
            return "Юго-Восток";
        } else if (azimuth >= 157.5 && azimuth < 202.5) {
            return "Юг";
        } else if (azimuth >= 202.5 && azimuth < 247.5) {
            return "Юго-Запад";
        } else if (azimuth >= 247.5 && azimuth < 292.5) {
            return "Запад";
        } else {
            return "Северо-Запад";
        }
    }

}
