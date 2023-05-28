package com.example.heal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// AccountFragment.java
public class AccountFragment extends Fragment {
    private TextView textViewUsername;
    private TextView textViewPassword;
    private TextView textViewRegistrationDate;
    private Button buttonLogout;

    private MyDatabaseHelper databaseHelper;
    private String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new MyDatabaseHelper(getActivity());

        // Получаем имя пользователя из аргументов
        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewPassword = view.findViewById(R.id.textViewPassword);
        textViewRegistrationDate = view.findViewById(R.id.textViewRegistrationDate);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            // Отображение имени пользователя
            textViewUsername.setText("Username: " + username);
        }

        // Получение данных о пользователе из предыдущей активити или фрагмента
        if (getArguments() != null) {
            String username = getArguments().getString("username");
            String password = getArguments().getString("password");
            String registrationDate = getArguments().getString("registrationDate");

            // Отображение данных пользователя
            textViewUsername.setText("Username: " + username);
            textViewPassword.setText("Password: " + password);
            textViewRegistrationDate.setText("Registration Date: " + registrationDate);
        }

        // Обработчик нажатия кнопки "Выход"
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Действия при нажатии кнопки "Выход"
                // Например, возвращение к окну регистрации
                ((MainActivity) requireActivity()).setLoggedIn(false);
                // Создаем новый экземпляр фрагмента регистрации
                RegisterFragment registerFragment = new RegisterFragment();

                // Заменяем текущий фрагмент на фрагмент регистрации
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, registerFragment)
                        .commit();
            }
        });

        return view;
    }
    public static AccountFragment newInstance(String username, String password, String registrationDate) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("password", password);
        args.putString("registrationDate", registrationDate);
        fragment.setArguments(args);
        return fragment;
    }
}
