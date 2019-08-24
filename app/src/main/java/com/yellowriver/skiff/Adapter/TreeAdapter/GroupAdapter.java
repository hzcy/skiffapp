package com.yellowriver.skiff.Adapter.TreeAdapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.yellowriver.skiff.Bean.SourcesBean.group;
import com.yellowriver.skiff.Bean.SourcesBean.sources;
import com.yellowriver.skiff.R;

import java.util.List;

/**
 * 可展开适配器
 * @author huang
 */
public class GroupAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {

    private static final String SEARCH = "search";
    private static final String HOME = "home";
    private static final String HAVE = "1";

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GroupAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.vertical_sourcegroup_item);
        addItemType(TYPE_LEVEL_1, R.layout.vertical_sourcelist_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final group group = (group) item;
                helper.setText(R.id.tv_title, group.getGroupName()+"("+group.getSourcess().size()+")");

                if (group.getGroupDate() == null) {
                    helper.getView(R.id.tv_date).setVisibility(View.GONE);
                }

                if (group.isExpanded()) {
                    helper.setImageResource(R.id.iv_del, R.drawable.ic_keyboard_arrow_down_black_24dp);
                } else {
                    helper.setImageResource(R.id.iv_del, R.drawable.ic_keyboard_arrow_right_black_24dp);
                }
                //表示本地源
                if (group.getGroupLink()==null)
                {
                    helper.setImageResource(R.id.iv_down,R.drawable.ic_close_black_24dp);

                }else
                {

                    //表示源以及导入 换成对勾图标
                    if (group.getGroupIshave()!=null)
                    {

                        if (HAVE.equals(group.getGroupIshave())) {
                            helper.setImageResource(R.id.iv_down, R.drawable.ic_done_black_24dp);


                        }else {
                            helper.setImageResource(R.id.iv_down,R.drawable.ic_vertical_align_bottom_black_24dp);

                        }
                    }else
                    {
                        helper.setImageResource(R.id.iv_down,R.drawable.ic_vertical_align_bottom_black_24dp);

                    }
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (group.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                helper.addOnClickListener(R.id.iv_down);
                break;
            case TYPE_LEVEL_1:
                final sources sources = (sources) item;

                helper.setText(R.id.tv_title, sources.getSourcesName());

                if (sources.getSourcesDate()==null||sources.getSourcesDate().equals(""))
                {
                    helper.getView(R.id.tv_date).setVisibility(View.GONE);
                }else
                {
                    helper.setText(R.id.tv_date, sources.getSourcesDate());

                }
                if (SEARCH.equals(sources.getSourcesType()))
                {
                    helper.setImageResource(R.id.iv_icon,R.drawable.ic_search_black_24dp);
                }else if(HOME.equals(sources.getSourcesType()))
                {
                    helper.setImageResource(R.id.iv_icon,R.drawable.ic_remove_red_eye_black_24dp);

                }
                //表示本地源
                if (sources.getSourcesLink()==null)
                {
                    helper.setImageResource(R.id.iv_down,R.drawable.ic_close_red_24dp2);

                }else
                {

                    //表示源以及导入 换成对勾图标
                    if (sources.getSourcesIshave()!=null)
                    {

                        if (HAVE.equals(sources.getSourcesIshave())) {
                            helper.setImageResource(R.id.iv_down, R.drawable.ic_done_black_24dp);


                        }else {
                            helper.setImageResource(R.id.iv_down,R.drawable.ic_vertical_align_bottom_black_24dp);

                        }
                    }else
                    {
                        helper.setImageResource(R.id.iv_down,R.drawable.ic_vertical_align_bottom_black_24dp);

                    }
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            holder.setImageResource(R.id.iv_down,R.drawable.ic_done_black_24dp);
        }
        //holder.setText(R.id.tv_title, group.getGroupName()+"("+group.getSourcess().size()+")");

    }
}
