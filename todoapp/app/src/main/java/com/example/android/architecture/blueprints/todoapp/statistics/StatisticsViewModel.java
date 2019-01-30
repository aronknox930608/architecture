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

import android.app.Application;
import android.content.Context;

import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.data.Task;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository;

import java.util.List;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * Exposes the data to be used in the statistics screen.
 * <p>
 * This ViewModel uses both {@link ObservableField}s ({@link ObservableBoolean}s in this case) and
 * {@link Bindable} getters. The values in {@link ObservableField}s are used directly in the layout,
 * whereas the {@link Bindable} getters allow us to add some logic to it. This is
 * preferable to having logic in the XML layout.
 */
public class StatisticsViewModel extends AndroidViewModel {

    public final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();

    public final MutableLiveData<Boolean> error = new MutableLiveData<>();

    public final MutableLiveData<String> numberOfActiveTasks = new MutableLiveData<>();

    public final MutableLiveData<String> numberOfCompletedTasks = new MutableLiveData<>();

    /**
     * Controls whether the stats are shown or a "No data" message.
     */
    public final ObservableBoolean empty = new ObservableBoolean();

    private int mNumberOfActiveTasks = 0;

    private int mNumberOfCompletedTasks = 0;

    private final Context mContext;

    private final TasksRepository mTasksRepository;

    public StatisticsViewModel(Application context, TasksRepository tasksRepository) {
        super(context);
        mContext = context;
        mTasksRepository = tasksRepository;
    }

    public void start() {
        loadStatistics();
    }

    public void loadStatistics() {
        dataLoading.setValue(true);

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                error.setValue(false);
                computeStats(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                error.setValue(true);
                mNumberOfActiveTasks = 0;
                mNumberOfCompletedTasks = 0;
                updateDataBindingObservables();
            }
        });
    }

    /**
     * Called when new data is ready.
     */
    private void computeStats(List<Task> tasks) {
        int completed = 0;
        int active = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed += 1;
            } else {
                active += 1;
            }
        }
        mNumberOfActiveTasks = active;
        mNumberOfCompletedTasks = completed;

        updateDataBindingObservables();
    }

    private void updateDataBindingObservables() {
        numberOfCompletedTasks.setValue(
                mContext.getString(R.string.statistics_completed_tasks, mNumberOfCompletedTasks));
        numberOfActiveTasks.setValue(
                mContext.getString(R.string.statistics_active_tasks, mNumberOfActiveTasks));
        empty.set(mNumberOfActiveTasks + mNumberOfCompletedTasks == 0);
        dataLoading.setValue(false);

    }
}
