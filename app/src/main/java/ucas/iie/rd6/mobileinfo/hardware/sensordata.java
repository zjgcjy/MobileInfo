package ucas.iie.rd6.mobileinfo.hardware;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;

import ucas.iie.rd6.mobileinfo.R;

public class sensordata extends Activity
        implements SensorEventListener {
    // 定义Sensor管理器
    private SensorManager mSensorManager;
    float[] values = new float[3];//用来保存最终的结果
    float[] gravity = new float[3];//用来保存加速度传感器的值
    float[] r = new float[9];//
    float[] geomagnetic = new float[3];//用来保存地磁传感器的值
    EditText etOrientation;
    EditText etGyro;
    EditText etMagnetic;
    EditText etGravity;
    EditText etLinearAcc;
    EditText etTemerature;
    EditText etLight;
    EditText etPressure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_sensor);
        // 获取界面上的EditText组件
        etOrientation = (EditText) findViewById(R.id.etOrientation);
        etGyro = (EditText) findViewById(R.id.etGyro);
        etMagnetic = (EditText) findViewById(R.id.etMagnetic);
        etGravity = (EditText) findViewById(R.id.etGravity);
        etLinearAcc = (EditText) findViewById(R.id.etLinearAcc);
        etTemerature = (EditText) findViewById(R.id.etTemerature);
        etLight = (EditText) findViewById(R.id.etLight);
        etPressure = (EditText) findViewById(R.id.etPressure);
        // 获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  // ①
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 为系统的陀螺仪传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_UI);
        // 为系统的磁场传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
        // 为系统的重力传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_UI);
        // 为系统的线性加速度传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_UI);
        // 为系统的温度传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_UI);
        // 为系统的光传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_UI);
        // 为系统的压力传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    // 以下是实现SensorEventListener接口必须实现的方法
    @Override
    // 当传感器精度改变时回调该方法。
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void getValue() {
        // r从这里返回
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        //values从这里返回
        SensorManager.getOrientation(r, values);
        //提取数据
        float azimuth = (float) Math.toDegrees(values[0]);
        if (azimuth < 0) {
            azimuth = azimuth + 360;
        }
        float pitch = (float) Math.toDegrees(values[1]);
        float roll = (float) Math.toDegrees(values[2]);
        etOrientation.setText("方位角:" + azimuth + "\n倾斜角:" + pitch + "\n滚动角:" + roll);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        // 获取触发event的传感器类型
        int sensorType = event.sensor.getType();
        StringBuilder sb = null;
        // 判断是哪个传感器发生改变
        switch (sensorType) {
            // 陀螺仪传感器
            case Sensor.TYPE_GYROSCOPE:
                sb = new StringBuilder();
                sb.append("绕X轴：");
                sb.append(values[0]);
                sb.append("\n绕Y轴：");
                sb.append(values[1]);
                sb.append("\n绕Z轴：");
                sb.append(values[2]);
                etGyro.setText(sb.toString());
                break;
            // 磁场传感器
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values;
                sb = new StringBuilder();
                sb.append("X轴方向：");
                sb.append(values[0]);
                sb.append("\nY轴方向：");
                sb.append(values[1]);
                sb.append("\nZ轴方向：");
                sb.append(values[2]);
                etMagnetic.setText(sb.toString());
                break;
            // 重力传感器
            case Sensor.TYPE_GRAVITY:
                sb = new StringBuilder();
                sb.append("X轴方向：");
                sb.append(values[0]);
                sb.append("\nY轴方向：");
                sb.append(values[1]);
                sb.append("\nZ轴方向：");
                sb.append(values[2]);
                etGravity.setText(sb.toString());
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values;
                getValue();
                // 线性加速度传感器
            case Sensor.TYPE_LINEAR_ACCELERATION:
                gravity = event.values;
                getValue();
                sb = new StringBuilder();
                sb.append("X轴方向：");
                sb.append(values[0]);
                sb.append("\nY轴方向：");
                sb.append(values[1]);
                sb.append("\nZ轴方向：");
                sb.append(values[2]);
                etLinearAcc.setText(sb.toString());
                break;
            // 温度传感器
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sb = new StringBuilder();
                sb.append("当前温度为：");
                sb.append(values[0]);
                etTemerature.setText(sb.toString());
                break;
            // 光传感器
            case Sensor.TYPE_LIGHT:
                sb = new StringBuilder();
                sb.append("当前光的强度为：");
                sb.append(values[0]);
                etLight.setText(sb.toString());
                break;
            // 压力传感器
            case Sensor.TYPE_PRESSURE:
                sb = new StringBuilder();
                sb.append("当前压力为：");
                sb.append(values[0]);
                etPressure.setText(sb.toString());
                break;
        }
    }
}

