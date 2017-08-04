package example.streetview.stamsoft.app.streetviewexample.data;

import android.bluetooth.BluetoothDevice;

public class BleData {

    BluetoothDevice device;
    double points;
    long lastUpdated;

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
        setLastUpdated(System.currentTimeMillis());
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
