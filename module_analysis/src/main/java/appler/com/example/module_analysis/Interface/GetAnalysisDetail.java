package appler.com.example.module_analysis.Interface;

import java.util.List;

import appler.com.example.module_analysis.bean.AnalysisDetailData;

public interface GetAnalysisDetail {
//    请求智能分析的详情接口
    void getAnalysisDetail(List<AnalysisDetailData.RowsBean> detail);
}
