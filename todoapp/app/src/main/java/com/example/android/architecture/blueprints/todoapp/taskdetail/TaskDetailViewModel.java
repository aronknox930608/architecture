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

package com.example.android.architecture.blueprints.todoapp.taskdetail;

import android.content.Context;

import com.example.android.architecture.blueprints.todoapp.TaskViewModel;
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository;
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFragment;

import java.lang.ref.WeakReference;


/**
 * Listens to user actions from the list item in ({@link TasksFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class TaskDetailViewModel extends TaskViewModel {

    // Navigator has references to Activity so it must be wrapped to prevent leaks.
    private final WeakReference<TaskDetailNavigator> mTaskDetailNavigator;

    public TaskDetailViewModel(Context context, TasksRepository tasksRepository,
                               TaskDetailNavigator taskDetailNavigator) {
        super(context, tasksRepository);
        mTaskDetailNavigator = new WeakReference<>(taskDetailNavigator);
    }

    /**
     * Can be called by the Data Binding Library or the delete menu item.
     */
    public void deleteTask() {
        super.deleteTask();
        if (mTaskDetailNavigator.get() != null) {
            mTaskDetailNavigator.get().onTaskDeleted();
        }
    }

    public void startEditTask() {
        if (mTaskDetailNavigator.get() != null) {
            mTaskDetailNavigator.get().onStartEditTask();
        }
    }
}
