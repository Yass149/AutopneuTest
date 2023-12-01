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
        ImageView imageViewToMyCart = view.findViewById(R.id.imageViewToMyCart);
        ImageView iconView = view.findViewById(R.id.iconView);


        imageViewToAnotherPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAnotherActivity();
            }
        });

        imageViewToMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCartIconClick();
            }
        });

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIconClick();
            }
        });


        return view;
    }

    private void navigateToAnotherActivity() {
        Intent intent = new Intent(requireContext(), SearchForTiresActivity.class);
        startActivity(intent);
    }

    private void handleIconClick() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_home_to_catalog);
    }

    private void handleCartIconClick() {
        // Handle the click for the shopping cart icon
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_home_to_my_cart);
    }

}
