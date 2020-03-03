package com.example.servemesystem.Homepage;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.servemesystem.R;
import com.example.servemesystem.UserModel;
import com.example.servemesystem.domain.ConstantResources;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

public class UpdateProfile extends Activity {
    CircleImageView imageview_account_profile;
    TextView updateUserNameTV;
    EditText updateFNameTV, updateLNameTV, updatePhoneTV, updateEmailTV, updateAddressTV, updateCityTV, updateStateTV, updateZipTV,
            updateCompanyNameTV, updateCompanyAddressTV, updateCompanyCityTV, updateCompanyPhoneTV;
    Button updateProfileBtn;
    LinearLayout serviceProviderUpdateLayout;
    static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    SharedPreferences sharedPreferences;

    String userName;
    String userType;
    HashMap<String, UserModel> allUsers= new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        sharedPreferences = getSharedPreferences("currUser", MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", null);
        userType = sharedPreferences.getString("type",null);

        imageview_account_profile =findViewById(R.id.imageview_account_profile);
        updateFNameTV = findViewById(R.id.updateFNameTV);
        updateLNameTV = findViewById(R.id.updateLNameTV);
        updatePhoneTV= findViewById(R.id.updatePhoneTV);
        updateEmailTV= findViewById(R.id.updateEmailTV);
        updateAddressTV= findViewById(R.id.updateAddressTV);
        updateCityTV= findViewById(R.id.updateCityTV);
        updateStateTV= findViewById(R.id.updateStateTV);
        updateZipTV= findViewById(R.id.updateZipTV);
        updateCompanyNameTV= findViewById(R.id.updateCompanyNameTV);
        updateCompanyAddressTV= findViewById(R.id.updateCompanyAddressTV);
        updateCompanyCityTV= findViewById(R.id.updateCompanyCityTV);
        updateCompanyPhoneTV= findViewById(R.id.updateCompanyPhoneTV);

        if("user".equalsIgnoreCase(userType)){
            serviceProviderUpdateLayout = findViewById(R.id.serviceProviderUpdateLayout);
            serviceProviderUpdateLayout.setVisibility(View.GONE);
        }

        imageview_account_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1234);
            }
        });
    }

    private void fetchData() {

        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UserModel post = postSnapshot.getValue(UserModel.class);
                    String uname = postSnapshot.getKey();
                    allUsers.put(uname, post);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        imageview_account_profile.setImageBitmap(bitmap);
                        myRef.child("userName").child("dp").setValue(bitmap.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}