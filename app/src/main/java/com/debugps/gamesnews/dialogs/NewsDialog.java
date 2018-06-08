package com.debugps.gamesnews.dialogs;

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
import com.debugps.gamesnews.roomTools.POJO.New;
import com.squareup.picasso.Picasso;

public class NewsDialog extends DialogFragment {

    private New new_var;

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

        int color;

        do{
            color= MainActivity.getColorId();
        }while(color == R.color.MaterialBlueGrey900);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        View view = inflater.inflate(R.layout.news_dialog_layout, container, false);

        view.setMinimumWidth((int) (displayRectangle.width() * 0.98f));
        view.setMinimumHeight((int) (displayRectangle.height() * 0.98f));

        setResoursesUp(view);

        dialogImage.setBackgroundResource(color);

        if(new_var.getCoverImage().equals("")){
            dialogImage.setImageResource(R.drawable.ic_game_white);
            dialogImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else{
            Picasso.get().load(new_var.getCoverImage())
                    .error(R.drawable.ic_game_white)
                    .into(dialogImage);
            dialogImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        title.setText(new_var.getTitle());
        body.setText(new_var.getBody());
        game.setText(new_var.getGame());
        date.setText(new_var.getCreatedDate());

        return view;


    }

    public void setNew_var(New new_var) {
        this.new_var = new_var;
    }

    private void setResoursesUp(View view){
        dialogImage = view.findViewById(R.id.news_dialog_image);
        title = view.findViewById(R.id.news_dialog_title);
        body = view.findViewById(R.id.news_dialog_body);
        game = view.findViewById(R.id.news_dialog_game);
        date = view.findViewById(R.id.news_dialog_date);
    }
}
