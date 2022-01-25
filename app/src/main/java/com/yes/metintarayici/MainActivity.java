package com.yes.metintarayici;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText mResultEt;
    ImageView mPreviewIv;
    TextView dil;
    private TextToSpeech TTS;

    private static final int CAMERA_REQUEST_CODE=200; 
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;

    String cameraPermission[];
    String storagePermission[];
    Dialog dialog;
    ArrayList<String> diller;
    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));


        mResultEt=findViewById(R.id.resultEt);
        mPreviewIv=findViewById(R.id.imageIv);

        cameraPermission=new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        dil=findViewById(R.id.textView);
        diller=new ArrayList<>();
        diller.add("İngilizce");
        diller.add("Türkçe");
        diller.add("Fransızca");
        diller.add("Almanca");
        diller.add("İtalyanca");
        diller.add("İspanyolca");
        diller.add("Portekizce");


        dil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.spinner);
                dialog.getWindow().setLayout(800,1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.editText);
                ListView listView=dialog.findViewById(R.id.listview);

                ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,diller);

                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TTS=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                String s=adapter.getItem(position);
                                switch(s){
                                    case "Türkçe":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("tr", "TR");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "İngilizce":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("en", "US");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "Almanca":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("de", "DE");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "Fransızca":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("fr", "FR");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "İtalyanca":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("it", "IT");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "İspanyolca":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("es", "ES");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "Portekizce":
                                        if(status==TTS.SUCCESS){
                                            Locale tr = new Locale("pt", "PT");
                                            int sonuc=TTS.setLanguage(tr);
                                            if(sonuc==TTS.LANG_MISSING_DATA || sonuc==TTS.LANG_NOT_SUPPORTED){
                                                Toast.makeText(MainActivity.this, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show();
                                            }else{
                                                ConvertToSpeech();
                                            }
                                        }else{
                                            Toast.makeText(MainActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }

                            }
                        });

                        dialog.dismiss();
                    }
                });
            }
        });




    }

    private void ConvertToSpeech(){
        String text=mResultEt.getText().toString();
        if(text==null || text.equals("")){
            Toast.makeText(this, "Boş", Toast.LENGTH_SHORT).show();
            return;
        }
        TTS.speak(text,TTS.QUEUE_FLUSH,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.addImage){
            showImageImportDialog();
        }
        if(id==R.id.copy){
            ClipboardManager clipboardManager= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip= ClipData.newPlainText("EditText",mResultEt.getText().toString());
            clipboardManager.setPrimaryClip(clip);

            Toast.makeText(this, "Kopyalandı", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        String[] items={" Kamera"," Galeri"};
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Resim Seç");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickCamera();
                    }
                }
                if(which==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickGallery();
                    }
                }
            }


        });
        dialog.create().show();
    }

    private void pickGallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode){
        case CAMERA_REQUEST_CODE:
            if(grantResults.length>0){
                boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean writeStorageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted && writeStorageAccepted){
                    pickCamera();
                }else{
                    Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        case STORAGE_REQUEST_CODE:
            if(grantResults.length>0){

                boolean writeStorageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(writeStorageAccepted){
                    pickGallery();
                }else{
                    Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_SHORT).show();
                }
            }
            break;
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Hata.", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    mResultEt.setText(sb.toString());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception exception = result.getError();
                Toast.makeText(this, "" + exception, Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onPause(){
        if(TTS !=null){
            TTS.stop();
            TTS.shutdown();
        }
        super.onPause();
    }
}
