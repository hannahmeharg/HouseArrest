package hannahmeharg.example.housearrest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskSingleton {
    private static TaskSingleton sTaskSingleton;
    //private List<Task> mTasks;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static TaskSingleton get(Context context) {
        if (sTaskSingleton == null) {
            sTaskSingleton = new TaskSingleton(context);
        }
        return sTaskSingleton;
    }
    private TaskSingleton(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();

        //mTasks = new ArrayList<>();

    }

    public void addTask(Task t) {
        //mTasks.add(t);
        ContentValues values = getContentValues(t);

        mDatabase.insert(TaskDbSchema.TaskTable.NAME, null, values);
    }

    public void deleteTask(Task t) {

        String uuidString = t.getId().toString();
        mDatabase.delete(TaskDbSchema.TaskTable.NAME,
                TaskDbSchema.TaskTable.Cols.UUID + "= ?",
                new String[] {uuidString});
    }

    public List<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();
        TaskCursorWrapper cursor = queryTasks(null,
                null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return tasks;
    }

    public Task getTask(UUID id) {
        TaskCursorWrapper cursor = queryTasks(
                TaskDbSchema.TaskTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public void updateTask(Task task) {
        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);
        mDatabase.update(TaskDbSchema.TaskTable.NAME, values,
                TaskDbSchema.TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskDbSchema.TaskTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new TaskCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.TaskTable.Cols.UUID, task.getId().toString());

        values.put(TaskDbSchema.TaskTable.Cols.TITLE, task.getTitle());

        values.put(TaskDbSchema.TaskTable.Cols.DATE, task.getDate().getTime());

        values.put(TaskDbSchema.TaskTable.Cols.SOLVED, task.isSolved() ? 1 : 0);

        return values;
    }



}
