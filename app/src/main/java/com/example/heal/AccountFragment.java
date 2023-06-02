package com.example.heal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

// AccountFragment.java
public class AccountFragment extends Fragment {

    FirebaseAuth auth;
    Button buttonLogout, buttonSave;
    TextView textViewEmail, textViewName;
    FirebaseUser user;
    EditText editTextName, editTextSpent, editTextDial;
    String data, oldData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        buttonLogout = view.findViewById(R.id.btn_logout);
        buttonSave = view.findViewById(R.id.btn_save);
        textViewEmail = view.findViewById(R.id.email);
        textViewName = view.findViewById(R.id.name);
        editTextSpent = view.findViewById(R.id.edit_spend);
        editTextDial = view.findViewById(R.id.edit_dial);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, user.getDisplayName());
            textViewEmail.setText(user.getEmail());
            textViewName.setText(Objects.requireNonNull(user.getDisplayName()).split(";")[0]);
            editTextSpent.setText(Objects.requireNonNull(user.getDisplayName()).split(";")[1]);
            editTextDial.setText(Objects.requireNonNull(user.getDisplayName()).split(";")[2]);
        }

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextName = new EditText(getContext());

                ViewGroup.LayoutParams layoutParams = textViewName.getLayoutParams();
                editTextName.setLayoutParams(layoutParams);

                editTextName.setText(textViewName.getText());
                editTextName.setId(R.id.name);
                editTextName.setTextColor(textViewName.getCurrentTextColor());
                editTextName.setGravity(Gravity.CENTER);
                editTextName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                ViewGroup parent = (ViewGroup) textViewName.getParent();

                int index = parent.indexOfChild(textViewName);

                parent.removeView(textViewName);

                parent.addView(editTextName, index);

                editTextName.requestFocus();
                editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (view.findViewById(R.id.name) instanceof EditText) {

                    editTextName = view.findViewById(R.id.name);

                    textViewName = new TextView(getContext());

                    ViewGroup.LayoutParams layoutParams = editTextName.getLayoutParams();
                    textViewName.setLayoutParams(layoutParams);

                    textViewName.setText(editTextName.getText());
                    textViewName.setId(R.id.name);
                    textViewName.setGravity(Gravity.CENTER);
                    textViewName.setTypeface(null, Typeface.BOLD);
                    textViewName.setTextColor(editTextName.getCurrentTextColor());
                    textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                    ViewGroup parent = (ViewGroup) editTextName.getParent();

                    int index = parent.indexOfChild(editTextName);

                    parent.removeView(editTextName);

                    parent.addView(textViewName, index);

                    textViewName.setFocusable(false);
                    textViewName.setInputType(InputType.TYPE_NULL);

                    textViewName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            EditText editTextName = new EditText(getContext());

                            ViewGroup.LayoutParams layoutParams = textViewName.getLayoutParams();
                            editTextName.setLayoutParams(layoutParams);

                            editTextName.setText(textViewName.getText());
                            editTextName.setId(R.id.name);
                            editTextName.setTextColor(textViewName.getCurrentTextColor());
                            editTextName.setGravity(Gravity.CENTER);
                            editTextName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                            ViewGroup parent = (ViewGroup) textViewName.getParent();

                            int index = parent.indexOfChild(textViewName);

                            parent.removeView(textViewName);

                            parent.addView(editTextName, index);

                            editTextName.requestFocus();
                            editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    });
                }

                oldData = user.getDisplayName();

                data = textViewName.getText().toString() + ";" + editTextSpent.getText().toString()
                        + ";" + editTextDial.getText().toString() + ";" + oldData.split(";")[3];

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(data)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
            }
        });

        return view;
    }
}
