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

package com.example.android.architecture.blueprints.todomvp.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.architecture.blueprints.todomvp.Injection;
import com.example.android.architecture.blueprints.todomvp.R;
import com.example.android.architecture.blueprints.todomvp.addedittask.AddEditTaskActivity;
import com.example.android.architecture.blueprints.todomvp.data.Task;
import com.example.android.architecture.blueprints.todomvp.taskdetail.TaskDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Task}s. User can choose to view all, active or completed tasks.
 */
public class TasksFragment extends Fragment implements TasksContract.View {

    /**
     * This value corresponds to the position in the array shown in the navigation spinner.
     */
    final static int ALL_TASKS = 0;

    /**
     * This value corresponds to the position in the array shown in the navigation spinner.
     */
    final static int ACTIVE_TASKS = 1;

    /**
     * This value corresponds to the position in the array shown in the navigation spinner.
     */
    final static int COMPLETED_TASKS = 2;

    /**
     * Must be one of the following: {@link #ALL_TASKS}, {@link #ACTIVE_TASKS},
     * {@link #COMPLETED_TASKS}.
     */
    private int mCurrentFiltering;

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private static final int REQUEST_ADD_TASK = 1;

    private TasksContract.UserActionsListener mActionsListener;

    private TasksAdapter mListAdapter;

    private ListView mListView;

    private LinearLayout mNoTasksView;

    private ImageView mNoTaskIcon;

    private TextView mNoTaskMainView;

    private TextView mNoTaskAddView;

    private LinearLayout mTasksView;

    private TextView mFilteringLabelView;

    private boolean mFirstLoad;

