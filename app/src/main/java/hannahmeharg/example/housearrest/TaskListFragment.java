package hannahmeharg.example.housearrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private boolean mSubtitleVisible;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);



        mTaskRecyclerView = (RecyclerView) view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);

        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Task task = new Task();
                TaskSingleton.get(getActivity()).addTask(task);
                Intent intent = TaskPagerActivity.newIntent(getActivity(), task.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // this is where count subtitle is initiated

    private void updateSubtitle() {
        TaskSingleton taskLab = TaskSingleton.get(getActivity());
        int taskCount = taskLab.getTasks().size();
        String subtitle = getString(R.string.subtitle_format, taskCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    private void updateUI() {
        TaskSingleton taskSingletonLab = TaskSingleton.get(getActivity());
        List<Task> tasks = taskSingletonLab.getTasks();


        if (mAdapter == null) {
            mAdapter = new TaskAdapter(tasks);
            mTaskRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();

    }


    // =================================== TASK HOLDER ===================================
    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Task mTask;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.task_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.task_date);
            mSolvedImageView = (ImageView)
                    itemView.findViewById(R.id.task_solved);
        }

        public void bind(Task task) {
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            mDateTextView.setText(mTask.getDate().toString());

            mSolvedImageView.setVisibility(task.isSolved() ?
                    View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {

            Intent intent = TaskPagerActivity.newIntent(getActivity(), mTask.getId());
            startActivity(intent);
        }
    }

    // =================================== TASK ADAPTER ===================================
    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        public TaskAdapter(List<Task> tasks) {

            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {

            return mTasks.size();
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

    }


}
