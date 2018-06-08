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
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.Player;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerDialog extends DialogFragment {

    private TextView name;
    private TextView bio;
    private TextView game;

    private ImageView image;

    private Player player;

    public PlayerDialog() {
    }

    public static PlayerDialog newInstance(Player player){
        PlayerDialog dialog = new PlayerDialog();

        dialog.setPlayer(player);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(player ==  null){
            PlayerDialog.this.dismiss();
            return null;
        }

        int colorP;

        do{
            colorP= MainActivity.getColorId();
        }while(colorP == R.color.MaterialBlueGrey900);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        View view = inflater.inflate(R.layout.player_dialog_layout, container, false);

        view.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        view.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

        setResourcesUp(view);

        image.setBackgroundResource(colorP);

        if(!player.getAvatar().equals("")){
            Picasso.get().load(player.getAvatar())
                    .error(R.drawable.ic_person)
                    .into(image);
        }
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        name.setText(player.getName());
        bio.setText(player.getBiografia());
        //game.setText(player.getGame());

        return view;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void setResourcesUp(View view){
        name = view.findViewById(R.id.player_dialog_name);
        bio = view.findViewById(R.id.player_dialog_bio);
        game = view.findViewById(R.id.player_dialog_game);
        image = view.findViewById(R.id.player_dialog_image);
    }
}
