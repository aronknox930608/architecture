/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.android.architecture.blueprints.todoapp.data.Task;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksLoader;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link StatisticsFragment}), retrieves the data and updates
 * the UI as required.
 */
public class StatisticsPresenter implements StatisticsContract.Presenter, LoaderManager.LoaderCallbacks<List<Task>> {

    private static final int TASK_QUERY = 3;

    private StatisticsContract.View mStatisticsView;

    private TasksLoader mTasksLoader;

    private LoaderManager mLoaderManager;

    public StatisticsPresenter(@NonNull StatisticsContract.View statisticsView,
                               @NonNull TasksLoader tasksLoader,
                               @NonNull LoaderManager loaderManager) {
        mStatisticsView = checkNotNull(statisticsView, "StatisticsView cannot be null!");
        mTasksLoader = checkNotNull(tasksLoader, "tasksLoader cannot be null!");
        mLoaderManager = checkNotNull(loaderManager, "loaderManager cannot be null!");

        mStatisticsView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(TASK_QUERY, null, this);
    }

    @Override
    public Loader<List<Task>> onCreateLoader(int id, Bundle args) {
        mStatisticsView.setProgressIndicator(true);
        return mTasksLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Task>> loader, List<Task> data) {
        loadStatistics(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Task>> loader) {

    }

    private void loadStatistics(List<Task> tasks) {
        if (tasks == null) {
            mStatisticsView.showLoadingStatisticsError();
        } else {
            int activeTasks = 0;
            int completedTasks = 0;

            // Calculate number of active and completed tasks
            for (Task task : tasks) {
                if (task.isCompleted()) {
                    completedTasks += 1;
                } else {
                    activeTasks += 1;
                }
            }

            mStatisticsView.setProgressIndicator(false);

            mStatisticsView.showStatistics(activeTasks, completedTasks);
        }
    }

}
