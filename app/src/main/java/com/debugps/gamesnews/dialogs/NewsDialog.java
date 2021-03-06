package com.debugps.gamesnews.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.R;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsDialog extends DialogFragment {

    private New new_var;

    private MainTools tools;

    private CircleImageView exit_btn;
    private CircleImageView fav_btn;
    private ImageView dialogImage;
    private TextView title;
    private TextView body;
    private TextView game;
    private TextView date;

    public NewsDialog() {
    }

    public static NewsDialog newInstace(New new_var){
        NewsDialog dialog =  new NewsDialog();

        dialog.setNew_var(new_var);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(new_var ==  null){
            NewsDialog.this.dismiss();
            return null;
        }

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        View view = inflater.inflate(R.layout.news_dialog_layout, container, false);

        view.setMinimumWidth((int) (displayRectangle.width() * 0.98f));
        view.setMinimumHeight((int) (displayRectangle.height() * 0.98f));

        setResoursesUp(view);

        dialogImage.setBackgroundResource(new_var.getColorId());

        if(new_var.getCoverImage().equals("")){
            dialogImage.setImageResource(R.drawable.ic_game_white);
            dialogImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else{
            Picasso.get().load(new_var.getCoverImage())
                    .error(R.drawable.ic_game_white)
                    .into(dialogImage);
            dialogImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        if(new_var.getFavorited() == 1){
            fav_btn.setCircleBackgroundColorResource(R.color.loginBackground);
            fav_btn.setImageResource(R.drawable.ic_fav_red);
        }else{
            fav_btn.setCircleBackgroundColorResource(R.color.Trans);
            fav_btn.setImageResource(R.drawable.ic_fav_white);
        }

        title.setText(new_var.getTitle());
        body.setText(new_var.getBody());
        game.setText(new_var.getGame());
        date.setText(new_var.getCreatedDate());

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDialog.this.dismiss();
            }
        });

        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new_var.getFavorited() == 1){
                    new_var.setFavorited(0);
                    fav_btn.setImageResource(R.drawable.ic_fav_white);
                    tools.unsetFavorited(new_var.get_id());
                }else{
                    new_var.setFavorited(1);
                    fav_btn.setImageResource(R.drawable.ic_fav_red);
                    tools.setFavorited(new_var.get_id());
                }
            }
        });

        return view;


    }

    public void setNew_var(New new_var) {
        this.new_var = new_var;
    }

    private void setResoursesUp(View view){
        exit_btn =  view.findViewById(R.id.news_dialog_exit);
        fav_btn =  view.findViewById(R.id.news_dialog_fav);
        dialogImage = view.findViewById(R.id.news_dialog_image);
        title = view.findViewById(R.id.news_dialog_title);
        body = view.findViewById(R.id.news_dialog_body);
        game = view.findViewById(R.id.news_dialog_game);
        date = view.findViewById(R.id.news_dialog_date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainTools){
            tools = (MainTools) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools = null;
    }
}
