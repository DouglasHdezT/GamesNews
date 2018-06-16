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

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GalleryDialog extends DialogFragment {

    private String urlImage;
    private ImageView image;
    private CircleImageView btn_dimiss;

    public static GalleryDialog newInstance(String urlImage){
        GalleryDialog dialog = new GalleryDialog();
        dialog.setUrlImage(urlImage);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(urlImage ==  null){
            GalleryDialog.this.dismiss();
            return null;
        }

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        View view = inflater.inflate(R.layout.gallery_dialog_layout, container, false);

        image = view.findViewById(R.id.gallery_dailog_image);
        btn_dimiss = view.findViewById(R.id.gallery_dialog_exit);

        image.setBackgroundResource(MainActivity.getColorId());

        Picasso.get().load(urlImage)
                .error(R.drawable.ic_game_white)
                .into(image);

        view.setMinimumWidth((int) (displayRectangle.width() * 0.90f));
        view.setMinimumHeight((int) (image.getHeight()));

        btn_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryDialog.this.dismiss();
            }
        });

        return view;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
