package com.openclassrooms.entrevoisins;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedProfile extends AppCompatActivity
{
    //référencement et déclaration de l'Api & star button
    private FloatingActionButton mStarButton;
    private NeighbourApiService mApiService;
    boolean isFavorite;

    @BindView(R.id.item_list_avatar) ImageView avatar;
    @BindView(R.id.firstName) TextView firstName;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.phoneNumber) TextView phoneNumber;
    @BindView(R.id.facebook) TextView facebook;
    @BindView(R.id.about_title) TextView aboutTitle;
    @BindView(R.id.about_text) TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        Intent i = getIntent();
        Neighbour neighbour = i.getParcelableExtra("neighbourInfo");

        long idAvatar = neighbour.getId();
        String avatarImage = neighbour.getAvatarUrl();
        Glide.with(this)
                .load(avatarImage)
                .into(avatar);

        String name = neighbour.getName();
        firstName.setText(name);
        address.setText(neighbour.getAddress());
        phoneNumber.setText(neighbour.getPhoneNumber());
        facebook.setText(neighbour.getFacebook() + name);
        aboutTitle.setText("À propos de moi");
        aboutText.setText(neighbour.getAboutMe());
        isFavorite = neighbour.getIsFavorite();
        mApiService = DI.getNeighbourApiService();

        //branchement du favorite button
        mStarButton = findViewById(R.id.star_button);

        //étoile par défaut sans clic en fonction de l'état cad favori ou non
        if (!isFavorite)
        {
            mStarButton.setImageResource(R.drawable.ic_star_border_white_24dp);
        }
        else
        {
            mStarButton.setImageResource(R.drawable.ic_star_white_24dp);
        }

        mStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //ajouter les favoris si booleen est false et passer le booleen à true
                if (!isFavorite)
                {
                    mStarButton.setImageResource(R.drawable.ic_star_white_24dp);
                    mApiService.changeFavorite(idAvatar, true);
                    isFavorite = true;
                    Snackbar.make(view, name + " a éte ajouté(e) aux favoris", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    mStarButton.setImageResource(R.drawable.ic_star_border_white_24dp);
                    mApiService.changeFavorite(idAvatar, false);
                    isFavorite = false;
                    Snackbar.make(view, name + " a éte supprimé(e) des favoris", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
