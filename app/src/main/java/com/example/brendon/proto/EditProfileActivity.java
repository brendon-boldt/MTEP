package com.example.brendon.proto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


public class EditProfileActivity extends Activity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static int REQUEST_SELECT_IMAGE = 1;
    private static int REQUEST_CROP_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button done = (Button) findViewById(R.id.done_edit_profile_button);
        done.setOnClickListener(this);
        //Button image = findViewById(R.id.edit_profile_image);
        ImageView image = (ImageView) findViewById(R.id.edit_profile_image);
        image.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.edit_temperament_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(this, R.array.temperaments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        loadCurrentProfile("profile");
    }

    private void loadCurrentProfile(String profileName) {
        ProfileIO profileIO = new ProfileIO(profileName,this);
        ((EditText) findViewById(R.id.edit_profile_name))
                .setText((String) profileIO.readProfile("name"));
        Spinner spinner = ((Spinner) findViewById(R.id.edit_temperament_spinner));
        spinner.setSelection(getIndex(spinner, (String) profileIO.readProfile("temperament")));

        Bitmap image = profileIO.readProfileImage();
        if (image == null) {
            ((ImageView) findViewById(R.id.edit_profile_image))
                    .setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
            //image = getResources().getDrawable(R.drawable.ic_action_user).;
        }
        ((ImageView) findViewById(R.id.edit_profile_image))
                .setImageBitmap(image);
    }

    private int getIndex(Spinner spinner, String value) {
        for(int i = 0; i < spinner.getCount(); ++i) {
            if (spinner.getItemAtPosition(i).equals(value))
                return i;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_edit_profile_button:
                String name = ((EditText) findViewById(R.id.edit_profile_name))
                        .getText().toString();
                String temperament = ((Spinner) findViewById(R.id.edit_temperament_spinner))
                        .getSelectedItem().toString();
                ProfileIO profileIO = new ProfileIO("profile",this);
                profileIO.writeProfile("name",name);
                profileIO.writeProfile("temperament",temperament);
                Bitmap bitmap =
                        ((BitmapDrawable) ((ImageView) findViewById(R.id.edit_profile_image))
                        .getDrawable()).getBitmap();
                profileIO.writeProfileImage(bitmap);

                finish();
                break;

            case R.id.edit_profile_image:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_IMAGE);
            default:
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            // Get picture from Gallery
            Uri imageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ImageView imageView = (ImageView) findViewById(R.id.edit_profile_image);

            // Crop picture
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imageUri,"image/*");
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("outputX",imageView.getWidth());
            cropIntent.putExtra("outputY",imageView.getHeight());
            cropIntent.putExtra("return-data",true);
            startActivityForResult(cropIntent, REQUEST_CROP_IMAGE);


        }
        else if (requestCode == REQUEST_CROP_IMAGE && data != null) {

            // Set picture
            Bitmap bitmap= data.getExtras().getParcelable("data");
            ImageView imageView = (ImageView) findViewById(R.id.edit_profile_image);
            imageView.setImageBitmap(bitmap);
            //ProfileIO profileIO = new ProfileIO("profile",this);
            //profileIO.writeProfileImage(bitmap);
            //loadCurrentProfile("profile");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
