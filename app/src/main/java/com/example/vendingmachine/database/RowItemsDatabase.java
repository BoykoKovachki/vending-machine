package com.example.vendingmachine.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.vendingmachine.R;
import com.example.vendingmachine.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@Database(entities = {RowItems.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RowItemsDatabase extends RoomDatabase {

    private static RowItemsDatabase instance;

    private static Context activity;

    public abstract RowItemsDao rowItemsDao();

    public static synchronized RowItemsDatabase getInstance(Context context) {
        activity = context.getApplicationContext();

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RowItemsDatabase.class, "items_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallBack = new Callback() { // TODO: check if final its ok
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataBaseAsyncTask(instance).execute();
        }
    };

    private static class PopulateDataBaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private RowItemsDao rowItemsDao;

        private PopulateDataBaseAsyncTask(RowItemsDatabase db) {
            rowItemsDao = db.rowItemsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            populateDataBaseFromJson(activity);
            return null;
        }
    }

    private static void populateDataBaseFromJson(Context context) {
        RowItemsDao rowItemsDao = getInstance(context).rowItemsDao();
        JSONArray items = loadDataBaseFromJSON(context);

        try {
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    int count = item.getInt("count");
                    String name = item.getJSONObject("product").getString("name");
                    double price = item.getJSONObject("product").getDouble("price");
                    int type = item.getJSONObject("product").getInt("type");

                    rowItemsDao.insert(new RowItems(new ProductModel(name, price, type), count));
                }
            } else {
                Log.e("Error: ", "Empty Database!");
            }
        } catch (JSONException e) {
            Log.e("Error: Database populate -", Objects.requireNonNull(e.getMessage()));
        }
    }

    private static JSONArray loadDataBaseFromJSON(Context context) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(R.raw.inventory_db);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject object = new JSONObject(builder.toString());
            return object.getJSONArray("items");

        } catch (JSONException | IOException e) {
            Log.e("Error: Database file - ", Objects.requireNonNull(e.getMessage()));
        }
        return null;
    }

}
