package com.example.vendingmachine.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RowItemsViewModel extends AndroidViewModel {

    private RowItemsRepository repository;

    private LiveData<List<RowItems>> items;

    public RowItemsViewModel(@NonNull Application application) {
        super(application);
        repository = new RowItemsRepository(application);
        items = repository.getAllItems();
    }

    public LiveData<List<RowItems>> getAllItems() {
        return items;
    }

    public void insert(RowItems items) {
        repository.insert(items);
    }

    public void update(RowItems items) {
        repository.update(items);
    }

    public void delete(RowItems items) {
        repository.delete(items);
    }

}
