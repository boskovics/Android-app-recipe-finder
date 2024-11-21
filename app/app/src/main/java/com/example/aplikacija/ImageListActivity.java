package com.example.aplikacija;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {

    private ListView listViewRecipes;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        listViewRecipes = findViewById(R.id.listViewRecipes);

        ArrayList<Recipe> recipes = (ArrayList<Recipe>) getIntent().getSerializableExtra("recipes");

        if (recipes.isEmpty()) {
            Toast.makeText(this, "No recipes available for the selected ingredients!", Toast.LENGTH_SHORT).show();
        } else {
            recipeAdapter = new RecipeAdapter(this, recipes);
            listViewRecipes.setAdapter(recipeAdapter);

            listViewRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                    Recipe recipe = recipes.get(position);
                    Intent intent = new Intent(ImageListActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("recipe", recipe);
                    startActivity(intent);
                }
            });
        }
    }
}
