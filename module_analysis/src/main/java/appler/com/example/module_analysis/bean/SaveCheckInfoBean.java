package appler.com.example.module_analysis.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuYanYan on 2019/7/10.
 */

public class SaveCheckInfoBean {

    //智能分析
    public List<Map<Integer, Boolean>> xzqhCheckedMap = new ArrayList<>(); //智能分析 行政区划checkBox选中状态
    public List<String> wktList = new ArrayList<>();//行政区划多选时 保存多个数据的wkt值
}
