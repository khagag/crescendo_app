package com.fci.crescendo;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fci.crescendo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button  stoprecord;
    MediaRecorder mrc;
    MediaPlayer mp;
    String outputfile;
    ImageButton record;
    //alert message
    public void opendialog(){
        dialog dialog= new dialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }
    //get data from api
    public void get(String api_url) {
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        StringRequest req=new StringRequest(StringRequest.Method.GET, api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //only for test we will replace it with pojo to print result in edittext
                // pasingresponse(response);
                Log.i("res",response);
               // pasingresponse(response);
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(req);

    }
    //post data
    private void post(String posturl) {
        RequestQueue q = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, posturl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //example
                params.put("song", outputfile);
              //  params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        q.add(postRequest);
    }

    //parsing response data
   // private void pasingresponse(String response) {
      //  Gson gson=new Gson();
        //use site "jsonschema2pojo"
        //copy json response and paste in the site to convert to java class and download it
        //create package and paste classes in it
        //main class will download with defult name example
        // import and call it in this method

      //  gson.fromJson(response,examble.class);
        // string composer_name =example.getname();.......etc to get all methods


    //}

    //duration method
    public void duration(){

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mrc.stop();
                        mrc.reset();
                        mrc.release();
                        record.setEnabled(true);
                        Toast.makeText(MainActivity.this, "time out.. record stopped", Toast.LENGTH_SHORT).show();
                        stoprecord.setEnabled(false);
                    }
                });

            }
        }, 10000);
        mp=new MediaPlayer();
        try {
            mrc.setOutputFile(outputfile);
        mp.setDataSource(outputfile);
        mp.prepare();
        mp.start();
        Toast.makeText(MainActivity.this, "processing....", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
          e.printStackTrace();
        }
          }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        opendialog();
       // get("get url");
       // post ("posturl");


        stoprecord= findViewById(R.id.button4);
        stoprecord.setEnabled(false);
        record=findViewById(R.id.imageButton);


        record.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mrc=new MediaRecorder();
                mrc.setAudioSource(MediaRecorder.AudioSource.MIC);
                mrc.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mrc.setOutputFile(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/myrecording.mp3");
                mrc.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                post("url of api");

                stoprecord.setEnabled(true);
                record.setEnabled(false);
                try {
                    mrc.prepare();
                    mrc.start();
                    duration();


                    // duration();
                } catch (IllegalStateException ise) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
                stoprecord.setEnabled(true);
                Toast.makeText(MainActivity.this, "record started.....", Toast.LENGTH_SHORT).show();

                return true;
            }});

            stoprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stoprecord.setEnabled(false);
                record.setEnabled(true);
                    mrc.stop();
                    mrc.release();
                    mrc=null;
                Toast.makeText(MainActivity.this, "record stoped", Toast.LENGTH_SHORT).show();
                mp=new MediaPlayer();
                try {
                    mrc.setOutputFile(outputfile);
                    mp.setDataSource(outputfile);
                    mp.prepare();
                    mp.start();
                    Toast.makeText(MainActivity.this, "processing....", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //show.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {
              //  MediaPlayer mediaplayer = new MediaPlayer();
                //try {
                  //  mrc.setOutputFile(outputfile);
                    //mediaplayer.setDataSource(outputfile);
                    //mediaplayer.prepare();
                    //mediaplayer.start();
                    //Toast.makeText(MainActivity.this, "playing", Toast.LENGTH_SHORT).show();
                //} catch (IOException e) {
                  //  e.printStackTrace();
                //}
          //  }
        //});



    }


}

