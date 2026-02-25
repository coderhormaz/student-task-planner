package com.hormaz.studentplanner.ui.calendar;

import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.data.repository.TaskRepository;
import com.hormaz.studentplanner.ui.common.BasePresenter;
import com.hormaz.studentplanner.util.DateUtils;

import java.time.YearMonth;
import java.util.List;

public class CalendarPresenter extends BasePresenter<CalendarContract.View>
        implements CalendarContract.Presenter {

    private final TaskRepository taskRepository;

    public CalendarPresenter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void loadTasksForDate(String date) {
        if (!isViewAttached()) return;
        getView().showLoading();

        taskRepository.getTasksByDate(date, new TaskRepository.TaskCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                if (!isViewAttached()) return;
                getView().hideLoading();

                String dateLabel = DateUtils.getRelativeDate(date);
                if (result.isEmpty()) {
                    getView().showEmptyDate(dateLabel);
                } else {
                    getView().showTasksForDate(result, dateLabel);
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showError(message);
                }
            }
        });
    }

    @Override
    public void loadMonthHighlights(String yearMonth) {
        // yearMonth format: "yyyy-MM"
        try {
            YearMonth ym = YearMonth.parse(yearMonth);
            String startDate = ym.atDay(1).toString();
            String endDate = ym.atEndOfMonth().toString();

            taskRepository.getTaskDatesInRange(startDate, endDate,
                    new TaskRepository.TaskCallback<List<String>>() {
                        @Override
                        public void onSuccess(List<String> result) {
                            if (isViewAttached()) {
                                getView().highlightDates(result);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            // Silent fail
                        }
                    });
        } catch (Exception ignored) {}
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
