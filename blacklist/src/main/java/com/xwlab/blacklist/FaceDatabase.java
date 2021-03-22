package com.xwlab.blacklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.xwlab.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.xwlab.blacklist.HttpUtils.sendJsonPost;
import static com.xwlab.util.utils.getAgeByBirthday;
import static java.lang.Math.sqrt;

public class FaceDatabase {
    private List<KeyPersonnel> blacklist = new ArrayList<>();

    public List<KeyPersonnel> getBlacklist() {
        return blacklist;
    }

    //    private MyDatabaseHelper helper;
    private String logFile;
    private String logDir;
    private final String mtable = "FaceInfo";
    private static final String TAG = "FaceDatabase";
    boolean initialedStatus = false;
    private SharedPreferences share;

    public FaceDatabase(Context context) {
//        share = context.getSharedPreferences("attendance", Context.MODE_PRIVATE);
//        helper = new MyDatabaseHelper(context, "faceDataBase.db");
//        SQLiteDatabase database = helper.getReadableDatabase();

        // 更新日志的路径
        File sdDir = Environment.getExternalStorageDirectory();//get directory
        logDir = sdDir.toString() + "/attendance/";
        logFile = logDir + "update_log.txt";

        //从sqlite数据库中加载
//        Cursor cursor = database.query(mtable, null, null, new String[]{}, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                String phoneNum = cursor.getString(cursor.getColumnIndex("phoneNum"));
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String feature = cursor.getString(cursor.getColumnIndex("feature"));
//                String password = cursor.getString(cursor.getColumnIndex("password"));
//                Logger.i(TAG, "从数据库读取信息 phoneNum: " + phoneNum + " name: " + name);
////                Log.i(TAG,  "feature: " + feature);
//                flush(name, feature, phoneNum, password);
//            } while (cursor.moveToNext());
//
//            Logger.i(TAG, "loading Data done");
//        }
//        cursor.close();
    }

    /**
     * 比对人脸特征
     *
     * @return 返回匹配的人名，比对不成功则返回""
     */
    public KeyPersonnel featureCmp(String fstr) {

        String[] fs = fstr.trim().split(",");
        if (fs.length != 128) {
            return null;
        }

        double[] feature = new double[128];
        for (int i = 0; i < 128; i++) {
            feature[i] = Double.parseDouble(fs[i]);
        }

        int index = 0;
        double mSim = 0;
        for (int i = 0; i < blacklist.size(); i++) {
            double sim = calculSimilar(feature, blacklist.get(i).getFaceFeature());
            if (sim > mSim) {
                index = i;
                mSim = sim;     //选出匹配度最高的
            }

        }
        Logger.i(TAG, "max sim is " + mSim);
        if (mSim > 0.6) {
            return blacklist.get(index);
        } else {
            return null;
        }
    }

    /*
    密码比对
     */
//    public boolean passwordCmp(String input) {
//        Logger.i(TAG, input);
//        for (int i = 0; i < passwords.size(); i++) {
//            Logger.i(TAG, passwords.get(i) + passwords.get(i).length());
//            if (!passwords.get(i).isEmpty() || !passwords.get(i).equals("null")) {
//                Logger.i(TAG, i + "not null");
//                if (passwords.get(i).equals(input)) {
//                    Logger.i(TAG, "success");
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 对比两个特征，字符串格式
     *
     * @return 返回匹配度，如果插入失败返回0
     */
    public double calculSimilar(String f1, String f2) {
        String fs1[] = f1.split(",");
        String fs2[] = f2.split(",");
        if (fs1.length != 128) {
            fs1 = f1.split(" ");
            if (fs1.length != 128) return 0;
        }
        if (fs2.length != 128) {
            fs2 = f2.split(" ");
            if (fs2.length != 128) return 0;
        }
        double[] v1 = new double[128];
        double[] v2 = new double[128];
        for (int i = 0; i < 128; i++) {
            v1[i] = Double.parseDouble(fs1[i]);
            v2[i] = Double.parseDouble(fs2[i]);
        }
        if (v1.length != v2.length || v1.length == 0)
            return 0;
        double ret = 0.0, mod1 = 0.0, mod2 = 0.0;
        for (int i = 0; i != v1.length; ++i) {
            ret += v1[i] * v2[i];
            mod1 += v1[i] * v1[i];
            mod2 += v2[i] * v2[i];
        }
        return ret / sqrt(mod1) / sqrt(mod2);
    }

