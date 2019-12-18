package appler.com.example.module_layer.layerTree.tree;

import java.util.List;

public class LayerBean {

    private List<AllLayerBean> All_Layer;

    public List<AllLayerBean> getAll_Layer() {
        return All_Layer;
    }

    public void setAll_Layer(List<AllLayerBean> All_Layer) {
        this.All_Layer = All_Layer;
    }

    public static class AllLayerBean {
        /**
         * content :
         * One_Layer : [{"content":"\t        ","Two_Layer":[{"Layer_Type":"","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"二层数据","Layer_SDName":"google_yx","Shp_BZKey":""},{"Layer_Type":"","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"二层数据","Layer_SDName":"hj_yx","Shp_BZKey":""},{"content":"\t\t\t\t\t\t","name":"二层类型","Three_Layer":[{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""},{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""}]},{"name":"二层类型","Three_Layer":[{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""}]}],"name":"一层类型"},{"Two_Layer":[{"Layer_Type":"","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"谷歌影像","Layer_SDName":"google_yx","Shp_BZKey":""},{"Layer_Type":"","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"惠济影像","Layer_SDName":"hj_yx","Shp_BZKey":""}],"name":"一层类型"}]
         */

        private String content;
        private List<OneLayerBean> One_Layer;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<OneLayerBean> getOne_Layer() {
            return One_Layer;
        }

        public void setOne_Layer(List<OneLayerBean> One_Layer) {
            this.One_Layer = One_Layer;
        }

        public static class OneLayerBean {
            /**
             * content :
             * Two_Layer : [{"Layer_Type":"","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"二层数据","Layer_SDName":"google_yx","Shp_BZKey":""},{"Layer_Type":"","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"二层数据","Layer_SDName":"hj_yx","Shp_BZKey":""},{"content":"\t\t\t\t\t\t","name":"二层类型","Three_Layer":[{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""},{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""}]},{"name":"二层类型","Three_Layer":[{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""}]}]
             * name : 一层类型
             */

            private String content;
            private String name;
            private List<TwoLayerBean> Two_Layer;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<TwoLayerBean> getTwo_Layer() {
                return Two_Layer;
            }

            public void setTwo_Layer(List<TwoLayerBean> Two_Layer) {
                this.Two_Layer = Two_Layer;
            }

            public static class TwoLayerBean {
                /**
                 * Layer_Type :
                 * name :
                 * IS_DianCha :
                 * Shp_Color :
                 * Layer_name : 二层数据
                 * Layer_SDName : google_yx
                 * Shp_BZKey :
                 * content :
                 * Three_Layer : [{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""},{"Layer_Type":"raster","name":"","IS_DianCha":"","Shp_Color":"","Layer_name":"三层数据","Layer_SDName":"hj_zgt","Shp_BZKey":""}]
                 */

                private String Layer_Type;
                private String name;
                private String IS_DianCha;
                private String Shp_Color;
                private String Layer_name;
                private String Layer_SDName;
                private String Shp_BZKey;
                private String LayerViewCenter;
                private String content;
                private List<ThreeLayerBean> Three_Layer;

                public String getLayer_Type() {
                    return Layer_Type;
                }

                public void setLayer_Type(String Layer_Type) {
                    this.Layer_Type = Layer_Type;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getIS_DianCha() {
                    return IS_DianCha;
                }

                public void setIS_DianCha(String IS_DianCha) {
                    this.IS_DianCha = IS_DianCha;
                }

                public String getShp_Color() {
                    return Shp_Color;
                }

                public void setShp_Color(String Shp_Color) {
                    this.Shp_Color = Shp_Color;
                }

                public String getLayer_name() {
                    return Layer_name;
                }

                public void setLayer_name(String Layer_name) {
                    this.Layer_name = Layer_name;
                }

                public String getLayer_SDName() {
                    return Layer_SDName;
                }

                public void setLayer_SDName(String Layer_SDName) {
                    this.Layer_SDName = Layer_SDName;
                }

                public String getShp_BZKey() {
                    return Shp_BZKey;
                }

                public void setShp_BZKey(String Shp_BZKey) {
                    this.Shp_BZKey = Shp_BZKey;
                }

                public String getLayerViewCenter() {
                    return LayerViewCenter;
                }

                public void setLayerViewCenter(String layerViewCenter) {
                    LayerViewCenter = layerViewCenter;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public List<ThreeLayerBean> getThree_Layer() {
                    return Three_Layer;
                }

                public void setThree_Layer(List<ThreeLayerBean> Three_Layer) {
                    this.Three_Layer = Three_Layer;
                }

                public static class ThreeLayerBean {
                    /**
                     * Layer_Type : raster
                     * name :
                     * IS_DianCha :
                     * Shp_Color :
                     * Layer_name : 三层数据
                     * Layer_SDName : hj_zgt
                     * Shp_BZKey :
                     */

                    private String Layer_Type;
                    private String name;
                    private String IS_DianCha;
                    private String Shp_Color;
                    private String Layer_name;
                    private String Layer_SDName;
                    private String Shp_BZKey;
                    private String LayerViewCenter;

                    public String getLayer_Type() {
                        return Layer_Type;
                    }

                    public void setLayer_Type(String Layer_Type) {
                        this.Layer_Type = Layer_Type;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getIS_DianCha() {
                        return IS_DianCha;
                    }

                    public void setIS_DianCha(String IS_DianCha) {
                        this.IS_DianCha = IS_DianCha;
                    }

                    public String getShp_Color() {
                        return Shp_Color;
                    }

                    public void setShp_Color(String Shp_Color) {
                        this.Shp_Color = Shp_Color;
                    }

                    public String getLayer_name() {
                        return Layer_name;
                    }

                    public void setLayer_name(String Layer_name) {
                        this.Layer_name = Layer_name;
                    }

                    public String getLayer_SDName() {
                        return Layer_SDName;
                    }

                    public void setLayer_SDName(String Layer_SDName) {
                        this.Layer_SDName = Layer_SDName;
                    }

                    public String getShp_BZKey() {
                        return Shp_BZKey;
                    }

                    public void setShp_BZKey(String Shp_BZKey) {
                        this.Shp_BZKey = Shp_BZKey;
                    }

                    public String getLayerViewCenter() {
                        return LayerViewCenter;
                    }

                    public void setLayerViewCenter(String layerViewCenter) {
                        LayerViewCenter = layerViewCenter;
                    }
                }
            }
        }
    }
}
