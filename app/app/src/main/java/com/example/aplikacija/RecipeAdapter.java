package com.example.aplikacija;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private LayoutInflater inflater;
    private Context context;


    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        Recipe recipe = getItem(position);


        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewDesc = convertView.findViewById(R.id.textViewDesc);
        ImageView imageView = convertView.findViewById(R.id.ImageViewRecipe);

        textViewTitle.setText(recipe.getTitle());
        textViewDesc.setText(XMLTagRemover.strip(recipe.getSummary()));

        LoadImageTask task = new LoadImageTask(imageView);
        task.execute(recipe.getImageUrl());

        return convertView;
    }


    public static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }


}
