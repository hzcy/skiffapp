package com.yellowriver.skiff.View.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.MenuAdapter;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.SimpAdapter;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.DataUtils.LocalUtils.MyImportSharedPreferences;
import com.yellowriver.skiff.Help.LogUtil;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.MyImportActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListSaveFragment extends Fragment {



    RecyclerView menuList;
    private String type;
    private String title;
    private String xpath;
    private SimpAdapter mSimpAdapter;
    AlertDialog dlg;

    public ListSaveFragment(String title, String type, String xpath, AlertDialog dlg) {
        // Required empty public constructor
        this.type = type;
        this.title = title;
        this.xpath = xpath;
        this.dlg = dlg;
    }

    private List<String> textList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_save, container, false);
        menuList = v.findViewById(R.id.menu_list);
        MyLinearLayoutManager myLinearLayoutManagerMenu;
        myLinearLayoutManagerMenu = new MyLinearLayoutManager(getActivity());
        menuList.setLayoutManager(myLinearLayoutManagerMenu);
        menuList.setItemAnimator(new DefaultItemAnimator());
        menuList.setAdapter(mSimpAdapter = new SimpAdapter(R.layout.myimportsim_item));




        if (type.equals("??????")) {
            textList = new ArrayList<>();
            textList.add("????????????");
            textList.add("????????????");
            textList.add("????????????");
        } else if(type.equals("??????")){
            textList = new ArrayList<>();
            textList.add("??????????????????");
            textList.add("???????????????");
        }else{
            textList = new ArrayList<>();
            textList.add("????????????");
        }
        mSimpAdapter.setNewData(textList);
        mSimpAdapter.setOnItemClickListener((adapter, view, position) -> {

            final String selecttitle = (String) adapter.getData().get(position);
            LogUtil.info("??????????????????","??????"+title+"????????????"+selecttitle+"xpath"+xpath);

            switch (selecttitle){
                case "????????????":
                    MyImportSharedPreferences.writeTitle(title,xpath,getContext());
                    break;
                case "????????????":
                    MyImportSharedPreferences.writeSummary(title,xpath,getContext());
                    break;
                case "????????????":
                    MyImportSharedPreferences.writeDate(title,xpath,getContext());
                    break;
                case "??????????????????":
                    MyImportSharedPreferences.writeLink(title,xpath,getContext());
                    break;
                case "???????????????":
                    MyImportSharedPreferences.writeNextPage(title,xpath,getContext());
                    break;
                case "????????????":
                    MyImportSharedPreferences.writeCover(title,xpath,getContext());
                    break;

            }

            Toast.makeText(getActivity(), "???????????????", Toast.LENGTH_SHORT).show();
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("savetag");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }
            if(dlg!=null){
                dlg.dismiss();
            }
        });
        return v;
    }

}
