package appler.com.example.module_analysis.bean;

import java.util.List;

public class AnalysisData {


    private List<面积Bean> 面积;
    private List<区Bean> 区;
    private List<乡Bean> 乡;
    private List<村Bean> 村;
    private List<城市规划Bean> 城市规划;
    private List<现状地类Bean> 现状地类;
    private List<土地利用总体规划Bean> 土地利用总体规划;
    private List<建设用地管制区Bean> 建设用地管制区;

    public List<面积Bean> get面积() {
        return 面积;
    }

    public void set面积(List<面积Bean> 面积) {
        this.面积 = 面积;
    }

    public List<区Bean> get区() {
        return 区;
    }

    public void set区(List<区Bean> 区) {
        this.区 = 区;
    }

    public List<乡Bean> get乡() {
        return 乡;
    }

    public void set乡(List<乡Bean> 乡) {
        this.乡 = 乡;
    }

    public List<村Bean> get村() {
        return 村;
    }

    public void set村(List<村Bean> 村) {
        this.村 = 村;
    }

    public List<城市规划Bean> get城市规划() {
        return 城市规划;
    }

    public void set城市规划(List<城市规划Bean> 城市规划) {
        this.城市规划 = 城市规划;
    }

    public List<现状地类Bean> get现状地类() {
        return 现状地类;
    }

    public void set现状地类(List<现状地类Bean> 现状地类) {
        this.现状地类 = 现状地类;
    }

    public List<土地利用总体规划Bean> get土地利用总体规划() {
        return 土地利用总体规划;
    }

    public void set土地利用总体规划(List<土地利用总体规划Bean> 土地利用总体规划) {
        this.土地利用总体规划 = 土地利用总体规划;
    }

    public List<建设用地管制区Bean> get建设用地管制区() {
        return 建设用地管制区;
    }

    public void set建设用地管制区(List<建设用地管制区Bean> 建设用地管制区) {
        this.建设用地管制区 = 建设用地管制区;
    }

    @Override
    public String toString() {
        return "ZnfxBasicInfoData{" +
                "面积=" + 面积 +
                ", 区=" + 区 +
                ", 乡=" + 乡 +
                ", 村=" + 村 +
                ", 城市规划=" + 城市规划 +
                ", 现状地类=" + 现状地类 +
                ", 土地利用总体规划=" + 土地利用总体规划 +
                ", 建设用地管制区=" + 建设用地管制区 +
                '}';
    }

    public static class 面积Bean {
        /**
         * area : 3339906.94
         */

        private String area;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        @Override
        public String toString() {
            return "面积Bean{" +
                    "area='" + area + '\'' +
                    '}';
        }
    }

    public static class 区Bean {
        /**
         * XZQMC : 惠济区
         * amount : 1
         * Area : 222211164.97
         * ReArea : 3339906.75
         */

        private String XZQMC;
        private String amount;
        private String Area;
        private String ReArea;

        public String getXZQMC() {
            return XZQMC;
        }

        public void setXZQMC(String XZQMC) {
            this.XZQMC = XZQMC;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "区Bean{" +
                    "XZQMC='" + XZQMC + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }

    public static class 乡Bean {
        /**
         * XZQMC : 大河路街道办
         * amount : 1
         * Area : 22744051.37
         * ReArea : 3339837.15
         */

        private String XZQMC;
        private String amount;
        private String Area;
        private String ReArea;

        public String getXZQMC() {
            return XZQMC;
        }

        public void setXZQMC(String XZQMC) {
            this.XZQMC = XZQMC;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "乡Bean{" +
                    "XZQMC='" + XZQMC + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }

    public static class 村Bean {
        /**
         * XZQMC : 保合寨村
         * amount : 1
         * Area : 3339906.94
         * ReArea : 3339906.94
         */

        private String XZQMC;
        private String amount;
        private String Area;
        private String ReArea;

        public String getXZQMC() {
            return XZQMC;
        }

        public void setXZQMC(String XZQMC) {
            this.XZQMC = XZQMC;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "村Bean{" +
                    "XZQMC='" + XZQMC + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }

    public static class 城市规划Bean {
        /**
         * 用地名称 : 二类居住用地
         * amount : 15
         * Area : 774645.89
         * ReArea : 666251.50
         */

        private String 用地名称;
        private String amount;
        private String Area;
        private String ReArea;

        public String get用地名称() {
            return 用地名称;
        }

        public void set用地名称(String 用地名称) {
            this.用地名称 = 用地名称;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "城市规划Bean{" +
                    "用地名称='" + 用地名称 + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }

    public static class 现状地类Bean {
        /**
         * DLMC : 城市
         * amount : 15
         * Area : 344820.56
         * ReArea : 316059.60
         */

        private String 用地名称;
        private String amount;
        private String Area;
        private String ReArea;

        public String get用地名称() {
            return 用地名称;
        }

        public void set用地名称(String 用地名称) {
            this.用地名称 = 用地名称;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "现状地类Bean{" +
                    "DLMC='" + 用地名称 + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }

    public static class 土地利用总体规划Bean {
        /**
         * DLMC : 城市
         * amount : 12
         * Area : 342950.78
         * ReArea : 315571.18
         */

        private String 用地名称;
        private String amount;
        private String Area;
        private String ReArea;

        public String get用地名称() {
            return 用地名称;
        }

        public void set用地名称(String 用地名称) {
            this.用地名称 = 用地名称;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "土地利用总体规划Bean{" +
                    "DLMC='" + 用地名称 + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }

    public static class 建设用地管制区Bean {
        /**
         * YGZFQMC : 限制建设用地区
         * amount : 23
         * Area : 308583.93
         * ReArea : 263324.97
         */

        private String 用地名称;
        private String amount;
        private String Area;
        private String ReArea;

        public String get用地名称() {
            return 用地名称;
        }

        public void set用地名称(String 用地名称) {
            this.用地名称 = 用地名称;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getReArea() {
            return ReArea;
        }

        public void setReArea(String ReArea) {
            this.ReArea = ReArea;
        }

        @Override
        public String toString() {
            return "建设用地管制区Bean{" +
                    "YGZFQMC='" + 用地名称 + '\'' +
                    ", amount='" + amount + '\'' +
                    ", Area='" + Area + '\'' +
                    ", ReArea='" + ReArea + '\'' +
                    '}';
        }
    }
}