    public TasksFragment() {
        // Requires empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TasksAdapter(new ArrayList<Task>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        // To minimize number of calls, only force an update if this is the first load. Don't
        // force update if coming from another screen.
        loadTasks(mFirstLoad);
        mFirstLoad = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FILTERING_KEY, mCurrentFiltering);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
        mActionsListener = new TasksPresenter(Injection.provideTasksRepository(getContext()), this);

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_FILTERING_KEY)) {
            mCurrentFiltering = savedInstanceState.getInt(CURRENT_FILTERING_KEY);

        } else {
            mActionsListener.loadAllTasks(false);
        }
        mFirstLoad = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If a task was successfully added, show snackbar
        if (REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            Snackbar.make(getView(), getString(R.string.successfully_saved_task_message),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tasks_frag, container, false);

        // Set up tasks view
        mListView = (ListView) root.findViewById(R.id.tasks_list);
        mListView.setAdapter(mListAdapter);
        mFilteringLabelView = (TextView) root.findViewById(R.id.filteringLabel);
        mTasksView = (LinearLayout) root.findViewById(R.id.tasksLL);

        // Set up  no tasks view
        mNoTasksView = (LinearLayout) root.findViewById(R.id.noTasks);
        mNoTaskIcon = (ImageView) root.findViewById(R.id.noTasksIcon);
        mNoTaskMainView = (TextView) root.findViewById(R.id.noTasksMain);
        mNoTaskAddView = (TextView) root.findViewById(R.id.noTasksAdd);
        mNoTaskAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTask();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.addNewTask();
            }
        });

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTasks(true);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mActionsListener.clearCompletedTasks();
                return true;
            case R.id.menu_filter:
                showFilteringPopUpMenu(getActivity().findViewById(R.id.menu_filter));
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showFilteringPopUpMenu(View viewToAttachPopUpMenu) {
        PopupMenu popup = new PopupMenu(getContext(), viewToAttachPopUpMenu);
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int previousFilter = mCurrentFiltering;
                switch (item.getItemId()) {
                    case R.id.all:
                        mCurrentFiltering = ALL_TASKS;
                        break;
                    case R.id.active:
                        mCurrentFiltering = ACTIVE_TASKS;
                        break;
                    case R.id.completed:
                        mCurrentFiltering = COMPLETED_TASKS;
                        break;
                }
                if (mCurrentFiltering != previousFilter) {
                    loadTasks(false);
                }
                return true;
            }
        });

        popup.show();
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    TaskItemListener mItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mActionsListener.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mActionsListener.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mActionsListener.activateTask(activatedTask);
        }
    };

    @Override
    public void setProgressIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    public void showTasks(List<Task> tasks) {
        mListAdapter.replaceData(tasks);
        if (tasks.size() == 0) {
            mTasksView.setVisibility(View.GONE);
            mNoTasksView.setVisibility(View.VISIBLE);
            showNoTasks();
        } else {
            mTasksView.setVisibility(View.VISIBLE);
            mNoTasksView.setVisibility(View.GONE);
            showFilterLabel();
        }
    }

    private void showNoTasks() {
        String mainText = getResources().getString(R.string.no_tasks_all);
        int iconRes = R.drawable.ic_assignment_turned_in_24dp;
        boolean showAddView = true;
        if (mCurrentFiltering == ACTIVE_TASKS) {
            mainText = getResources().getString(R.string.no_tasks_active);
            iconRes = R.drawable.ic_check_circle_24dp;
            showAddView = false;
        } else if (mCurrentFiltering == COMPLETED_TASKS) {
            mainText = getResources().getString(R.string.no_tasks_completed);
            iconRes = R.drawable.ic_verified_user_24dp;
            showAddView = false;
        }
        mNoTaskMainView.setText(mainText);
        mNoTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoTaskAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    private void showFilterLabel() {
        String label = getResources().getString(R.string.label_all);
        if (mCurrentFiltering == ACTIVE_TASKS) {
            label = getResources().getString(R.string.label_active);
        } else if (mCurrentFiltering == COMPLETED_TASKS) {
            label = getResources().getString(R.string.label_completed);
        }
        mFilteringLabelView.setText(label);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show();
        loadTasks(false);
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show();
        loadTasks(false);
    }

    @Override
    public void showCompletedTasksCleared() {
        Snackbar.make(getView(), getString(R.string.completed_tasks_cleared), Snackbar.LENGTH_LONG)
                .show();
        loadTasks(false);
    }

    @Override
    public void showLoadingTasksError() {
        Snackbar.make(getView(), getString(R.string.loading_tasks_error), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean isInactive() {
        return !isAdded();
    }

    private void loadTasks(boolean forceUpdate) {
        switch (mCurrentFiltering) {
            case ALL_TASKS:
                mActionsListener.loadAllTasks(forceUpdate);
                break;
            case ACTIVE_TASKS:
                mActionsListener.loadActiveTasks(forceUpdate);
                break;
            case COMPLETED_TASKS:
                mActionsListener.loadCompletedTasks(forceUpdate);
                break;
            default:
                mActionsListener.loadAllTasks(forceUpdate);
                break;
        }
    }

    private static class TasksAdapter extends BaseAdapter {

        private List<Task> mTasks;
        private TaskItemListener mItemListener;

        public TasksAdapter(List<Task> tasks, TaskItemListener itemListener) {
            setList(tasks);
            mItemListener = itemListener;
        }

        public void replaceData(List<Task> tasks) {
            setList(tasks);
            notifyDataSetChanged();
        }

        private void setList(List<Task> tasks) {
            mTasks = checkNotNull(tasks);
        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Task getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.task_item, viewGroup, false);
            }

            final Task task = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.title);
            titleTV.setText(task.getTitleForList());

            CheckBox completeCB = (CheckBox) rowView.findViewById(R.id.complete);

            // Active/completed task UI
            completeCB.setChecked(task.isCompleted());
            if (task.isCompleted()) {
                rowView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
            } else {
                rowView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.touch_feedback));
            }

            completeCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!task.isCompleted()) {
                        mItemListener.onCompleteTaskClick(task);
                    } else {
                        mItemListener.onActivateTaskClick(task);
                    }
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onTaskClick(task);
                }
            });

            return rowView;
        }
    }

    public interface TaskItemListener {

        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);
    }

}
