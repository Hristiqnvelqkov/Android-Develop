package example.streetview.stamsoft.app.streetviewexample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import example.streetview.stamsoft.app.streetviewexample.data.BleData;
import example.streetview.stamsoft.app.streetviewexample.interfaces.LumiBleListener;

public class LumiManager {

    private static LumiManager instance;

    public static LumiManager getInstance(Activity context) {
        if (instance == null) {
            instance = new LumiManager(context);
        }
        return instance;
    }

    public static final String ACTION_DATA_UPDATE = "ACTION_DATA_UPDATE";

    private Activity context;
    private Map<Integer, BleData> devices;

    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 60000;
    //    private BluetoothGatt mGatt;
    private boolean connect = false;
    public static final int START_BLUETOOTH = 100;
    private PowerManager.WakeLock wakeLock;
    LumiBleListener listener;
    private List<String> devicesName;
    private List<BluetoothDevice> currentConnectedDevices;
    private List<BluetoothGatt> currentConnectedGatts;
    boolean availableForDiscover = true;

    ExecutorService executor;

    private LumiManager(Activity context) {
        this.context = context;
        devices = new HashMap<>();
        currentConnectedDevices = new ArrayList<>();
        devicesName = new ArrayList<>();
        currentConnectedGatts = new ArrayList<>();

        executor = Executors.newFixedThreadPool(6);
    }

    public Map<Integer, BleData> getDevices() {
        return devices;
    }

    public void setListener(LumiBleListener listener) {
        this.listener = listener;
    }

    public void setupBle(LumiBleListener listener) {
        BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mHandler = new Handler(Looper.getMainLooper());

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        this.listener = listener;
    }

