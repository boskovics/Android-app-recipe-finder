package com.example.aplikacija;

import static com.example.aplikacija.Recipe.parseJSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPull;
    private ListView ingredientListView;
    private AutoCompleteTextView autoComplete;
    private List<String> selectedIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        buttonPull = findViewById(R.id.buttonPull);
        buttonPull.setOnClickListener(this);
        autoComplete = findViewById(R.id.autoComplete);

        List<String> ingredientList = loadIngredientsFromCSV();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, ingredientList);

        AutoCompleteTextView autoComplete = findViewById(R.id.autoComplete);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);

        ingredientListView = findViewById(R.id.ingredientListView);
        ArrayAdapter<String> selectedIngredientsAdapter = new ArrayAdapter<>(this, R.layout.selected_ingredient, selectedIngredients);
        ingredientListView.setAdapter(selectedIngredientsAdapter);

        autoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedIngredient = adapter.getItem(position);
            selectedIngredients.add(selectedIngredient);
            selectedIngredientsAdapter.notifyDataSetChanged();
            autoComplete.setText("");
        });

        ingredientListView.setOnItemClickListener((parent, view, position, id) -> {
            String removedIngredient = selectedIngredients.get(position);
            selectedIngredients.remove(removedIngredient);
            selectedIngredientsAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onClick(View view) {
        String query = TextUtils.join(",", selectedIngredients);
        String url = "";

        if (!TextUtils.isEmpty(query)) {
            url = String.format("https://api.spoonacular.com/recipes/complexSearch?query=%s&number=20&addRecipeInformation=true&apiKey=a64ce5d75775415c949f6b10c2ac63fb", query);
        } else {
            url = "https://api.spoonacular.com/recipes/complexSearch?query=&number=20&addRecipeInformation=true&apiKey=a64ce5d75775415c949f6b10c2ac63fb";
        }

        try {
            Api.getJSON(url,
                    new ReadDataHandler() {
                        public void handleMessage(Message msg) {
                            String reply = getJson();
                            ArrayList<Recipe> recipes = null;

                            try {
                                JSONObject object = new JSONObject(reply);
                                JSONArray array = (JSONArray) object.get("results");
                                recipes = Recipe.parseJSONArray(array);
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Error parsing response!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            if (recipes != null) {
                                Intent intent = new Intent(MainActivity.this, ImageListActivity.class);
                                intent.putExtra("recipes", new LinkedList<>(recipes));
                                startActivity(intent);
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "An error occurred while making API call!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // f-ja za load CSV ingredients.csv
    private List<String> loadIngredientsFromCSV() {
        List<String> ingredients = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("ingredients.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1);
                }
                ingredients.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}
