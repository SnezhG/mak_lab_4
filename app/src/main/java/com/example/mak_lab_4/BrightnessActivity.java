package com.example.mak_lab_4;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BrightnessActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView tvBrightnessLevel;

    private static final String TAG = "BrightnessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);

        tvBrightnessLevel = findViewById(R.id.tvBrightnessLevel);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Log.e(TAG, "Датчик освещенности отсутствует на устройстве.");
            tvBrightnessLevel.setText("Датчик освещенности недоступен.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lightSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];

            float brightness = Math.min(1f, lightLevel / 1000f);

            setScreenBrightness(brightness);

            tvBrightnessLevel.setText(String.format("Яркость: %.2f (Уровень света: %.2f люкс)", brightness, lightLevel));
        }
    }

    private void setScreenBrightness(float brightness) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightness;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
