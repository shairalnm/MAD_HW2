package com.example.group5_hw2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    SeekBar threadCountSeekbar;
    TextView threadCountNumber;
    SeekBar threadLengthSeekbar;
    TextView threadLengthNumber;
    int threadCount = 1;
    int threadLength = 7;
    static String THREAD_KEY = "Thread";
    ArrayList<String> generatedPasswordThread;

    SeekBar asyncCountSeekbar;
    TextView asyncCountNumber;
    SeekBar asyncLengthSeekbar;
    TextView asyncLengthNumber;
    ProgressDialog progressDialog;
    int asyncCount = 1;
    int asyncLength = 7;
    static String ASYNC_KEY = "Async";
    ArrayList<String> generatedPasswordAsync;
    Handler handler;

    Intent intent;
    static int progress = 0;

    static final int STATUS_START = 0x00;
    static final int STATUS_PROGRESS = 0x01;
    static final int STATUS_STOP = 0x02;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Generator");

        threadCountSeekbar = findViewById(R.id.seekBarSelectPasswordCount1);
        threadCountNumber = findViewById(R.id.textViewSelectPasswordLength1);
        threadLengthSeekbar = findViewById(R.id.seekBarSelectPasswordCount2);
        threadLengthNumber = findViewById(R.id.textViewSelectPasswordLength2);
        threadCountNumber.setText("1");
        threadLengthNumber.setText("7");
        threadCountSeekbar.setMin(1);
        threadCountSeekbar.setMax(10);
        threadLengthSeekbar.setMin(7);
        threadLengthSeekbar.setMax(23);
        threadCountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threadCountNumber.setText(progress + "");
                threadCount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        threadLengthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threadLengthNumber.setText(progress + "");
                threadLength = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        asyncCountSeekbar = findViewById(R.id.seekBarSelectPasswordCount2);
        asyncLengthSeekbar = findViewById(R.id.seekBarSelectPasswordLength2);
        asyncCountNumber = findViewById(R.id.textViewSelectPasswordCount2);
        asyncLengthNumber = findViewById(R.id.textViewSelectPasswordLength2);
        asyncCountNumber.setText("1");
        asyncLengthNumber.setText("7");
        asyncCountSeekbar.setMin(1);
        asyncCountSeekbar.setMax(10);
        asyncLengthSeekbar.setMin(7);
        asyncLengthSeekbar.setMax(23);
        asyncCountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                asyncCountNumber.setText(progress + "");
                asyncCount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        asyncLengthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                asyncLengthNumber.setText(progress + "");
                asyncLength = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final ExecutorService threadPool;
        threadPool = Executors.newFixedThreadPool(2);

        findViewById(R.id.buttonGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (threadCount == 0 || threadLength == 0 || asyncCount == 0 || asyncLength == 0) {
                    Toast.makeText(getApplicationContext(), "Set Count and Length!", Toast.LENGTH_SHORT).show();
                } else {
                    threadPool.execute(new generatePasswordThread());
                    new generatePasswordAsync().execute();
                    intent = new Intent(MainActivity.this, GeneratedPasswordsActivity.class);
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Generating Passwords");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    progressDialog.setMax(asyncCount > threadCount ? asyncCount : threadCount);
                    progressDialog.setMax(asyncCount + threadCount);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    progress = 0;
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if (msg.getData().getInt("ThreadProgress") > msg.getData().getInt("AsyncProgress")) {
                        progress += 1;
                        progressDialog.setProgress(progress);
                    } else if (msg.getData().getInt("ThreadProgress") < msg.getData().getInt("AsyncProgress")) {
                        progress += 1;
                        progressDialog.setProgress(progress);
                    }

//                Log.d("demo", msg.getData().getInt("ThreadProgress") + " -- Thread");
//                Log.d("demo", msg.getData().getInt("AsyncProgress") + " -- Async");
//                Log.d("demo", "------- ----------- --- ---------");

                    if (msg.getData().getInt("AsyncStatus") == 1) {
                        generatedPasswordAsync = msg.getData().getStringArrayList(ASYNC_KEY);
                        intent.putExtra(ASYNC_KEY, generatedPasswordAsync);
                    } else if (msg.getData().getInt("ThreadStatus") == 1) {
                        generatedPasswordThread = msg.getData().getStringArrayList(THREAD_KEY);
                        intent.putExtra(THREAD_KEY, generatedPasswordThread);
                    }
                    if (generatedPasswordAsync != null && generatedPasswordThread != null) {
                        startActivity(intent);
                        progressDialog.dismiss();
                        asyncCountSeekbar.setProgress(0);
                        asyncLengthSeekbar.setProgress(0);
                        threadCountSeekbar.setProgress(0);
                        threadLengthSeekbar.setProgress(0);
                        generatedPasswordThread = null;
                        generatedPasswordAsync = null;
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public class generatePasswordThread implements Runnable {
        @Override
        public void run() {
            ArrayList<String> generatedPasswordThread = new ArrayList<>();
            for (int i = 0; i <= threadCount; i++) {
                generatedPasswordThread.add(Util.getPassword(threadLength));
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("ThreadProgress", i);
                message.setData(bundle);
                handler.sendMessage(message);
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("ThreadStatus", 1);
            bundle.putStringArrayList(THREAD_KEY, generatedPasswordThread);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    public class generatePasswordAsync extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            ArrayList<String> generatePasswordAsync = new ArrayList<>();
            for (int i = 0; i <= asyncCount; i++) {
                generatePasswordAsync.add(Util.getPassword(asyncLength));
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("AsyncProgress", i);
                message.setData(bundle);
                handler.sendMessage(message);
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("AsyncStatus", 1);
            bundle.putStringArrayList(ASYNC_KEY, generatePasswordAsync);
            message.setData(bundle);
            handler.sendMessage(message);
            return 0;
        }
    }

}
