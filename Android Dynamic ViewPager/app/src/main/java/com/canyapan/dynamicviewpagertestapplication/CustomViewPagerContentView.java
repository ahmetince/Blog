package com.canyapan.dynamicviewpagertestapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class CustomViewPagerContentView extends RelativeLayout {
    private int mItemID;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private LoadDateAsyncTask mAsyncTask = null;

    public CustomViewPagerContentView(Context context) {
        this(context, null);
    }

    public CustomViewPagerContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewPagerContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTextView = (TextView) findViewById(R.id.text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    public void setContent(int itemID) {
        // Cancel any old probably ongoing process.
        if (null != mAsyncTask) {
            mAsyncTask.cancel(true);
        }

        mItemID = itemID;

        mAsyncTask = new LoadDateAsyncTask();
        mAsyncTask.execute(Integer.toString(itemID));
    }

    public int getItemID() {
        return mItemID;
    }

    protected class LoadDateAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            mTextView.setVisibility(TextView.GONE);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Simulating some process or database operation to get the dynamic data.
                Thread.sleep(200 + new Random().nextInt(800));

                return params[0];
            } catch (InterruptedException ignore) {
            }

            // Incomplete operation..
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (null != result) {
                mProgressBar.setVisibility(ProgressBar.GONE);
                mTextView.setText(result);
                mTextView.setVisibility(TextView.VISIBLE);
            }
        }

        protected void onCancelled() {
            mTextView.setVisibility(TextView.GONE);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);

            super.onCancelled();
        }
    }
}
