package com.yellowriver.skiff.Bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 多类型
 * @author huang
 */
public class MultipleItem implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    private int itemType;
    //
    private String Content;
    private String Image;

    public static int getTEXT() {
        return TEXT;
    }

    public static int getIMG() {
        return IMG;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public MultipleItem(int itemType) {
        this.itemType = itemType;
    }
    public MultipleItem() {

    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
