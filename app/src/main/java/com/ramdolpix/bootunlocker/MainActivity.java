package com.ramdolpix.bootunlocker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PERMISSION = 1;

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
                    "/data/local/xbin/", "/data/local/bin/",
                    "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    private static boolean isRooted() {
        return findBinary("su");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //this means permission is granted and you can do read and write

        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
//            Toast toast = Toast.makeText(getApplicationContext(),R.string.perm,Toast.LENGTH_LONG);
//            toast.show();
            finish();
        }

        File workDirectory = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files");

        File bootloaderLockedFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_locked");
        File bootloaderUnlockedFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_unlocked");
        File unlockedtwrpRecoveryFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/twrp_unlocked.img");
        File stockRecoveryFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/stock.img");

        if (!workDirectory.exists() || workDirectory.exists()) {
            if (!workDirectory.exists()) {
                workDirectory.mkdirs();
            }
            if (!bootloaderLockedFile.exists() | !bootloaderUnlockedFile.exists()) {
                CopyLockedBootloaderFile(R.raw.bl_locked);
                CopyUnlockedBootloaderFile(R.raw.bl_unlocked);
            }
            if (!stockRecoveryFile.exists() | !unlockedtwrpRecoveryFile.exists()) {
                CopyStockRecoveryFile(R.raw.stock);
                CopyTWRPUnlockedRecoveryFile(R.raw.twrp_unlocked);
            }
        }

            if (!isRooted()) {
                Toast toast = Toast.makeText(getApplicationContext(),R.string.su_file_not_found,Toast.LENGTH_LONG);
                toast.show();
                finish();
            }

    }
    private void CopyStockRecoveryFile(int Resource) {
        String pathSDCard = Environment.getExternalStorageDirectory() +
                "/Android/data/com.ramdolpix.bootunlocker/files/stock.img"; // File path
        try {
            InputStream in = getResources().openRawResource(Resource);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void CopyTWRPUnlockedRecoveryFile(int Resource) {
        String pathSDCard = Environment.getExternalStorageDirectory() +
                "/Android/data/com.ramdolpix.bootunlocker/files/twrp_unlocked.img"; // File path
        try {
            InputStream in = getResources().openRawResource(Resource);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void CopyLockedBootloaderFile(int Resource) {
        String pathSDCard = Environment.getExternalStorageDirectory() +
                "/Android/data/com.ramdolpix.bootunlocker/files/bl_locked"; // File path
        try {
            InputStream in = getResources().openRawResource(Resource);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void CopyUnlockedBootloaderFile(int Resource) {
        String pathSDCard = Environment.getExternalStorageDirectory() +
                "/Android/data/com.ramdolpix.bootunlocker/files/bl_unlocked"; // File path
        try {
            InputStream in = getResources().openRawResource(Resource);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sudo(String... strings) {
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickUnlockBL(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.unlock_bootloader_title);
        alertDialog.setMessage(R.string.unlock_bootloader_message);

        View checkBoxView = View.inflate(this, R.layout.checkbox, null);
        final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
        checkBox.setText(R.string.install_twrp);
        alertDialog.setView(checkBoxView);
        checkBox.setChecked(true);
        final AlertDialog.Builder rest = new AlertDialog.Builder(this);
        rest.setTitle(R.string.unlock_bootloader_complete);
        rest.setMessage(R.string.press_ok_to_recovery);
        rest.setCancelable(false);
        rest.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sudo("reboot recovery");
                    }
                });
        final AlertDialog alert = rest.create();
        // Обработчик на нажатие ДА
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                sudo("mount -o rw,remount /dev");
                sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_unlocked of=/dev/block/bootdevice/by-name/devinfo");
                if (checkBox.isChecked()){
                    sudo("mount -o rw,remount /system");
                    sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/twrp_unlocked.img of=/dev/block/bootdevice/by-name/recovery");
                    sudo("rm -rf /system/recovery-from-boot.p");
                    sudo("rm -rf /system/wlfx0recovery-from-boot.bak0xwlf");
                    sudo("rm -rf /system/bin/wlfx0install-recoverybak0xwlf");
                    sudo("rm -rf /system/bin/install-recovery.sh");
                    sudo("rm -rf /system/etc/install-recovery.sh");
                    sudo("rm -rf /system/etc/install-recovery.sh");
                    sudo("rm -rf /system/vendor/bin/install-recovery.sh");
                }

                rest.show();
            }
        });
        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast noToast = Toast.makeText(getApplicationContext(),
                        R.string.canceled_by_user, Toast.LENGTH_SHORT);
                noToast.show();
            }
        });

        alertDialog.show();
    }

    public void onClickLockBL(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.lock_bootloader_title);
        alertDialog.setMessage(R.string.lock_bootloader_message);

        final AlertDialog.Builder rest = new AlertDialog.Builder(this);
        rest.setTitle(R.string.lock_bootloader_complete);
        rest.setMessage(R.string.press_ok_to_recovery);
        rest.setCancelable(false);
        rest.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sudo("reboot recovery");
                    }
                });
        final AlertDialog alert = rest.create();

        // Обработчик на нажатие ДА
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                sudo("mount -o rw,remount /dev");
                sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/bl_locked of=/dev/block/bootdevice/by-name/devinfo");
                sudo("dd if=" + Environment.getExternalStorageDirectory() + "/Android/data/com.ramdolpix.bootunlocker/files/stock.img of=/dev/block/bootdevice/by-name/recovery");
                rest.show();
            }
        });
        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast noToast = Toast.makeText(getApplicationContext(),
                        R.string.canceled_by_user, Toast.LENGTH_SHORT);
                noToast.show();
            }
        });

        alertDialog.show();
    }

}


