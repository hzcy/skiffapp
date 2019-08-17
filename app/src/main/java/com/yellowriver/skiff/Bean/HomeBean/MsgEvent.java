package com.yellowriver.skiff.Bean.HomeBean;

import java.util.Vector;

/**
 * 事件传递 目录
 * @author huang
 */
public class MsgEvent {
    private int id;
    private Vector<DataEntity> content;
    private int selIndex;

    @Override
    public String toString() {
        return "MsgEvent{" +
                "id=" + id +
                ", content='" + content.size() + '\'' +
                '}';
    }

    public MsgEvent(int id, Vector<DataEntity> content,int selIndex) {
        this.id = id;
        this.content = content;
        this.selIndex = selIndex;
    }

    public int getSelIndex() {
        return selIndex;
    }

    public void setSelIndex(int selIndex) {
        this.selIndex = selIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vector<DataEntity> getContent() {
        return content;
    }

    public void setContent(Vector<DataEntity> content) {
        this.content = content;
    }
}
