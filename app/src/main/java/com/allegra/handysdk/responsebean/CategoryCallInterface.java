package com.allegra.handysdk.responsebean;

import com.allegra.handysdk.bean.MainCategoryData;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public interface CategoryCallInterface {
    public void CategoryData(ArrayList<MainCategoryData> mainCategoryDatas);
    public void setdata(String selected,ArrayList<String> Sub_id);
}