    /**
     * 添加数据到FaceInfo表中
     *
     * @return 返回新插入的行号，如果插入失败返回-1
     */
//    public long addData(String name, String feature, String personId, String password) {
////        Log.d("thisadd", "addData: ");
//        SQLiteDatabase database = helper.getReadableDatabase();
//        if (flush(name, feature, personId, password)) {
//            // 使用anddroid封装的SQL语法
//            ContentValues values = new ContentValues();
//            values.put("name", name);
//            values.put("feature", feature);
//            values.put("phoneNum", personId);
//            values.put("password", password);
//            long insert = database.insert(mtable, null, values);
//            Logger.i(TAG, "新增人员信息 name: " + name + " phoneNum: " + personId + " password: " + password);
//            return insert;
//        } else return -1;
//    }

    /**
     * 更新FaceInfo表中的数据
     *
     * @return 返回受影响的行
     */
//    public int updateData(String name, String feature, String phoneNum, String password) {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        // 使用anddroid封装的SQL语法
//        ContentValues values = new ContentValues();
//        values.put("name", name);
//        values.put("feature", feature);
//        values.put("phoneNum", phoneNum);
//        values.put("password", password);
//
//        // 更新内存的信息
//        for (int i = 0; i < phoneNums.size(); i++) {
//            if (phoneNums.get(i).equals(phoneNum)) {
//                names.set(i, name);
//                double[] featureArray;
//                if (feature.isEmpty() || feature.equals("null")) {
//                    featureArray = null;
//                } else {
//                    featureArray = featureStringToArray(feature);
//                }
//                if (featureArray != null && featureArray.length != 128) {
//                    Logger.e(TAG, "the feature of name: " + name + " phoneNum: " + phoneNum + " is invalid");
//                    Logger.e(TAG, "feature is: " + feature);
//                    return -1;
//                }
//                AllFeatures.set(i, featureArray);
//                break;
//            }
//        }
//        // 更新数据库的信息
//        int update = database.update(mtable, values, "phoneNum = ?", new String[]{phoneNum.toString()});   //返回受影响的行
//        Logger.i(TAG, "更新人员信息 name: " + name + " phoneNum: " + phoneNum + " password: " + password);
//        return update;
//    }

    /**
     * 根据name删除FaceInfo中的数据
     *
     * @return 返回受影响的行
     */
//    public int deleteByName(String name) {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        // 使用anddroid封装的SQL语法
//        int delete = database.delete(mtable, "name = ?", new String[]{name});
//        return delete;
//    }

    /**
     * PersonId
     *
     * @return 返回受影响的行
     */
//    public int deleteByPhoneNum(String phoneNum) {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        // 使用anddroid封装的SQL语法
//        int delete = database.delete(mtable, "phoneNum = ?", new String[]{phoneNum.toString()});
//        return delete;
//    }
//
//    public boolean isExistPhoneNum(String phoneNum) {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        Cursor cursor = database.query(mtable, null, "phoneNum = ?", new String[]{phoneNum.toString()}, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                String personName = cursor.getString(cursor.getColumnIndex("name"));
//                String fstr = cursor.getString(cursor.getColumnIndex("feature"));
//                Log.i(TAG, "exist peronId: " + phoneNum + " name: " + personName);
//            } while (cursor.moveToNext());
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 删除FaceInfo中所有的数据
     *
     * @return 返回受影响的行
     */
//    public int deleteAllData() {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        // 使用anddroid封装的SQL语法
//        int delete = database.delete(mtable, null, new String[]{});
//
//        File file = new File(logFile);
//        if (file.exists()) {
//            boolean res = file.delete();
//        }
//        AllFeatures.clear();
//        names.clear();
//        phoneNums.clear();
//        passwords.clear();
//        return delete;
//    }

    /**
     * 根据name查询FaceInfo表中的数据
     *
     * @return 返回的是name对应的feature字符串
     */
//    public String query(String name) {
//        SQLiteDatabase database = helper.getReadableDatabase();
//        Cursor cursor = database.query(mtable, null, "name = ?", new String[]{name}, null, null, null);
//        String res = "";
//        if (cursor.moveToFirst()) {
//            do {
//                int person_id = cursor.getInt(cursor.getColumnIndex("person_id"));
//                String person_name = cursor.getString(cursor.getColumnIndex("name"));
//                String feature = cursor.getString(cursor.getColumnIndex("feature"));
//                Log.i(TAG, "person_id: " + person_id);
//                Log.i(TAG, "name: " + person_name);
//                Log.i(TAG, "feature: " + feature);
//                res = feature;
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return res;
//    }

