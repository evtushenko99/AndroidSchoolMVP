package com.example.androidschoolmvp.view.fragments;

import androidx.annotation.NonNull;

import com.example.androidschoolmvp.data.model.Lecture;

import java.util.List;

public interface ILectureFragmentView {

    void initRecyclerView(boolean isFirstCreate, @NonNull List<Lecture> lectures);
    void initLectorsSpinner(@NonNull List<String> lectors, @NonNull List<Lecture> lectures);
    void initDisplayModeSpinner();
    void showToast(int message);


}
