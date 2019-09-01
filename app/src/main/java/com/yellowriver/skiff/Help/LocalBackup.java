package com.yellowriver.skiff.Help;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.SourcesBean.GroupEntity;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.MainActivity;
import com.yellowriver.skiff.R;

import org.apache.http.util.EncodingUtils;
import org.greenrobot.greendao.internal.SqlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.security.Permissions;
import java.util.List;

public class LocalBackup {
    private Context mContext;




    public LocalBackup(Context mContext) {
        this.mContext = mContext;
    }

    public void backup(String sources,String frovite) {
        if (isExternalStorageWritable()) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

                String path =  sdCardDir.getPath()+"/Skiff";
                File skiffFile = new File(path);
                if (!skiffFile.exists()) {
                    if(skiffFile.mkdir())
                    {

                    }else{
                        Log.d("dd", "backup: 创建目录失败");
                    }
                }
                File saveSourceFile = new File(skiffFile, "sources.txt");
                File saveFroviteFile = new File(skiffFile, "frovite.txt");
                if (!saveSourceFile.exists())
                {
                    try {
                        saveSourceFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!saveFroviteFile.exists())
                {
                    try {
                        saveFroviteFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                FileOutputStream sourceOutStream = null;
                FileOutputStream froviteOutStream = null;
                try {
                    sourceOutStream = new FileOutputStream(saveSourceFile);
                    sourceOutStream.write(sources.getBytes());
                    froviteOutStream = new FileOutputStream(saveFroviteFile);
                    froviteOutStream.write(frovite.getBytes());
                    Toast.makeText(mContext,"备份成功",Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else
        {
            Log.d("ddd", "backup: 不可写");
        }
    }

    public void resume()
    {
        if (isExternalStorageReadable()) {
            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

            String path =  sdCardDir.getPath()+"/Skiff";
            File skiffFile = new File(path);
            if (!skiffFile.exists()) {
                Toast.makeText(mContext,"没有备份文件",Toast.LENGTH_LONG).show();
            }
            File saveSourceFile = new File(skiffFile, "sources.txt");
            File saveFroviteFile = new File(skiffFile, "frovite.txt");
            if (!saveSourceFile.exists())
            {
                Toast.makeText(mContext,"没有备份文件",Toast.LENGTH_LONG).show();
            }
            if (!saveFroviteFile.exists())
            {
                Toast.makeText(mContext,"没有备份文件",Toast.LENGTH_LONG).show();
            }
            FileInputStream fisSources = null;
            FileInputStream fisFrovite = null;
            try {
                fisSources = new FileInputStream(saveSourceFile);
                fisFrovite = new FileInputStream(saveFroviteFile);
                int lengthSources = fisSources.available();
                int lengthFrovite = fisFrovite.available();

                byte [] bufferSources  = new byte[lengthSources];
                byte [] bufferFrovite  = new byte[lengthFrovite];
                fisSources.read(bufferSources);
                fisFrovite.read(bufferFrovite);
                String sourcesStr =  new String(bufferSources, "UTF-8");
                String froviteStr =  new String(bufferFrovite, "UTF-8");
//                String sourcesStr = EncodingUtils.getString(bufferSources, "UTF-8");
//                String froviteStr = EncodingUtils.getString(bufferFrovite, "UTF-8");
                homeSql(sourcesStr);
                favioteSql(froviteStr);
                Toast.makeText(mContext,"恢复完成",Toast.LENGTH_LONG).show();

                fisSources.close();
                fisFrovite.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Log.d("dd", "resume: 文件不可读");
        }
    }




    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    private void homeSql(String json)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<List<HomeEntity>>() {
        }.getType();
        List<HomeEntity> homeEntityList = gson.fromJson(json, type);
        if (homeEntityList!=null&&homeEntityList.size()>0)
        {
            for (HomeEntity homeEntity : homeEntityList)
            {
                SQLiteUtils.getInstance().addHome(homeEntity);
            }
        }
    }

    private void favioteSql(String json)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<List<FavoriteEntity>>() {
        }.getType();
        List<FavoriteEntity> favoriteEntities = gson.fromJson(json, type);
        if (favoriteEntities!=null&&favoriteEntities.size()>0)
        {
            for (FavoriteEntity favoriteEntity : favoriteEntities)
            {
                SQLiteUtils.getInstance().addFavorite(favoriteEntity);
            }
        }
    }

    }
