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

package com.example.android.architecture.blueprints.todomvploaders.tasks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.android.architecture.blueprints.todomvploaders.data.Task;
import com.example.android.architecture.blueprints.todomvploaders.data.source.TasksDataSource;
import com.example.android.architecture.blueprints.todomvploaders.data.source.TasksLoader;
import com.example.android.architecture.blueprints.todomvploaders.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.architecture.blueprints.todomvploaders.tasks.TasksFragment.ACTIVE_TASKS;
import static com.example.android.architecture.blueprints.todomvploaders.tasks.TasksFragment.ALL_TASKS;
import static com.example.android.architecture.blueprints.todomvploaders.tasks.TasksFragment.COMPLETED_TASKS;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Listens to user actions from the UI ({@link TasksFragment}), retrieves the data and updates the
 * UI as required. It is implemented as a non UI {@link Fragment} to make use of the
 * {@link LoaderManager} mechanism for managing loading and updating data asynchronously.
 */
public class TasksPresenter implements TasksContract.UserActionsListener,
        LoaderManager.LoaderCallbacks<List<Task>> {

    private final static int TASKS_QUERY = 1;

    private final TasksRepository mTasksRepository;

    private final TasksContract.View mTasksView;

    private final TasksLoader mLoader;

    private List<Task> mCurrentTasks;

    protected int mFilterType;

    public TasksPresenter(@NonNull TasksLoader loader, @NonNull TasksRepository tasksRepository,
                          @NonNull TasksContract.View tasksView) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");
        mLoader = checkNotNull(loader, "loader cannot be null!");
    }

    /**
     * This starts the {@link LoaderManager}, querying the list of tasks. It returns the
     * TasksPresenter so it can be chained with the constructor. This isn't called from the
     * constructor to enable writing unit tests for the non loader methods in the TasksPresenter
     * (creating an instance from a unit test would fail if this method were called from it).
     */
    public TasksPresenter startLoader(TasksFragment fragment) {
        mFilterType = fragment.getCurrentFiltering();
        fragment.getLoaderManager().initLoader(TASKS_QUERY, null, this);
        return this;
    }

    @Override
    public void loadAllTasks(boolean forceUpdate) {
        mFilterType = ALL_TASKS;
        loadTasks(forceUpdate);
    }

    @Override
    public void loadActiveTasks(boolean forceUpdate) {
        mFilterType = ACTIVE_TASKS;
        loadTasks(forceUpdate);
    }

    @Override
    public void loadCompletedTasks(boolean forceUpdate) {
        mFilterType = COMPLETED_TASKS;
        loadTasks(forceUpdate);
    }

    /**
     * @param forceUpdate Pass in true to refresh the data in the {@link TasksDataSource}
     */
    private void loadTasks(boolean forceUpdate) {
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        } else {
            showFilteredTasks();
        }
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask, "completedTask cannot be null!");
        mTasksRepository.completeTask(completedTask);
        mTasksView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");
        mTasksRepository.activateTask(activeTask);
        mTasksView.showTaskMarkedActive();
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRepository.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
    }


    @Override
    public Loader<List<Task>> onCreateLoader(int id, Bundle args) {
        mTasksView.setProgressIndicator(true);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Task>> loader, List<Task> data) {
        mTasksView.setProgressIndicator(false);

        mCurrentTasks = data;
        if (mCurrentTasks == null) {
            mTasksView.showLoadingTasksError();
        } else {
            showFilteredTasks();
        }
    }

    private void showFilteredTasks() {
        List<Task> tasksToDisplay = new ArrayList<Task>();
        if (mCurrentTasks != null) {
            for (Task task : mCurrentTasks) {
                switch (mFilterType) {
                    case ALL_TASKS:
                        tasksToDisplay.add(task);
                        break;
                    case ACTIVE_TASKS:
                        if (task.isActive()) {
                            tasksToDisplay.add(task);
                        }
                        break;
                    case COMPLETED_TASKS:
                        if (task.isCompleted()) {
                            tasksToDisplay.add(task);
                        }
                        break;
                    default:
                        tasksToDisplay.add(task);
                        break;
                }
            }
        }

        mTasksView.showTasks(tasksToDisplay);
    }


    @Override
    public void onLoaderReset(Loader<List<Task>> loader) {

    }
}
