package com.ramdolpix.bootunlocker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import static com.ramdolpix.bootunlocker.MainActivity.sudo;

class LockProcess extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;


    public LockProcess(MainActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle(R.string.wait);
        dialog.setMessage("Locking...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.setMessage("Operation Complete\nReboot in Recovery after \n5 seconds");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                sudo("reboot recovery");

            }
        }, 5000);
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            sudo();
            Thread.sleep(1000);
            sudo("mount -o rw,remount /dev");
            Thread.sleep(2000);

            sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_locked of=/dev/block/bootdevice/by-name/devinfo");
            Thread.sleep(2000);
            sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/stock.img of=/dev/block/bootdevice/by-name/recovery");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return null;
    }
}

