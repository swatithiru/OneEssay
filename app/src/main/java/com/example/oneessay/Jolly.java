package com.example.oneessay;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

public class Jolly extends AsyncTask<Integer,Integer,Integer> {
    ProgressBar progressBar;
    Activity activity = new Activity();
    public Jolly(Activity activity)
    {
      this.activity=activity;
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        for(int i=0;i<10;i++)
        {
            try
            {
                Thread.sleep(1000);
                publishProgress(i);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        super.onPostExecute(integer);
    }

    @Override
    protected void onPreExecute() {
        progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
        progressBar.setMax(10);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
        progressBar.setProgress(values[0]);
        super.onProgressUpdate(values);
    }
}
