package com.yellowriver.skiff.Adapter.TreeAdapter;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.entity.node.NodeFooterImp;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootNode  extends BaseNode implements NodeFooterImp {


    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }

    @Nullable
    @Override
    public BaseNode getFooterNode() {
        return null;
    }
}
