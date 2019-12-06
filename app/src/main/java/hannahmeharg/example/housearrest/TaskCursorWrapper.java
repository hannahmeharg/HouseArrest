package hannahmeharg.example.housearrest;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.UUID));

        String title = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.TITLE));

        long date = getLong(getColumnIndex(TaskDbSchema.TaskTable.Cols.DATE));

        int isSolved = getInt(getColumnIndex(TaskDbSchema.TaskTable.Cols.SOLVED));

        Task task = new Task(UUID.fromString(uuidString));

        task.setTitle(title);
        task.setDate(new Date(date));
        task.setSolved(isSolved != 0);
        return task;
    }
}
