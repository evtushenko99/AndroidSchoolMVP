package com.example.androidschoolmvp.presenter;

import com.example.androidschoolmvp.R;
import com.example.androidschoolmvp.data.dataprovider.LearningProgramProvider;
import com.example.androidschoolmvp.data.model.DisplayMode;
import com.example.androidschoolmvp.data.model.Lecture;
import com.example.androidschoolmvp.view.fragments.ILectureFragmentView;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;


public class SchoolCoursesPresenter {
    private WeakReference<ILectureFragmentView> mLecturesFragmentReference;
    private LearningProgramProvider mLearningProgramProvider;

    public SchoolCoursesPresenter(ILectureFragmentView lecturesFragmentReference,
                                  LearningProgramProvider provider) {
        mLecturesFragmentReference = new WeakReference<>(lecturesFragmentReference);
        mLearningProgramProvider = provider;
    }

    public List<Lecture> getLecturesFromProvider() {
        return mLearningProgramProvider.provideLectures();
    }

    public Lecture getLectureFromProviderNextTo() {
        return mLearningProgramProvider.getLectureNextTo(mLearningProgramProvider.provideLectures(), new Date());
    }

    public List<Lecture> getFilteredLectures(String lectorName) {
        return mLearningProgramProvider.filterBy(lectorName);
    }

    public DisplayMode getDisplayMode(int position) {
        return DisplayMode.values()[position];
    }

    public void startLectureFragmentInitialization(boolean isItFirstCreation) {
        ILectureFragmentView fragment = mLecturesFragmentReference.get();
        List<Lecture> lectures = mLearningProgramProvider.provideLectures();

        LearningProgramProvider.OnLoadingFinishListener onLoadingFinishListener = loadLectures -> {
            ILectureFragmentView view = mLecturesFragmentReference.get();

            if (view != null) {
                if (loadLectures == null) {
                    view.showToast(R.string.failed_to_load_lectures);
                } else {
                    List<String> lectors = mLearningProgramProvider.providerLectors();
                    view.initRecyclerView(isItFirstCreation, loadLectures);
                    view.initDisplayModeSpinner();
                    view.initLectorsSpinner(lectors, loadLectures);
                }
            }
        };

        if (lectures == null) {
            mLearningProgramProvider.loadDataAsync(onLoadingFinishListener);
        } else {
            initRecyclesAndSpinners(isItFirstCreation, fragment, lectures, mLearningProgramProvider.providerLectors());
        }

    }

    private void initRecyclesAndSpinners(boolean isItFirstCreation, ILectureFragmentView fragment, List<Lecture> lectures, List<String> lectors) {
        fragment.initRecyclerView(isItFirstCreation, lectures);
        fragment.initDisplayModeSpinner();
        fragment.initLectorsSpinner(lectors, lectures);
    }
}
