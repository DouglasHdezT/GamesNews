package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.debugps.gamesnews.roomTools.POJO.Category;
import com.debugps.gamesnews.roomTools.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;

    private LiveData<List<Category>> categoriesList;
    private LiveData<Double> cantCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        categoryRepository= new CategoryRepository(application);

        categoriesList = categoryRepository.getCategoryList();
        cantCategories = categoryRepository.getCantCategories();
    }

    public LiveData<Double> getCantCategories() {
        return cantCategories;
    }

    public LiveData<List<Category>> getCategoriesList() {
        return categoriesList;
    }

    public void insertCategory(Category category){
        categoryRepository.insertCategory(category);
    }

    public void deleteAllCategories(){
        categoryRepository.deleteAllCategories();
    }
}
