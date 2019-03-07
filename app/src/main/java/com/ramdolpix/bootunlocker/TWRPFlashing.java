package com.ramdolpix.bootunlocker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import static com.ramdolpix.bootunlocker.MainActivity.sudo;

class TWRPFlashing extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;


    public TWRPFlashing(MainActivity activity) {
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

                sudo("reboot recovery");

            }
        }, 5000);
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            Thread.sleep(1500);
            sudo();
            sudo("mount -o rw,remount /system");
            sudo("mount -o rw,remount /dev");
            Thread.sleep(1000);
            sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_unlocked of=/dev/block/bootdevice/by-name/devinfo");
            Thread.sleep(2000);
            sudo("rm -rf /system/recovery-from-boot.p");

            sudo("rm -rf /system/wlfx0recovery-from-boot.bak0xwlf");

            sudo("rm -rf /system/bin/wlfx0install-recoverybak0xwlf");

            sudo("rm -rf /system/bin/install-recovery.sh");

            sudo("rm -rf /system/etc/install-recovery.sh");

            sudo("rm -rf /system/vendor/bin/install-recovery.sh");

            sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/twrp_unlocked.img of=/dev/block/bootdevice/by-name/recovery");
            Thread.sleep(1500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}