    public void scanBle() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false, null);
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void scanLeDevice(final boolean enable, String deviceName) {
        if (mBluetoothAdapter == null) {
            return;
        }
        if (enable) {
            if (deviceName != null && !devicesName.contains(deviceName)) {
                devicesName.add(deviceName);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    boolean success = mBluetoothAdapter.startLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            boolean success = mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.d("Device1", "Name success " + success);

        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            if (deviceName != null && !devicesName.contains(deviceName)) {
                devicesName.remove(deviceName);
            }
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          if (device != null) {
                                if ((device.getName() != null) && devicesName.contains(device.getName().toLowerCase())) {
                                    Log.d("Device1", "Name " + device.getName());
                                    connectToDevice(device);
                                }
                            }
                        }
                    });
                }
            };

    public synchronized void connectToDevice(BluetoothDevice device) {

        if (!currentConnectedDevices.contains(device)) {
            BluetoothGatt gatt = device.connectGatt(context, false, createCallback());

            Log.d("Device1", "tryToconnet " + device.getName());

            for (BluetoothGatt oldGatt : currentConnectedGatts) {
                if (oldGatt.getDevice().getName().equals(device.getName())) {
                    currentConnectedGatts.remove(oldGatt);
                    break;
                }
            }
            currentConnectedGatts.add(gatt);
            currentConnectedDevices.add(device);
//            scanLeDevice(false, null);
        } else {
            Log.d("Device1", "connectToDevice " + device.getName());
        }

    }


    private BluetoothGattCallback createCallback() {
        return new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                switch (newState) {
                    case BluetoothProfile.STATE_CONNECTED:
                        listener.onConnected();
                        connect(gatt);
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        listener.onDisconnected();
                        disconnect(true, gatt);
                        break;
                    case BluetoothProfile.STATE_DISCONNECTING:
                        Log.w("Test", "STATE_DISCONNECTING");
                        disconnect(true, gatt);
                        break;
                    default:
                        Log.w("gattCallback", "STATE_OTHER");
                }

            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                availableForDiscover = true;
                List<BluetoothGattService> services = gatt.getServices();
                BluetoothGattCharacteristic therm_char;
                Log.d("Device1", "Service " + gatt.getDevice().getName());
                for (BluetoothGattService s : services) {
                    if (s.getUuid().toString().toLowerCase().contains("180F".toLowerCase())) {
                        for (BluetoothGattCharacteristic c : s.getCharacteristics()) {
                            if (c.getUuid().toString().toLowerCase().contains("2A19".toLowerCase())) {
                                therm_char = c;
                                gatt.setCharacteristicNotification(therm_char, true);
                                for (BluetoothGattDescriptor descriptor : therm_char.getDescriptors()) {
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    gatt.writeDescriptor(descriptor);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt,
                                             BluetoothGattCharacteristic
                                                     characteristic, int status) {
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                updateBleDevice(gatt);
//            pointCounter += 0.05;
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (points != null) {
//                        points.setText(String.valueOf((int) pointCounter));
//                    }
//                }
//            });
            }
        };
    }

    public void connect(final BluetoothGatt gatt) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        int counter = 0;
                        while (!availableForDiscover) {
                            try {
                                Thread.sleep(50);
                                counter += 50;
                                if (counter == 3000) { //VICHO: was 1000
                                    break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        availableForDiscover = false;
                        Log.d("Device1", "Connected " + gatt.getDevice().getName());
                        gatt.connect();
                        gatt.discoverServices();
                    }
                });

//        if (!connect) {
//            Log.w("gattCallback", "STATE_CONNECTED");
//            connect = true;
//            System.out.println("Device1 Connected " + gatt.getDevice().getName());
//
////            if (mGatt != null) {
////                mGatt.connect();
////                mGatt.discoverServices();
////            }
//            gatt.connect();
//            gatt.discoverServices();
//        }
    }

    public void finish() {
        Log.w("gattCallback", "STATE_DISCONNECTED");
        connect = false;

//        if (mGatt != null) {
//            mGatt.disconnect();
//        }
        for (BluetoothGatt gatt : currentConnectedGatts) {
            gatt.disconnect();
        }
        currentConnectedDevices.clear();
    }

    public void disconnect(boolean withScan, BluetoothGatt localGatt) {
        if (localGatt == null) {
            for (BluetoothGatt gatt : currentConnectedGatts) {
                gatt.disconnect();
            }
            connect = false;
            currentConnectedDevices.clear();
        } else {
            for (BluetoothGatt gatt : currentConnectedGatts) {
                if (gatt.getDevice().getName().equals(localGatt.getDevice().getName())) {
                    gatt.disconnect();
                    for (BluetoothDevice device : currentConnectedDevices) {
                        if (device.getName().equals(gatt.getDevice().getName())) {
                            currentConnectedDevices.remove(device);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        if (withScan) {
            scanLeDevice(true, null);
        }
    }

    public boolean isConnect() {
        return connect;
    }

    public void onResume() {
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    public void onPause() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    public void onDestroy() {
        for (BluetoothGatt gatt : currentConnectedGatts) {
            gatt.disconnect();
            gatt.close();
        }
    }

    public void updateBleDevice(BluetoothGatt gatt) {
        for (BluetoothDevice currentDevice : currentConnectedDevices) {
            if (currentDevice != null && currentDevice.getName().equals(gatt.getDevice().getName())) {
                Log.d("Device1", "Updated " + gatt.getDevice().getName());
                boolean hasMatch = false;
                for (Integer i : devices.keySet()) {
                    BleData data = devices.get(i);
                    if (data.getDevice().getAddress().equals(currentDevice.getAddress())) {
                        data.setPoints(data.getPoints() + 0.1);
                        addTotal();
                        hasMatch = true;
                        break;
                    }
                }
                if (!hasMatch) {
                    BleData data = new BleData();
                    data.setPoints(0.1);
                    data.setDevice(currentDevice);
                    devices.put(devicesName.indexOf(currentDevice.getName().toLowerCase()), data);
                    addTotal();
                }
                Intent intent = new Intent(ACTION_DATA_UPDATE);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }
    }

    private void addTotal() {
        double totalPrefScore = PreferenceManager.getDefaultSharedPreferences(context).
                getFloat(context.getString(R.string.pref_total), 0);

        Log.d("Device1", "totalPrefScore " + totalPrefScore);

        double newTotal = totalPrefScore + 0.1;

        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putFloat(context.getString(R.string.pref_total), (float) newTotal).commit();
    }

    public List<String> getDevicesName() {
        return devicesName;
    }
}
