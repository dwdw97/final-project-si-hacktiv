package com.hacktiv.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hacktiv.ecommerce.Adapter.CategorySpinnerAdapter;
import com.hacktiv.ecommerce.Model.Item;
import com.hacktiv.ecommerce.R;
import com.squareup.picasso.Picasso;

public class AddStockActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnChooseImg = findViewById(R.id.btnChooseImg);
        Spinner spinCategory = findViewById(R.id.spinnerItemCategory);
        storageReference = FirebaseStorage.getInstance().getReference("Assets");
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        String[] categories = getResources().getStringArray(R.array.item_categories);
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinFilter = findViewById(R.id.spinnerItemFilter);
                ArrayAdapter<CharSequence> arrayAdapter;
                int category = spinCategory.getSelectedItemPosition();
                switch(category) {
                    case 1:
                        spinFilter.setVisibility(View.VISIBLE);
                        arrayAdapter = ArrayAdapter.createFromResource(AddStockActivity.this, R.array.Clothing_filter, R.layout.spinner_item);
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

                        spinFilter.setAdapter(arrayAdapter);
                        break;
                    case 2:
                        spinFilter.setVisibility(View.VISIBLE);
                        arrayAdapter = ArrayAdapter.createFromResource(AddStockActivity.this, R.array.Electronic_filter, R.layout.spinner_item);
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

                        spinFilter.setAdapter(arrayAdapter);
                        break;
                    default:
                        spinFilter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(spinCategory);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView itemPic = findViewById(R.id.ivItemPic);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into((itemPic));
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void addItem(Spinner spinCategory) {
        String name = ((EditText)findViewById(R.id.txtItemName)).getText().toString().trim();
        String desc = ((EditText)findViewById(R.id.txtItemDesc)).getText().toString().trim();
        String price = ((EditText)findViewById(R.id.txtItemPrice)).getText().toString().trim();
        String quantity = ((EditText)findViewById(R.id.txtItemQuantity)).getText().toString().trim();
        String category = spinCategory.getSelectedItem().toString();
        String filter = ((Spinner)findViewById(R.id.spinnerItemFilter)).getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity)) {
            Toast.makeText(AddStockActivity.this, "There can't be any empty field!", Toast.LENGTH_SHORT).show();
        } else if(imageUri == null) {
            Toast.makeText(AddStockActivity.this, "No image file selected!", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference fileReference = storageReference
                    .child("Item_Pictures")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddStockActivity.this, "Add Item Successful.", Toast.LENGTH_LONG).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgUrl = uri.toString();
                                    storeNewItem(name, imgUrl, desc, price, quantity, category, filter);
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                        }
//                    });
        }
    }

    private void storeNewItem(String itemName, String imgUrl, String itemDesc, String itemPrice, String itemQuantity, String itemCategory, String itemFilter) {
        int price = Integer.parseInt(itemPrice);
        int quantity = Integer.parseInt(itemQuantity);

        Item newItem = new Item(itemName, imgUrl, itemDesc, price, quantity);

        if(itemCategory.equals("Clothing") || itemCategory.equals("Electronic")) {
            databaseReference.child(itemCategory).child(itemFilter).push().setValue(newItem);
        } else {
            databaseReference.child(itemCategory).push().setValue(newItem);
        }
    }
}