    //特征描述字符串转double数组
    private double[] featureStringToArray(String feature) {
        String[] fs = feature.split(",");   //按逗号分割
        if (fs.length != 128) {
            fs = feature.split(" ");        //按空格分割
        }
        double[] featureArray = new double[128];
        for (int i = 0; i < 128; i++) {
            featureArray[i] = Double.parseDouble(fs[i]);
        }
        return featureArray;
    }

    /**
     * 根据加载name和feature到内存中，方便比对
     */
//    private boolean flush(String personName, String feature, String phoneNum, String password) {
//        double[] featureArray;
//        if (feature.isEmpty() || feature.equals("null")) {
//            featureArray = null;
//        } else {
//            featureArray = featureStringToArray(feature);
//        }
//
//        if (featureArray != null && featureArray.length != 128) { //特征描述数组不为空并且长度不为128，则报错
//            Logger.e(TAG, "the feature of name: " + personName + " phoneNum: " + phoneNum + " is invalid");
//            Logger.e(TAG, "feature is: " + feature);
//            return false;
//        }
//        AllFeatures.add(featureArray);
//        names.add(personName);
//        phoneNums.add(phoneNum);
//        passwords.add(password);
//        Log.i(TAG, "flush feature for " + personName);
//        return true;
//    }

    /**
     * 特征匹配
     * 根据加载name和feature到内存中，方便比对
     */
    public double calculSimilar(double[] v1, double[] v2) {
        if (v1.length != v2.length || v1.length == 0)
            return 0;
        double ret = 0.0, mod1 = 0.0, mod2 = 0.0;
        for (int i = 0; i != v1.length; ++i) {
            ret += v1[i] * v2[i];
            mod1 += v1[i] * v1[i];
            mod2 += v2[i] * v2[i];
        }
        return ret / sqrt(mod1) / sqrt(mod2);
    }


