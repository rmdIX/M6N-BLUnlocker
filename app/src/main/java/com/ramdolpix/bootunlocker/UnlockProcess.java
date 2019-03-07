package com.ramdolpix.bootunlocker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import static com.ramdolpix.bootunlocker.MainActivity.sudo;

class UnlockProcess extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;



    public UnlockProcess(MainActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle(R.string.wait);
        dialog.setMessage("Unlocking...");
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
                //Do something after 3sec
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
            sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_unlocked of=/dev/block/bootdevice/by-name/devinfo");


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}