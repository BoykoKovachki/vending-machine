package com.example.vendingmachine.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RowItemsRepository {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private RowItemsDao rowItemsDao;

    private LiveData<List<RowItems>> items;

    public RowItemsRepository(Application application) {
        RowItemsDatabase database = RowItemsDatabase.getInstance(application);
        rowItemsDao = database.rowItemsDao();
        items = rowItemsDao.getAllItems();
    }

    public LiveData<List<RowItems>> getAllItems() {
        return items;
    }

    public void insert(RowItems items) {
        new InsertRowItemsAsyncTask(rowItemsDao).execute(items);
    }

    public void update(RowItems items) {
        new UpdateRowItemsAsyncTask(rowItemsDao).execute(items);
    }

    public void delete(RowItems items) { // different way to do the task
        executor.execute(new Runnable() {
            @Override
            public void run() {
                rowItemsDao.delete(items);
            }
        });
    }


    private static class InsertRowItemsAsyncTask extends AsyncTask<RowItems, Void, Void> {
        private RowItemsDao rowItemsDao;

        private InsertRowItemsAsyncTask(RowItemsDao rowItemsDao) {
            this.rowItemsDao = rowItemsDao;
        }

        @Override
        protected Void doInBackground(RowItems... items) {
            rowItemsDao.insert(items[0]);
            return null;
        }
    }

    private static class UpdateRowItemsAsyncTask extends AsyncTask<RowItems, Void, Void> {
        private RowItemsDao rowItemsDao;

        private UpdateRowItemsAsyncTask(RowItemsDao rowItemsDao) {
            this.rowItemsDao = rowItemsDao;
        }

        @Override
        protected Void doInBackground(RowItems... items) {
            rowItemsDao.update(items[0]);
            return null;
        }
    }

}
