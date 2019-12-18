package com.oneMap.module.common.utils;

public class XmlToJson {
    public static String convertXmlToJson(String xml) {
        fr.arnaudguyon.xmltojsonlib.XmlToJson xmlToJson = new fr.arnaudguyon.xmltojsonlib.XmlToJson.Builder(xml)
                .build();
        return alterJson(xmlToJson.toString());
    }

    private static String alterJson(String json) {
        String result = "";
        result = json.replace("{", "[{");
        json = result.replace("}", "}]");
        result = json.replace("}]]", "}]");
        json = result.replace("[[{", "[{");
        result = json.replace("}],[{", "},{");
        json = result.substring(1, result.length() - 1);
        return json;
    }
}
