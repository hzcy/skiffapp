package com.yellowriver.skiff.Bean.SourcesBean;

/**
 * @author huang
 */
public class SourcesEntity {


    /**
     * name : 测试
     * type : home
     * link : /测试.txt
     * data :
     */

    private String name;
    private String type;
    private String link;
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = date;
    }
}
