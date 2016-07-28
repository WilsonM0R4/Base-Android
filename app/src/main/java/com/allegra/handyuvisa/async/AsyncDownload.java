package com.allegra.handyuvisa.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by victor on 22/02/15.
 * com.allem.allemevent.async
 */


public class AsyncDownload extends AsyncTask<String, Void, Boolean> {

    private static final String TAG="AsyncDownload";
    private String pathto,filename,url;
    private int codeRequest;

    private AsyncDownload(String url, String pathto,String filename,int codeRequest){
        this.url=url;
        this.pathto=pathto;
        this.filename=filename;
        this.codeRequest = codeRequest;
    }

    public static AsyncDownload getInstance(String url, String pathto,String filename,int codeRequest){
        return new AsyncDownload(url,pathto,filename,codeRequest);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        Log.d(TAG, "parameter: " + url + "\n" + filename + "\n" + pathto);
        return  downloadFile(url,filename,pathto);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result) MyBus.getInstance().post(new AsyncTaskBooleanResultEvent(result,codeRequest));
    }

    private boolean downloadFile(String link,String filename, String path){
        boolean result =false;
        try {
            String linkencoded = Uri.encode(link);
            Log.d(TAG, "link encoded: " + linkencoded + "\n" + link);
            URL url = new URL(link);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            InputStream is = url.openStream();
            File testDirectory = new File(path);
            if (!testDirectory.exists()) {
                testDirectory.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(testDirectory + "/" +filename);
            byte data[] = new byte[1024];
            int count = 0;
            long total = 0;
            int progress = 0;
            while ((count = is.read(data)) != -1) {
                total += count;
                int progress_temp = (int) total * 100 / lenghtOfFile;
                if (progress_temp % 10 == 0 && progress != progress_temp) {
                    progress = progress_temp;
                }
                fos.write(data, 0, count);
            }
            is.close();
            fos.close();
            result = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
