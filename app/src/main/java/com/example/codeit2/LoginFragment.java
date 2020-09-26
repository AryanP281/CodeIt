package com.example.codeit2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


public class LoginFragment extends Fragment
{

    boolean authEntity; //Student or admin
    private FirebaseAuth firebaseAuth;

    private boolean passwordVisible = false;
    private EditText passwordField;
    private ImageButton showPssBtn;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.registration_btn)
                loginUser();
            else
                managePssVisibility();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_login, container, false);

        if(savedInstanceState != null)
        {
            authEntity = savedInstanceState.getBoolean("AUTH_E");
            passwordVisible = savedInstanceState.getBoolean("PSS");
        }

        initializeUi(fragmentView);

        firebaseAuth = FirebaseAuth.getInstance();

        ((Button)fragmentView.findViewById(R.id.login_btn)).setOnClickListener(clickListener);
        showPssBtn.setOnClickListener(clickListener);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {
        state.putBoolean("AUTH_E", authEntity);
        state.putBoolean("PSS", passwordVisible);
    }

    private void initializeUi(View fragmentView)
    {
        //Setting title
        String title = "Welcome,\n" + (authEntity ? "Student" : "Admin");
        ((TextView)fragmentView.findViewById(R.id.login_title)).setText(title);

        passwordField = fragmentView.findViewById(R.id.login_password);
        showPssBtn = fragmentView.findViewById(R.id.login_show_password_btn);
        managePssVisibility();
    }

    private void loginUser()
    {
        /**Logs the user in firebase using the entered credentials**/

        //Getting the entered user info
        String email  = ((EditText)getView().findViewById(R.id.login_email)).getText().toString().trim(); //Getting the entered email
        String password = ((EditText)getView().findViewById(R.id.login_password)).getText().toString().trim(); //Getting the entered password

        //Checking if the entered user credentials are valid
        if(validCredentials(email, password))
        {
            //Registering the user in firebase
            Task<AuthResult> authRes = firebaseAuth.signInWithEmailAndPassword(email, password);
            authRes.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validCredentials(String email , String password)
    {
        //Checking if entered email is empty
        if (email.length() == 0) {
            Toast.makeText(getActivity(), "Email field cannot be empty", Toast.LENGTH_SHORT).show(); //Showing user the error message
            return false;
        }

        //Checking if entered email is of valid form
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show(); //Showing user the error message
            return false;
        }

        //Checking if the password is of the required strength
        int minPasswordSize = 6;
        if (password.length() < minPasswordSize)
        {
            Toast.makeText(getActivity(), "Password size has to atleast be ${minPasswordSize}", Toast.LENGTH_SHORT).show();//Showing user the error message
            return false;
        }

        return true;
    }

    private void managePssVisibility()
    {
        if(passwordVisible)
        {
            passwordVisible = false;
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordField.setSelection(passwordField.getText().length());

            showPssBtn.setImageResource(R.drawable.hidden_icon);
        }
        else
        {
            passwordVisible = true;
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordField.setSelection(passwordField.getText().length());

            showPssBtn.setImageResource(R.drawable.visible_icon);
        }
    }
}