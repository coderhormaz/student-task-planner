package com.hormaz.studentplanner.ui.stats;

import com.hormaz.studentplanner.ui.common.BaseView;

import java.util.Map;

public interface StatsContract {

    interface View extends BaseView {
        void showCompletionRate(int completed, int total);
        void showStreak(int days);
        void showWeeklyData(int[] dailyCounts);
        void showCategoryBreakdown(Map<String, Integer> breakdown);
        void showMotivationalMessage(String message);
    }

    interface Presenter {
        void loadStats();
        void onDestroy();
    }
}
