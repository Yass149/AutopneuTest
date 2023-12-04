package com.example.autopneutest.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ImageView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.autopneutest.R;

public class SearchForTiresActivity extends AppCompatActivity {

    String[] item = {"Mercedes-Benz", "BMW", "Dacia"};
    String[] seconditem = {"C300", "E350", "M4", "X1", "Logan", "Sandero"};
    String[] thirditem = {"2022"};
    String[] largeuritem = {"235", "195"};
    String[] hauteuritem = {"55", "65"};
    String[] diametreitem = {"R17", "R16"};

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView secondAutoCompleteTextView;
    AutoCompleteTextView thirdAutoCompleteTextView;
    AutoCompleteTextView largeurAutoCompleteTextView;
    AutoCompleteTextView hauteurAutoCompleteTextView;
    AutoCompleteTextView diametreAutoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> secondAdapterItems;
    ArrayAdapter<String> thirdAdapterItems;
    ArrayAdapter<String> largeurAdapterItems;
    ArrayAdapter<String> hauteurAdapterItems;
    ArrayAdapter<String> diametreAdapterItems;

    Button rechercherButton;
    Button filtrerButton;

    TextView filterText;
    TextView ouText;
    TextView filterTailleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_tires);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        secondAutoCompleteTextView = findViewById(R.id.second_auto_complete_txt);
        thirdAutoCompleteTextView = findViewById(R.id.third_auto_complete_txt);
        largeurAutoCompleteTextView = findViewById(R.id.largeur_auto_complete_txt);
        hauteurAutoCompleteTextView = findViewById(R.id.hauteur_auto_complete_txt);
        diametreAutoCompleteTextView = findViewById(R.id.diametre_auto_complete_txt);

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, item);
        secondAdapterItems = new ArrayAdapter<>(this, R.layout.list_item);
        thirdAdapterItems = new ArrayAdapter<>(this, R.layout.list_item, thirditem);
        largeurAdapterItems = new ArrayAdapter<>(this, R.layout.list_item, largeuritem);
        hauteurAdapterItems = new ArrayAdapter<>(this, R.layout.list_item);
        diametreAdapterItems = new ArrayAdapter<>(this, R.layout.list_item);

        autoCompleteTextView.setAdapter(adapterItems);
        secondAutoCompleteTextView.setAdapter(secondAdapterItems);
        thirdAutoCompleteTextView.setAdapter(thirdAdapterItems);
        largeurAutoCompleteTextView.setAdapter(largeurAdapterItems);
        hauteurAutoCompleteTextView.setAdapter(hauteurAdapterItems);
        diametreAutoCompleteTextView.setAdapter(diametreAdapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SearchForTiresActivity.this, "Item: " + selectedItem, Toast.LENGTH_SHORT).show();

                updateSecondDropdownItems(selectedItem);
            }
        });

        largeurAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLargeur = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SearchForTiresActivity.this, "Selected Largeur: " + selectedLargeur, Toast.LENGTH_SHORT).show();

                // Update hauteurAdapterItems based on selectedLargeur
                updateHauteurDropdownItems(selectedLargeur);
            }
        });

        hauteurAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedHauteur = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SearchForTiresActivity.this, "Selected Hauteur: " + selectedHauteur, Toast.LENGTH_SHORT).show();

                // Get the selected largeur
                String selectedLargeur = largeurAutoCompleteTextView.getText().toString();
                Toast.makeText(SearchForTiresActivity.this, "Selected Largeur: " + selectedLargeur, Toast.LENGTH_SHORT).show();

                // Update diametreAdapterItems based on selectedHauteur and selectedLargeur
                updateDiametreDropdownItems(selectedLargeur, selectedHauteur);
            }
        });




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rechercherButton = findViewById(R.id.rechercherButton);
        filtrerButton = findViewById(R.id.filtrerButton);

        filterText = findViewById(R.id.filterText);
        ouText = findViewById(R.id.ouText);
        filterTailleText = findViewById(R.id.filterTailleText);

        // Add your button click listeners or any additional logic here
    }

    // Handle the back button click
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateSecondDropdownItems(String selectedItem) {
        secondAdapterItems.clear();

        if (selectedItem.equals("Mercedes-Benz")) {
            secondAdapterItems.addAll("C300", "E350");
        } else if (selectedItem.equals("BMW")) {
            secondAdapterItems.addAll("M4", "X1");
        } else if (selectedItem.equals("Dacia")) {
            secondAdapterItems.addAll("Logan", "Sandero");
        }

        secondAdapterItems.notifyDataSetChanged();
    }

    private String selectedThirdItem;

    private void updateThirdDropdownItems(String selectedSecondItem) {
        thirdAdapterItems.clear();

        if (selectedSecondItem.equals("C300")) {
            thirdAdapterItems.addAll("2022");
            selectedThirdItem = "2022"; // Assign the selected item to the variable
        } else if (selectedSecondItem.equals("E350")) {
            thirdAdapterItems.addAll("2022");
            selectedThirdItem = "2022"; // Assign the selected item to the variable
        } else if (selectedSecondItem.equals("M4")) {
            thirdAdapterItems.addAll("2022");
            selectedThirdItem = "2022"; // Assign the selected item to the variable
        } else if (selectedSecondItem.equals("X1")) {
            thirdAdapterItems.addAll("2022");
            selectedThirdItem = "2022"; // Assign the selected item to the variable
        } else if (selectedSecondItem.equals("Logan")) {
            thirdAdapterItems.addAll("2022");
            selectedThirdItem = "2022"; // Assign the selected item to the variable
        } else if (selectedSecondItem.equals("Sandero")) {
            thirdAdapterItems.addAll("2022");
            selectedThirdItem = "2022"; // Assign the selected item to the variable
        }

        thirdAdapterItems.notifyDataSetChanged();

        // Make thirdAutoCompleteTextView visible
        thirdAutoCompleteTextView.setVisibility(View.VISIBLE);

        // Check conditions for showing the image
        String selectedBrand = autoCompleteTextView.getText().toString();
        if (selectedBrand.equals("Mercedes-Benz") && selectedSecondItem.equals("C300") && selectedThirdItem.equals("2022")) {
            // Show the image
            showImage();
        }
    }

    private void showImage() {
        // Create an AlertDialog to display the image
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_image_display, null);
        ImageView imageView = dialogView.findViewById(R.id.imageView);

        // Set the image resource (replace with your actual image resource)
        imageView.setImageResource(R.drawable.mercedes_c300_2022);

        // Configure the dialog
        builder.setView(dialogView)
                .setTitle("Your Image Title")
                .setPositiveButton("OK", null); // You can add more buttons if needed

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateHauteurDropdownItems(String selectedLargeur) {
        // Update hauteurAdapterItems based on selectedLargeur
        hauteurAdapterItems.clear();

        // Add logic to determine hauteur options based on selectedLargeur
        if (selectedLargeur.equals("235")) {
            hauteurAdapterItems.addAll("55", "65");
        } else if (selectedLargeur.equals("195")) {
            hauteurAdapterItems.addAll("65", "55");
        }

        hauteurAdapterItems.notifyDataSetChanged();

        // Make hauteurAutoCompleteTextView visible
        hauteurAutoCompleteTextView.setVisibility(View.VISIBLE);
    }

    private void updateDiametreDropdownItems(String selectedLargeur, String selectedHauteur) {
        // Update diametreAdapterItems based on selectedHauteur
        diametreAdapterItems.clear();

        // Add logic to determine diametre options based on selectedHauteur and selectedLargeur
        if (selectedLargeur.equals("235") && selectedHauteur.equals("55")){
            diametreAdapterItems.add("R17");
        } else if (selectedLargeur.equals("235") && selectedHauteur.equals("65")) {
            diametreAdapterItems.addAll("R16", "R17");
        } else if (selectedLargeur.equals("195") && selectedHauteur.equals("65")) {
            diametreAdapterItems.add("R16");
        } else if (selectedLargeur.equals("195") && selectedHauteur.equals("55")) {
            diametreAdapterItems.add("R16");
        }

        diametreAdapterItems.notifyDataSetChanged();

        // Make diametreAutoCompleteTextView visible
        diametreAutoCompleteTextView.setVisibility(View.VISIBLE);
    }
}
