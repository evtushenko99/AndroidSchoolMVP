package com.example.androidschoolmvp.presenter;

import com.example.androidschoolmvp.data.dataprovider.LearningProgramProvider;
import com.example.androidschoolmvp.data.dataprovider.LearningProgramProvider.OnLoadingFinishListener;
import com.example.androidschoolmvp.data.model.Lecture;
import com.example.androidschoolmvp.view.fragments.ILectureFragmentView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchoolCoursesPresenterTest {
    @Mock
    private ILectureFragmentView mLectureFragmentView;

    @Mock
    private LearningProgramProvider mProvider;


    private SchoolCoursesPresenter mPresenter;

    @Before
    public void setUp() {
        mPresenter = new SchoolCoursesPresenter(mLectureFragmentView, mProvider);
    }

    @Test
    public void testLoadDataAsync() {
        when(mProvider.provideLectures()).thenReturn(null);
        when(mProvider.providerLectors()).thenReturn(createTestLectors());
        List<Lecture> expectedLectures = createTestLectures();
        List<String> expectedLectors = createTestLectors();
        doAnswer(invocation -> {
            //получаем слушателя из метода loadDataAsync().
            OnLoadingFinishListener onLoadingFinishListener =
                    (OnLoadingFinishListener) invocation.getArguments()[0];
            //кидаем в него ответы
            onLoadingFinishListener.onFinish(expectedLectures);

            return null;
        }).when(mProvider).loadDataAsync(any(OnLoadingFinishListener.class));

        mPresenter.startLectureFragmentInitialization(true);

        //Проверка на вызов требуемых методов презентором
        verify(mLectureFragmentView).initRecyclerView(true, expectedLectures);
        verify(mLectureFragmentView).initDisplayModeSpinner();
        verify(mLectureFragmentView).initLectorsSpinner(expectedLectors, expectedLectures);
    }

    @Test
    public void restLoadDataAsyncInOrder(){
        when(mProvider.provideLectures()).thenReturn(null);
        when(mProvider.providerLectors()).thenReturn(createTestLectors());
        List<Lecture> expectedLectures = createTestLectures();
        List<String> expectedLectors = createTestLectors();
        doAnswer(invocation -> {
            //получаем слушателя из метода loadDataAsync().
            OnLoadingFinishListener onLoadingFinishListener =
                    (OnLoadingFinishListener) invocation.getArguments()[0];
            //кидаем в него ответы
            onLoadingFinishListener.onFinish(expectedLectures);

            return null;
        }).when(mProvider).loadDataAsync(any(OnLoadingFinishListener.class));

        mPresenter.startLectureFragmentInitialization(true);

        InOrder inOrder = Mockito.inOrder(mLectureFragmentView);

        inOrder.verify(mLectureFragmentView).initRecyclerView(true, expectedLectures);
        inOrder.verify(mLectureFragmentView).initDisplayModeSpinner();
        inOrder.verify(mLectureFragmentView).initLectorsSpinner(expectedLectors, expectedLectures);
    }

    private List<String> createTestLectors() {
        List<String> lectors = new ArrayList<>();
        lectors.add("Чумак");
        return lectors;
    }

    private List<Lecture> createTestLectures() {
        List<String> subTopics = new ArrayList<>();
        subTopics.add("Без тестов не выжить в этом мире");
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(1, "16.01.2020", "Тесты", "Чумак", subTopics));
        return lectures;
    }
}