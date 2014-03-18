package com.huynguyen.GithubAnalytics.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.huynguyen.GithubAnalytics.R;
import com.huynguyen.GithubAnalytics.Utils;
import com.huynguyen.GithubAnalytics.adapters.IssuesAdapter;
import com.huynguyen.GithubAnalytics.asynctasks.FetchIssuesTask;
import com.huynguyen.GithubAnalytics.model.IssuesManager;
import com.huynguyen.GithubAnalytics.model.pojo.Issue;
import com.huynguyen.GithubAnalytics.model.pojo.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssuesActivity extends Activity
        implements FetchIssuesTask.Listener {
    private static final int DIALOG_FETCH_ISSUES = 0;
    private static final int DIALOG_FILTER_ASSIGNEE = 1;
    private static final int DIALOG_FILTER_STATE = 2;
    private static final int DIALOG_FILTER_AUTHOR = 3;
    private static final int DIALOG_FILTER_CREATED_DATE = 4;

    private FetchIssuesTask fetchIssuesTask;
    private ListView listView;
    private List<User> filterAssignees;
    private String filterState;
    private List<User> filterAuthors;
    private Date filterCreatedDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issues);

        Button btnFilterAssignee = (Button) findViewById(R.id
                .btn_filer_assignee);
        Button btnFilterState = (Button) findViewById(R.id.btn_filer_state);
        Button btnFilterAuthors = (Button) findViewById(R.id.btn_filer_author);
        Button btnFilterCreatedDate = (Button) findViewById(R.id
                .btn_filer_crate_date);
        listView = (ListView) findViewById(android.R.id.list);

        btnFilterAssignee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FILTER_ASSIGNEE);
            }
        });

        btnFilterState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FILTER_STATE);
            }
        });

        btnFilterCreatedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FILTER_CREATED_DATE);
            }
        });

        btnFilterAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FILTER_AUTHOR);
            }
        });

        if (IssuesManager.getInstance().isEmpty()) {
            fetchIssues();
        } else {
            showIssues();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fetchIssuesTask != null) {
            fetchIssuesTask.setListener(null);
            fetchIssuesTask.cancel(true);
            fetchIssuesTask = null;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_FETCH_ISSUES) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Fetching issues");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return dialog;
        } else if (id == DIALOG_FILTER_ASSIGNEE) {
            final List<User> assignees = IssuesManager.getInstance()
                    .getAssignees();

            return new AlertDialog.Builder(this)
                    .setTitle("Assignees")
                    .setAdapter(
                            new ArrayAdapter<User>(this,
                                    R.layout.simple_list_item,
                                    assignees),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    filterAssignees = new ArrayList<User>(1);
                                    filterAssignees.add(assignees.get(which));
                                    showIssues();
                                    dialog.dismiss();
                                }
                            })
            .create();
        } else if (id == DIALOG_FILTER_STATE) {
            final String[] states = new String[]{"open", "closed"};

            return new AlertDialog.Builder(this)
                    .setTitle("States")
                    .setAdapter(
                            new ArrayAdapter<String>(this,
                                    R.layout.simple_list_item,
                                    states),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    filterState = states[which];
                                    showIssues();
                                    dialog.dismiss();
                                }
                            })
                    .create();
        } else if (id == DIALOG_FILTER_AUTHOR) {
            final List<User> authors = IssuesManager.getInstance()
                    .getAuthors();

            return new AlertDialog.Builder(this)
                    .setTitle("Authors")
                    .setAdapter(
                            new ArrayAdapter<User>(this,
                                    R.layout.simple_list_item,
                                    authors),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    filterAuthors = new ArrayList<User>(1);
                                    filterAuthors.add(authors.get(which));
                                    showIssues();
                                    dialog.dismiss();
                                }
                            })
                    .create();
        } else if (id == DIALOG_FILTER_CREATED_DATE) {
            final String[] dates = new String[] {"Yesterday", "Today"};

            return new AlertDialog.Builder(this)
                    .setTitle("Created date")
                    .setAdapter(
                            new ArrayAdapter<String>(this,
                                    R.layout.simple_list_item,
                                    dates),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Date today = new Date();
                                    if (which == 0) {
                                        // Yesterday
                                        filterCreatedDate = new Date(
                                                today.getYear(),
                                                today.getMonth(),
                                                today.getDate() - 1
                                        );
                                    } else if (which == 1) {
                                        filterCreatedDate = today;
                                    }
                                    showIssues();
                                    dialog.dismiss();
                                }
                            })
                    .create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onFinish() {
        showIssues();
        dismissDialog(DIALOG_FETCH_ISSUES);
    }

    @Override
    public void onFailed(Throwable th) {
        setTitle("Error: " + th.getLocalizedMessage());
        dismissDialog(DIALOG_FETCH_ISSUES);
    }

    private void fetchIssues() {
        showDialog(DIALOG_FETCH_ISSUES);
        fetchIssuesTask = new FetchIssuesTask(this);
        fetchIssuesTask.execute();
    }

    private void showIssues() {
        List<Issue> issues = IssuesManager.getInstance().getIssues();

        // Filter the result
        issues = filterAssignees(issues);
        issues = filterState(issues);
        issues = filterAuthors(issues);
        issues = filterCreatedDate(issues);

        IssuesAdapter adapter = new IssuesAdapter(this, R.layout.item_issue,
                issues);
        listView.setAdapter(adapter);
    }

    private List<Issue> filterAssignees(List<Issue> originalIssues) {
        if (filterAssignees == null || filterAssignees.size() == 0) {
            return originalIssues;
        }

        List<Issue> result = new ArrayList<Issue>();
        for (Issue i : originalIssues) {
            if (filterAssignees.contains(i.getAssignee())) {
                result.add(i);
            }
        }

        return result;
    }

    private List<Issue> filterState(List<Issue> originalIssues) {
        if (TextUtils.isEmpty(filterState)) {
            return originalIssues;
        }

        List<Issue> result = new ArrayList<Issue>();
        for (Issue i : originalIssues) {
            if (i.getState().equals(filterState)) {
                result.add(i);
            }
        }

        return result;
    }

    private List<Issue> filterAuthors(List<Issue> originalIssues) {
        if (filterAuthors == null || filterAuthors.size() == 0) {
            return originalIssues;
        }

        List<Issue> result = new ArrayList<Issue>();
        for (Issue i : originalIssues) {
            if (filterAuthors.contains(i.getAuthor())) {
                result.add(i);
            }
        }

        return result;
    }

    private List<Issue> filterCreatedDate(List<Issue> originalIssues) {
        if (filterCreatedDate == null) {
            return originalIssues;
        }

        List<Issue> result = new ArrayList<Issue>();
        for (Issue i : originalIssues) {
            if (Utils.sameDate(i.getCreatedAt(), filterCreatedDate)) {
                result.add(i);
            }
        }

        return result;
    }
}