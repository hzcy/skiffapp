package com.yellowriver.skiff.Help;

import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * @author huang
 */
public class MyTagHandler implements TagHandler{
	private boolean first= true;
	private String parent="ul";
	private int index=1;
	
	private int sIndex = 0;

    @Override
	public void handleTag(boolean opening, String tag, Editable output,
	        XMLReader xmlReader) {

	    if("ul".equals(tag)) {
            parent="ul";
        } else if("ol".equals(tag)) {
            parent="ol";
        }
	    if("li".equals(tag)){
	        if("ul".equals(parent)){
	            if(first){
	                output.append("\n• ");
	                first= false;
	            }else{
	                first = true;
	            }
	        }
	        else{
	            if(first){
	                output.append("\n"+index+". ");
	                first= false;
	                index++;
	            }else{
	                first = true;
	            }
	        }   
	    }
	    
	    
	    if("bold".equals(tag)){
	    	System.out.println("tag" + tag);
	    	if (opening) {
                sIndex=output.length();
                System.out.println("sIndex" + sIndex);
            }else {
                int eIndex = output.length();
                System.out.println("eIndex" + eIndex);
                output.setSpan(new ForegroundColorSpan(Color.BLACK), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //output.setSpan(new SubscriptSpan(), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
	    }
	}
	}
