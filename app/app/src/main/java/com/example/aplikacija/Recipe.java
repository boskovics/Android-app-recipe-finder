package com.example.aplikacija;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    private String title;
    private String summary;
    private String imageUrl;
    private List<String> steps;

    public Recipe() {
    }

    public Recipe(String title, String summary, String imageUrl, List<String> steps) {
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.steps = steps != null ? steps : new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public static Recipe parseJSONObject(JSONObject object) {
        Recipe recipe = new Recipe();

        try {

            if (object.has("title")) {
                recipe.setTitle(object.getString("title"));
            }
            if (object.has("summary")) {
                recipe.setSummary(object.getString("summary"));
            }
            if (object.has("image")) {
                recipe.setImageUrl(object.getString("image"));
            }


            if (object.has("analyzedInstructions")) {
                JSONArray instructionsArray = object.getJSONArray("analyzedInstructions");
                if (instructionsArray.length() > 0) {
                    JSONObject instructionObject = instructionsArray.getJSONObject(0);

                    if (instructionObject.has("steps")) {
                        JSONArray stepsArray = instructionObject.getJSONArray("steps");
                        List<String> stepTexts = new ArrayList<>();

                        for (int i = 0; i < stepsArray.length(); i++) {
                            JSONObject stepObject = stepsArray.getJSONObject(i);
                            if (stepObject.has("step")) {
                                stepTexts.add(stepObject.getString("step"));
                            }
                        }

                        recipe.setSteps(stepTexts);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipe;
    }

    public static ArrayList<Recipe> parseJSONArray(JSONArray array) {
        ArrayList<Recipe> list = new ArrayList<>();

        try {

            for (int i = 0; i < array.length(); i++) {
                Recipe recipe = parseJSONObject(array.getJSONObject(i));
                list.add(recipe);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
