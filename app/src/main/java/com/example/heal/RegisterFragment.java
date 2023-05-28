package com.example.heal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// RegisterFragment.java
public class RegisterFragment extends Fragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRegistrationDate;
    private Button buttonRegister;

    private MyDatabaseHelper databaseHelper;
    String username;
    String password;
    String registrationDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);


        databaseHelper = new MyDatabaseHelper(getActivity());

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextRegistrationDate = view.findViewById(R.id.editTextRegistrationDate);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        editTextRegistrationDate.setText(getCurrentDate());

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                registrationDate = editTextRegistrationDate.getText().toString();

                if (validateInputs(username, password, registrationDate)) {
                    // Сохраняем пользователя в базе данных
                    long result = databaseHelper.saveUser(username, password, registrationDate);

                    if (result != -1) {
                        /// Проверка учетных данных пользователя

                        ((MainActivity) requireActivity()).setLoggedIn(true);
                        ((MainActivity) requireActivity()).setCurrentUsername(username);
                        moveToAccountPage();
                    } else {
                        Toast.makeText(getActivity(), "Не удалось зарегистрироваться", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private boolean validateInputs(String username, String password, String registrationDate) {
        if (username.isEmpty() || password.isEmpty() || registrationDate.isEmpty()) {
            Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void moveToAccountPage() {

        AccountFragment accountFragment = AccountFragment.newInstance(username, password, registrationDate);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, accountFragment)
                .commit();

    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
