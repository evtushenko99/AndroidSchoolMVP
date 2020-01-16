package com.example.androidschoolmvp.view.adapters;

import androidx.annotation.NonNull;

import com.example.androidschoolmvp.data.model.Lecture;

public interface OnLectureClickListener {
    void onItemClick(@NonNull Lecture lecture);
}
