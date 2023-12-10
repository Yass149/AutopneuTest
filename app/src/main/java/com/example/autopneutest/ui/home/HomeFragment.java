package com.example.autopneutest.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.autopneutest.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView imageViewToAnotherPage = view.findViewById(R.id.imageViewToAnotherPage);

        ImageView imageView3 = view.findViewById(R.id.imageView3);



        imageViewToAnotherPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAnotherActivity();
            }
        });



        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for imageView3
                handleThirdImageClick();
            }
        });


        return view;
    }

    private void navigateToAnotherActivity() {
        Intent intent = new Intent(requireContext(), SearchForTiresActivity.class);
        startActivity(intent);
    }

    private void handleThirdImageClick() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_home_to_catalog);
    }

    private void handleAppointmentsButtonClick() {
        // Handle the click for the appointments button
        NavHostFragment.findNavController(this)
                .navigate(R.id.nav_view_appointments);
    }


    public void appointementfragment(View view) {
    }
}
