package com.hormaz.studentplanner.ui.calendar;

import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.ui.common.BaseView;

import java.util.List;

public interface CalendarContract {

    interface View extends BaseView {
        void showTasksForDate(List<Task> tasks, String dateLabel);
        void highlightDates(List<String> dates);
        void showEmptyDate(String dateLabel);
    }

    interface Presenter {
        void loadTasksForDate(String date);
        void loadMonthHighlights(String yearMonth);
        void onDestroy();
    }
}