    /*
     * 定义文件保存的方法，写入到文件中，所以是输出流
     * */
    public String getUpdataTime() {
        FileInputStream fis = null;
        String result = "";
        try {
            /* 判断sd的外部设置状态是否可以读写 */
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(logFile);
                if (!file.exists()) {
                    return result;
                }
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                result = new String(buffer, "UTF-8");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
     * 定义文件保存的方法，写入到文件中，所以是输出流
     * */
    public void writeUpdateTime(String content) {
        FileOutputStream fos = null;
        try {
            /* 判断sd的外部设置状态是否可以读写 */
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(logDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(logFile);
                // 先清空内容再写入
                fos = new FileOutputStream(file);
                byte[] buffer = content.getBytes();
                fos.write(buffer);
                fos.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shareWrite(String lastUpdateTime) {

    }

    public boolean loadCsv(String path) {
        // 通过本函数加载的人脸数据为测试数据，不添加到Database中
        Log.i(TAG, "加载本地csv文件: " + path);
        File file = new File(path);
        FileInputStream fileInputStream;
        Scanner in;
        try {
            fileInputStream = new FileInputStream(file);
            in = new Scanner(fileInputStream, "GBK");
            Log.i(TAG, "line: " + in.nextLine());
            int i = 0;
            int j = 0;
            while (in.hasNextLine()) {
                String line = in.nextLine();
//                Log.i(TAG,"line: "+ line);
                i = 0;
                j = line.indexOf(',');
                String name = line.substring(i, j);
                i = j + 1;
                j = line.indexOf(',', i + 1);
                String id = line.substring(i, j);
                i = j + 2;
                j = line.length();
                String fstr = line.substring(i, j - 1);
                // csv文件的人员personId默认为-1
//                if (!flush(name, fstr, "-1")) {
//                    Log.e("FaceDatebase", "loading csv error");
//                    return false;
//                }
                Log.i(TAG, "从本地csv文件成功录入 name: " + name + " person_id: " + "-1");
            }
            Log.i("FaceDatebase", "csv loaded");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Runnable loadRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("service", "keyPersonnel.getList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = sendJsonPost(requestBody.toString());
            try {
                JSONObject result = new JSONObject(response);
                JSONArray keyPersonnelList = result.optJSONArray("keyPersonnelList");
                for (int i = 0; i < keyPersonnelList.length(); i++) {
                    JSONObject keyPersonnelJson = keyPersonnelList.getJSONObject(i);
                    String name = keyPersonnelJson.optString("name");
                    String identifyId = keyPersonnelJson.optString("identifyId");
                    String gender = keyPersonnelJson.optString("gender");
                    String birth = keyPersonnelJson.optString("birth");
                    String type = keyPersonnelJson.optString("type");
                    String caseFile = keyPersonnelJson.optString("caseFile");
                    String faceBase64 = keyPersonnelJson.optString("faceBase64");
                    String feature = keyPersonnelJson.optString("feature");
                    String featureWithMask = keyPersonnelJson.optString("featureWithMask");
                    byte[] faceBytes = Base64.decode(faceBase64, Base64.NO_WRAP);
                    Bitmap face = BitmapFactory.decodeByteArray(faceBytes, 0, faceBytes.length);
                    KeyPersonnel keyPersonnel = new KeyPersonnel(identifyId, name, face, featureStringToArray(feature), featureStringToArray(featureWithMask), gender, getAgeByBirthday(birth), type, caseFile);
                    Logger.i(TAG, keyPersonnel.toString());
                    blacklist.add(keyPersonnel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    // 从后台(云端)获取人脸数据库
//    public class Updatemysql extends Thread {
//        @Override
//        public void run() {
//            Logger.i(TAG, "start->加载数据库");
//            JSONObject jsonObject = new JSONObject();
//            String timestamp = HttpUtils.getSecondTimestamp();   //获取当前的时间戳
//
//            try {
//                //*** 公共参数 ***//
//                jsonObject.put("apiVersion", "1.0");
//                jsonObject.put("charset", "UTF-8");
//                jsonObject.put("productCode", "DOOR");
//                jsonObject.put("system", "ANDROID");
//                jsonObject.put("timestamp", timestamp);
//                //******  人脸信息接口 ******//
//                jsonObject.put("service", "door.person.getAll");
////                jsonObject.put("doorId", 8);
////                updataTime_last 表示上次更新本地数据库的时间，本次会更新从这个时间之后新增的数据
////                String updataTime_last = getUpdataTime();
//                jsonObject.put("community", "豪利花园");
//                jsonObject.put("gate", "南门");
//                String updataTime_last = share.getString("lastUpdateTime", "2020-02-02 00:00:00");
////                if (TextUtils.isEmpty(updataTime_last)) {
////                    updataTime_last = "2020-02-02 00:00:00";
////                }
//                jsonObject.put("updateTime", updataTime_last);
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//
//            String result = HttpUtils.sendJsonPost(jsonObject.toString());
//
//            Logger.i(TAG, "result is " + result);
//            int addCount = 0;
//            int updateCount = 0;
//            try {
//                JSONObject jsonObj = new JSONObject(result);
//                JSONArray userInfoList = (JSONArray) jsonObj.get("personInfos");
//                int resultCode = jsonObj.getInt("resultCode");
//                if (resultCode == -1) {
//                    for (int i = 0; i < userInfoList.length(); i++) {
//                        String userInfo = (String) userInfoList.get(i);
//                        String[] infos = userInfo.split(",");
//                        String phoneNum = infos[0];
//                        String name = infos[1];
//                        String password = infos[2];
//                        int start = userInfo.indexOf('[');
//                        int end = userInfo.indexOf(']');
//                        String feature = userInfo.substring(start + 1, end);
//                        if (isExistPhoneNum(phoneNum)) {
//                            if (updateData(name, feature, phoneNum, password) == -1) {
//                                Logger.e(TAG, "更新人员信息失败，用户：" + phoneNum);
//                                return;
//                            } else {
//                                updateCount++;
//                                SharedPreferences.Editor editor = share.edit();
//                                editor.putString("lastUpdateTime", timestamp);
//                                editor.commit();
//                            }
//                        } else {
//                            // 增加新的personId数据
//                            if (addData(name, feature, phoneNum, password) == -1) {
//                                Logger.e(TAG, "增加人员信息失败：用户：" + phoneNum);
//                                return;
//                            } else {
//                                addCount++;
//                                SharedPreferences.Editor editor = share.edit();
//                                editor.putString("lastUpdateTime", timestamp);
//                                editor.commit();
//                            }
//                        }
//                    }
//                    //更新成功，保存这次的更新时间到本地
////            writeUpdateTime(timestamp);
//
//                }
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//            Logger.i("TAG", "新增人员数：" + addCount + " || 更新人员数：" + updateCount);
//            initialedStatus = true;
//        }
//    }

    public void loadDatabase() {
        new Thread(loadRunnable).start();
    }

    public boolean isInitialed() {
        return initialedStatus;
    }

}