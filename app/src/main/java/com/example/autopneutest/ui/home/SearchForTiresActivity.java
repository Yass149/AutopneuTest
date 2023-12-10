package com.example.autopneutest.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.autopneutest.R;

public class SearchForTiresActivity extends AppCompatActivity {

    Button rechercherButton;
    Button filtrerButton;

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

    TextView filterText;
    TextView ouText;
    TextView filterTailleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_tires);

        rechercherButton = findViewById(R.id.rechercherButton);
        filtrerButton = findViewById(R.id.filtrerButton);

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

                updateHauteurDropdownItems(selectedLargeur);
            }
        });

        hauteurAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedHauteur = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(SearchForTiresActivity.this, "Selected Hauteur: " + selectedHauteur, Toast.LENGTH_SHORT).show();

                String selectedLargeur = largeurAutoCompleteTextView.getText().toString();
                Toast.makeText(SearchForTiresActivity.this, "Selected Largeur: " + selectedLargeur, Toast.LENGTH_SHORT).show();

                updateDiametreDropdownItems(selectedLargeur, selectedHauteur);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filterText = findViewById(R.id.filterText);
        ouText = findViewById(R.id.ouText);
        filterTailleText = findViewById(R.id.filterTailleText);

        rechercherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = autoCompleteTextView.getText().toString();
                String selectedSecondItem = secondAutoCompleteTextView.getText().toString();
                String selectedThirdItem = thirdAutoCompleteTextView.getText().toString();

                String imageName = getSelectedImage(selectedItem, selectedSecondItem, selectedThirdItem);

                Intent intent = new Intent(SearchForTiresActivity.this, ShowProduct.class);
                intent.putExtra("imageName", imageName);
                startActivity(intent);
            }
        });

        filtrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLargeurItem = largeurAutoCompleteTextView.getText().toString();
                String selectedHauteurItem = hauteurAutoCompleteTextView.getText().toString();
                String selectedDiametreItem = diametreAutoCompleteTextView.getText().toString();

                String imageName = getSelectedImage1(selectedLargeurItem, selectedHauteurItem, selectedDiametreItem);

                Intent intent = new Intent(SearchForTiresActivity.this, ShowProductBySize.class);
                intent.putExtra("imageName", imageName);
                startActivity(intent);
            }
        });
    }

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

    private void updateHauteurDropdownItems(String selectedLargeur) {
        hauteurAdapterItems.clear();

        if (selectedLargeur.equals("235")) {
            hauteurAdapterItems.addAll("55", "65");
        } else if (selectedLargeur.equals("195")) {
            hauteurAdapterItems.addAll("65", "55");
        }

        hauteurAdapterItems.notifyDataSetChanged();
    }

    private void updateDiametreDropdownItems(String selectedLargeur, String selectedHauteur) {
        diametreAdapterItems.clear();

        if (selectedLargeur.equals("235") && selectedHauteur.equals("55")) {
            diametreAdapterItems.add("R17");
        } else if (selectedLargeur.equals("235") && selectedHauteur.equals("65")) {
            diametreAdapterItems.addAll("R16", "R17");
        } else if (selectedLargeur.equals("195") && selectedHauteur.equals("65")) {
            diametreAdapterItems.add("R16");
        } else if (selectedLargeur.equals("195") && selectedHauteur.equals("55")) {
            diametreAdapterItems.add("R16");
        }

        diametreAdapterItems.notifyDataSetChanged();
    }

    private String getSelectedImage(String selectedItem, String selectedSecondItem, String selectedThirdItem) {
        String imageName = "";

        if (selectedItem.equals("Mercedes-Benz") && selectedSecondItem.equals("C300") && selectedThirdItem.equals("2022")) {
            imageName = "mercedes_c300_2022";
        } else if (selectedItem.equals("Mercedes-Benz") && selectedSecondItem.equals("E350") && selectedThirdItem.equals("2022")) {
            imageName = "mercedes_e350_2022";
        } else if (selectedItem.equals("BMW") && selectedSecondItem.equals("X1") && selectedThirdItem.equals("2022")) {
            imageName = "bmw_x1";
        } else if (selectedItem.equals("BMW") && selectedSecondItem.equals("M4") && selectedThirdItem.equals("2022")) {
            imageName = "bmw_m4_2022";
        } else if (selectedItem.equals("Dacia") && selectedSecondItem.equals("Logan") && selectedThirdItem.equals("2022")) {
            imageName = "dacia_logan";
        } else if (selectedItem.equals("Dacia") && selectedSecondItem.equals("Sandero") && selectedThirdItem.equals("2022")) {
            imageName = "dacia_sandero";
        }

        return imageName;
    }

    private String getSelectedImage1(String selectedLargeurItem, String selectedHauteurItem, String selectedDiametreItem) {
        String imageName = "";

        if (selectedLargeurItem.equals("235") && selectedHauteurItem.equals("55") && selectedDiametreItem.equals("R17")) {
            imageName = "p235_55_r17";
        } else if (selectedLargeurItem.equals("235") && selectedHauteurItem.equals("65") && selectedDiametreItem.equals("R16")) {
            imageName = "p235_65_r16";
        } else if (selectedLargeurItem.equals("235") && selectedHauteurItem.equals("65") && selectedDiametreItem.equals("R17")) {
            imageName = "p235_65_r17";
        } else if (selectedLargeurItem.equals("195") && selectedHauteurItem.equals("65") && selectedDiametreItem.equals("R16")) {
            imageName = "p195_65_r16";
        } else if (selectedLargeurItem.equals("195") && selectedHauteurItem.equals("55") && selectedDiametreItem.equals("R16")) {
            imageName = "p195_55_r16";
        }
        return imageName;
    }
}
