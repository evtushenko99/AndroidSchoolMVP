package com.example.androidschoolmvp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolmvp.R;
import com.example.androidschoolmvp.data.dataprovider.LearningProgramProvider;
import com.example.androidschoolmvp.data.model.Lecture;
import com.example.androidschoolmvp.presenter.SchoolCoursesPresenter;
import com.example.androidschoolmvp.view.adapters.DisplayModeSpinnerAdapter;
import com.example.androidschoolmvp.view.adapters.LectorSpinnerAdapter;
import com.example.androidschoolmvp.view.adapters.LecturesAdapter;
import com.example.androidschoolmvp.view.adapters.OnLectureClickListener;

import java.util.Collections;
import java.util.List;

public class LecturesFragment extends Fragment implements ILectureFragmentView {
    private static final int POSITION_ALL = 0;
    private SchoolCoursesPresenter mPresenter;

    private LecturesAdapter mLecturesAdapter;

    private View mLoadingView;
    private RecyclerView mRecyclerView;
    private Spinner mLectorsSpinner;
    private Spinner mDisplayModeSpinner;
    private OnLectureClickListener mOnLectureClickListener = new OnLectureClickListener() {
        @Override
        public void onItemClick(@NonNull Lecture lecture) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_my, DetailsFragment.newInstance(lecture))
                    .addToBackStack(DetailsFragment.class.getSimpleName())
                    .commit();
        }
    };

    public static Fragment newInstance() {
        return new LecturesFragment();
    }

    {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lectures_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingView = view.findViewById(R.id.loading_view);
        mRecyclerView = view.findViewById(R.id.learning_program_recycler);
        mLectorsSpinner = view.findViewById(R.id.lectors_spinner);
        mDisplayModeSpinner = view.findViewById(R.id.display_mode_spinner);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // пока так. Потом брать из специального места (компонента даггера)
        LearningProgramProvider provider = new LearningProgramProvider();
        mPresenter = new SchoolCoursesPresenter(this, provider);
        mPresenter.startLectureFragmentInitialization(savedInstanceState == null);
    }

    @Override
    public void showToast(int message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initRecyclerView(boolean isFirstCreate, @NonNull List<Lecture> lectures) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mLecturesAdapter = new LecturesAdapter(getResources());
        mLecturesAdapter.setLectures(lectures);
        mLecturesAdapter.setClickListener(mOnLectureClickListener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mLecturesAdapter);
        if (isFirstCreate) {
            Lecture nextLecture = mPresenter.getLectureFromProviderNextTo();
            int positionOfNextLecture = mLecturesAdapter.getPositionOf(nextLecture);
            if (positionOfNextLecture != -1) {
                mRecyclerView.scrollToPosition(positionOfNextLecture);
            }
        }
    }

    @Override
    public void initLectorsSpinner(@NonNull List<String> lectors, @NonNull List<Lecture> lectures) {
        final List<String> spinnerItems = lectors;
        Collections.sort(spinnerItems);
        spinnerItems.add(POSITION_ALL, getResources().getString(R.string.all));
        LectorSpinnerAdapter adapter = new LectorSpinnerAdapter(spinnerItems);
        mLectorsSpinner.setAdapter(adapter);

        mLectorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final List<Lecture> lectures = position == POSITION_ALL ?
                        mPresenter.getLecturesFromProvider() :
                        mPresenter.getFilteredLectures(spinnerItems.get(position));
                mLecturesAdapter.setLectures(lectures);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void initDisplayModeSpinner() {

        mDisplayModeSpinner.setAdapter(new DisplayModeSpinnerAdapter());
        mDisplayModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLecturesAdapter.setDisplayMode(mPresenter.getDisplayMode(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}
