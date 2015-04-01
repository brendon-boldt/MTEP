package com.example.brendon.proto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by brendon on 3/18/15.
 */
public class ProfileIO {

    private final String profileName;
    private final Context context;
    private JSONObject jsonObject;


    public ProfileIO(String profileName, Context context) {
        this.profileName = profileName;
        this.context = context;
        updateJSONObject();
    }

    private void updateJSONObject() {
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(profileName+".json");
        } catch (FileNotFoundException e) {
            inputStream = context.getResources().openRawResource(R.raw.profile_default);
        }
        try {
            byte[] buffer = new byte[0x400];
            inputStream.read(buffer);
            inputStream.close();
            jsonObject = new JSONObject(new String(buffer,"UTF-8"));
        } catch (Exception e) {

        }
    }

    public void writeProfile(String key, String value) {
        updateJSONObject();
        try {
            jsonObject.put(key,value);
            OutputStream outputStream =
                    context.openFileOutput(profileName+".json",Context.MODE_PRIVATE);
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object readProfile(String key) {
        return jsonObject.optString(key);
    }

    public void writeProfileImage(Bitmap bitmap) {
        try {
            OutputStream outputStream =
                    context.openFileOutput(profileName+".png",Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap readProfileImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(context.getFilesDir()+"/"+profileName+".png");
        if (bitmap == null) {
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_action_user);
        }
        return bitmap;
    }
}
