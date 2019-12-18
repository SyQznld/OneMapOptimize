package appler.com.example.module_layer.property;

/**
 * 点查弹框 实体类显示 属性以及值
 */

public class PropertyData {
    private String name;
    private String param;

    public PropertyData() {
    }

    public PropertyData(String param, String name) {
        this.param = param;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getParam() {
        return param;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "Params{" +
                "name='" + name + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
