package appler.com.example.module_analysis.bean;

import java.util.List;

public class AnalysisDetailData {


    private List<RowsBean> Rows;

    public List<RowsBean> getRows() {
        return Rows;
    }

    public void setRows(List<RowsBean> Rows) {
        this.Rows = Rows;
    }

    @Override
    public String toString() {
        return "AnalysisDetailData{" +
                "Rows=" + Rows +
                '}';
    }

    public static class RowsBean {
        /**
         * 用地名称 : 二类居住用地
         * ReArea : 8911.75
         * ogr_geometry : MULTIPOLYGON (((457512.61054992676 3863660.735748291, 457642.65617627371 3863673.3427328696, 457491.72804260254 3863784.0233612061, 457500.09862752369 3863661.4707939853, 457512.61054992676 3863660.735748291)))
         */

        private String 用地名称;
        private String ReArea;
        private String ogr_geometry;

        public String get用地名称() {
            return 用地名称;
        }

        public void set用地名称(String 用地名称) {
            this.用地名称 = 用地名称;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        public String getOgr_geometry() {
            return ogr_geometry;
        }

        public void setOgr_geometry(String ogr_geometry) {
            this.ogr_geometry = ogr_geometry;
        }

        @Override
        public String toString() {
            return "RowsBean{" +
                    "用地名称='" + 用地名称 + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    ", ogr_geometry='" + ogr_geometry + '\'' +
                    '}';
        }
    }
}
