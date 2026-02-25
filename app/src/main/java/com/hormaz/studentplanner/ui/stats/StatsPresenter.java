package com.hormaz.studentplanner.ui.stats;

import com.hormaz.studentplanner.data.local.prefs.PreferencesManager;
import com.hormaz.studentplanner.data.repository.TaskRepository;
import com.hormaz.studentplanner.ui.common.BasePresenter;
import com.hormaz.studentplanner.util.DateUtils;

import java.time.LocalDate;

public class StatsPresenter extends BasePresenter<StatsContract.View>
        implements StatsContract.Presenter {

    private final TaskRepository taskRepository;
    private final PreferencesManager prefs;

    public StatsPresenter(TaskRepository taskRepository, PreferencesManager prefs) {
        this.taskRepository = taskRepository;
        this.prefs = prefs;
    }

    @Override
    public void loadStats() {
        if (!isViewAttached()) return;
        getView().showLoading();

        // Completion rate
        taskRepository.getCompletionStats(new TaskRepository.TaskCallback<int[]>() {
            @Override
            public void onSuccess(int[] result) {
                if (!isViewAttached()) return;
                getView().hideLoading();
                int completed = result[0];
                int total = result[1];
                getView().showCompletionRate(completed, total);

                // Motivational message
                if (total == 0) {
                    getView().showMotivationalMessage("Add your first task to get started!");
                } else {
                    float rate = (float) completed / total;
                    if (rate >= 0.8f) {
                        getView().showMotivationalMessage("Amazing work! Stay consistent! 🔥");
                    } else if (rate >= 0.5f) {
                        getView().showMotivationalMessage("Almost there! Push a little more! 💪");
                    } else {
                        getView().showMotivationalMessage("Keep going! You're doing great! ✨");
                    }
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

        // Streak
        getView().showStreak(prefs.getStreakCount());

        // Weekly data - last 7 days
        loadWeeklyData();
    }

    private void loadWeeklyData() {
        final int[] dailyCounts = new int[7];
        final int[] loaded = {0};

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = DateUtils.toISO(date);
            final int index = 6 - i;

            taskRepository.getCompletedCountByDate(dateStr,
                    new TaskRepository.TaskCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer result) {
                            dailyCounts[index] = result;
                            loaded[0]++;
                            if (loaded[0] == 7 && isViewAttached()) {
                                getView().showWeeklyData(dailyCounts);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            loaded[0]++;
                            if (loaded[0] == 7 && isViewAttached()) {
                                getView().showWeeklyData(dailyCounts);
                            }
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
