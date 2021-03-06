package com.example.foodify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodify.Database.AppDatabase;
import com.example.foodify.Database.Entities.UserEntity;
import com.example.foodify.Login.SaveSharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * Fragment that handles the user profile
 * @author jorisbertram
 */
public class ProfileFragment extends Fragment {

    private enum Mode {
        TEXT,
        EDIT
    }

    private Button m_editButton;
    private Button m_saveButton;
    private Button m_logoutButton;

    private LinearLayout m_textContent;
    private LinearLayout m_editContent;

    private TextView m_firstNameText;
    private TextView m_lastNameText;
    private TextView m_addressText;
    private TextView m_emailText;
    private TextView m_passwordText;

    private TextView m_firstNameEdit;
    private TextView m_lastNameEdit;
    private EditText m_addressEdit;
    private EditText m_emailEdit;
    private EditText m_passwordEdit;
    private EditText m_confirmPasswordEdit;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupIcons();
        setupModeSystem();
        setMode(Mode.TEXT);

    }

    @Override
    public void onStart() {        // Retrieve list container from layout
        super.onStart();

        // Check if the user is logged in
        if(SaveSharedPreference.getUserName(getActivity()).length() == 0){
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_profileFragment_to_loginActivity);
        }
        else{
            // Do nothing, you're logged in!
            setupLogout();
            getUserFromDB();
        }
    }

    /**
     * SETUPS
     */
    private void setupIcons() {
        // TextView
        m_firstNameText = (TextView) getView().findViewById(R.id.textView_first_name);
        m_lastNameText = (TextView) getView().findViewById(R.id.textView_last_name);
        m_addressText = (TextView) getView().findViewById(R.id.textView_address);
        m_emailText = (TextView) getView().findViewById(R.id.textView_email);
        m_passwordText = (TextView) getView().findViewById(R.id.textView_password);

        setLeftDrawable(m_firstNameText, R.drawable.ic_id_card_solid, 50, 50);
        setLeftDrawable(m_lastNameText, R.drawable.ic_id_card_solid, 50, 50);
        setLeftDrawable(m_addressText, R.drawable.ic_map_marker_alt_solid, 50, 60);
        setLeftDrawable(m_emailText, android.R.drawable.ic_dialog_email, 50, 50);
        setLeftDrawable(m_passwordText, android.R.drawable.ic_lock_idle_lock, 50, 50);

        //EditText
        m_firstNameEdit = (TextView) getView().findViewById(R.id.editText_first_name);
        m_lastNameEdit = (TextView) getView().findViewById(R.id.editText_last_name);
        m_addressEdit = (EditText) getView().findViewById(R.id.editText_address);
        m_emailEdit = (EditText) getView().findViewById(R.id.editText_email);
        m_passwordEdit = (EditText) getView().findViewById(R.id.editText_password);
        m_confirmPasswordEdit = (EditText) getView().findViewById(R.id.editText_confirm_password);

        setLeftDrawable(m_firstNameEdit, R.drawable.ic_id_card_solid, 50, 50);
        setLeftDrawable(m_lastNameEdit, R.drawable.ic_id_card_solid, 50, 50);
        setLeftDrawable(m_addressEdit, R.drawable.ic_map_marker_alt_solid, 50, 60);
        setLeftDrawable(m_emailEdit, android.R.drawable.ic_dialog_email, 50, 50);
        setLeftDrawable(m_passwordEdit, android.R.drawable.ic_lock_idle_lock, 50, 50);
        setLeftDrawable(m_confirmPasswordEdit, android.R.drawable.ic_lock_idle_lock, 50, 50);
    }

    private void setupModeSystem() {
        // Setup Mode Buttons
        m_editButton = (Button) getView().findViewById(R.id.button_edit);
        m_saveButton = (Button) getView().findViewById(R.id.button_save);

        m_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(Mode.EDIT);
            }
        });
        m_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveData();
            }
        });

        // Get Mode Contents
        m_textContent = (LinearLayout) getView().findViewById(R.id.linearLayout_text);
        m_editContent = (LinearLayout) getView().findViewById(R.id.linearLayout_edit);
    }

    private void setupLogout(){
        m_logoutButton = (Button)getView().findViewById(R.id.button_logout);

        m_logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });
    }


    /**
     * Set mode to given mode
     * @param mode
     */
    private void setMode(ProfileFragment.Mode mode) {
        // Update Fragment
        resetMode();

        if (mode == ProfileFragment.Mode.TEXT)
        {
            m_editButton.setVisibility(View.VISIBLE);
            m_textContent.setVisibility(View.VISIBLE);
        }
        else if (mode == ProfileFragment.Mode.EDIT)
        {
            m_saveButton.setVisibility(View.VISIBLE);
            m_editContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Reset mode (show none)
     */
    private void resetMode() {
        m_editButton.setVisibility(View.GONE);
        m_saveButton.setVisibility(View.GONE);

        m_textContent.setVisibility(View.GONE);
        m_editContent.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceAsColor")
    private void setLeftDrawable(TextView text, int iconId, int iconSizeX, int iconSizeY) {

        Drawable drawable = ContextCompat.getDrawable(getContext(), iconId);
        drawable.setBounds(0, 0, iconSizeX, iconSizeY);

        int color = ResourcesCompat.getColor(getResources(), R.color.darkishBlue, null);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        text.setCompoundDrawables(drawable, null, null, null);
    }

    private void showSaveToast() {
        Context context = getContext();
        CharSequence text =  "Je profiel is opgeslagen!";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();
    }

    private void handleSaveData() {
        // Check if everything was filled in
        if( m_firstNameEdit.getText().toString().matches("") ||
                m_lastNameEdit.getText().toString().matches("") ||
                m_emailEdit.getText().toString().matches("")    ||
                m_addressEdit.getText().toString().matches("") ||
                m_passwordEdit.getText().toString().matches("") ||
                m_confirmPasswordEdit.getText().toString().matches("") )
        {
            Toast.makeText(getContext(), "Vul eerst alle velden in.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (m_passwordEdit.getText().toString().matches(m_confirmPasswordEdit.getText().toString())){
                updateUserData();
                showSaveToast();
                setMode(Mode.TEXT);
            }
            // If passwords don't match
            else{
                Toast.makeText(getContext(), "Zorg dat de paswoorden overeenkomen.",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void handleLogout(){
        SaveSharedPreference.setUserName(getContext(),"");
        ((BottomNavigationView) getActivity().findViewById(R.id.bottom_nav)).setSelectedItemId(R.id.shopFragment);
        Toast.makeText(getContext(),"Je bent nu uitgelogd.", Toast.LENGTH_SHORT).show();
    }


    /**
     * ///////////////////////
     * DATA + DATABASE METHODS
     * ///////////////////////
     */

    /**
     * Update points for given shop
     */
    private void updateUserData() {
        AppDatabase db = AppDatabase.getDatabase(getActivity());
        //UserEntity user = db.m_foodisticDAO().getUserByName(m_firstNameText.getText().toString());
        db.m_foodisticDAO().setUserData(
                m_firstNameText.getText().toString(),
                m_emailEdit.getText().toString(),
                m_addressEdit.getText().toString(),
                m_passwordEdit.getText().toString()
        );
        getUserFromDB();
    }

    private void getUserFromDB(){
        String username = SaveSharedPreference.getUserName(getContext());
        AppDatabase db = AppDatabase.getDatabase(getContext());
        UserEntity user = db.m_foodisticDAO().getUserByName(username);

        Log.v("test", username);

        if(user != null) {
            // Set Textviews to log in data
            m_firstNameText.setText(user.getFirstname());
            m_lastNameText.setText(user.getLastname());
            m_addressText.setText(user.getAddress());
            m_emailText.setText(user.getEmail());
            m_passwordText.setText(user.getPassword());

            // Set editText fields to log in data
            m_firstNameEdit.setText(user.getFirstname());
            m_lastNameEdit.setText(user.getLastname());
            m_emailEdit.setText(user.getEmail());
            m_addressEdit.setText(user.getAddress());
            m_passwordEdit.setText(user.getPassword());
        }
    }
}
