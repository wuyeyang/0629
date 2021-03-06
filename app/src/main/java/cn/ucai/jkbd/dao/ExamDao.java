package cn.ucai.jkbd.dao;

import android.util.Log;

import java.util.List;

import cn.ucai.jkbd.ExamApplication;
import cn.ucai.jkbd.bean.Exam;
import cn.ucai.jkbd.bean.ExamInfo;
import cn.ucai.jkbd.bean.Result;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;

/**
 * Created by clawpo on 2017/6/30.
 */

public class ExamDao implements IExamDao {
    @Override
    public void loadExamInfo() {
        OkHttpUtils<ExamInfo> utils = new OkHttpUtils<>(ExamApplication.getInstance());
        String uri = "http://101.251.196.90:8080/JztkServer/examInfo";
        utils.url(uri)
                .targetClass(ExamInfo.class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamInfo>() {
                    @Override
                    public void onSuccess(ExamInfo result) {
                        Log.e("main","result="+result);
                        ExamApplication.getInstance().setExamInfo(result);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main","error="+error);
                    }
                });
    }

    @Override
    public void loadQuestionLists() {
        OkHttpUtils<String> utils1 = new OkHttpUtils<>(ExamApplication.getInstance());
        String url2 = "http://101.251.196.90:8080/JztkServer/getQuestions?testType=rand";
        utils1.url(url2)
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String jsonStr) {
                        Result result = ResultUtils.getListResultFromJson(jsonStr);
                        if (result!=null && result.getError_code()==0){
                            List<Exam> list = result.getResult();
                            if (list!=null && list.size()>0){
                                ExamApplication.getInstance().setExamList(list);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main","error="+error);
                    }
                });
    }
}
