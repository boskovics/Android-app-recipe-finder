package com.example.aplikacija;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView recipeImageView;
    private TextView recipeNameTextView;
    private TextView recipeDescriptionTextView;
    private LinearLayout stepsLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        recipeImageView = findViewById(R.id.recipeImageView);
        recipeNameTextView = findViewById(R.id.recipeNameTextView);
        recipeDescriptionTextView = findViewById(R.id.recipeDescriptionTextView);
        stepsLinearLayout = findViewById(R.id.stepsLinearLayout);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        if (recipe != null) {

            recipeNameTextView.setText(recipe.getTitle());
            recipeDescriptionTextView.setText((XMLTagRemover.strip(recipe.getSummary())));

            List<String> stepTexts = recipe.getSteps();

            if (stepTexts != null) {
                LinearLayout stepsLinearLayout = findViewById(R.id.stepsLinearLayout);

                for (int i = 0; i < stepTexts.size(); i++) {
                    String stepText = stepTexts.get(i);
                    String labeledStepText = "Step " + (i + 1) + ": " + stepText;

                    TextView stepTextView = new TextView(this);
                    stepTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    stepTextView.setText(labeledStepText);
                    stepTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    stepTextView.setPadding(10, 10, 10, 10);

                    stepsLinearLayout.addView(stepTextView);
                }
            }

            RecipeAdapter.LoadImageTask task = new RecipeAdapter.LoadImageTask(recipeImageView);
            task.execute(recipe.getImageUrl());
        } else {
            Toast.makeText(this, "Recipe couldn't be loaded", Toast.LENGTH_SHORT).show();
        }
    }
}

