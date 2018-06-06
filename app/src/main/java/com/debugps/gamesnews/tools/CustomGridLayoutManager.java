package com.debugps.gamesnews.tools;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class CustomGridLayoutManager extends GridLayoutManager {

    public CustomGridLayoutManager(Context context) {
        super(context, 9);

        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position%5 == 0){
                    return 9;
                }else{
                    if(position%5 == 1){
                        if(position%2 == 0){
                            return 4;
                        }else{
                            return 5;
                        }
                    }else if(position%5 == 2){
                        if(position%2 == 0){
                            return 4;
                        }else{
                            return 5;
                        }
                    }else if(position%5 == 3){
                        if(position%2 == 0){
                            return 5;
                        }else{
                            return 4;
                        }
                    }else{
                        if(position%2 == 0){
                            return 5;
                        }else{
                            return 4;
                        }
                    }
                }
            }
        });
    }
}
