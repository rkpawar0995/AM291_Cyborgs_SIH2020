package com.example.mdmscheme.Common;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.mdmscheme.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodData> myFoodList;
    private int lastPosition = -1;

    public MyAdapter(Context mContext, List<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row,viewGroup,false);
        return new FoodViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder foodViewHolder, int i) {


        Glide.with(mContext)
                .load(myFoodList.get(i).getPhoto())
                .into(foodViewHolder.photo);

        TextView time,location,type,name;

        // foodViewHolder.time.setText(myFoodList.get(i).getTime());
        foodViewHolder.location.setText(myFoodList.get(i).getLocation());
        // foodViewHolder.type.setText(myFoodList.get(i).getType());
        foodViewHolder.name.setText(myFoodList.get(i).getName());

        foodViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Info.class);
                intent.putExtra("count",myFoodList.get(foodViewHolder.getAdapterPosition()).getName());
                intent.putExtra("snapshot",myFoodList.get(foodViewHolder.getAdapterPosition()).getDec());
                intent.putExtra("timespan",myFoodList.get(foodViewHolder.getAdapterPosition()).getPhoto());
                mContext.startActivity(intent);

            }
        });


        setAnimation(foodViewHolder.itemView,i);

    }


    public void setAnimation(View viewToAnimate, int position ){

        if(position> lastPosition){

            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;

        }

    }

    @Override
    public int getItemCount() { return myFoodList.size(); }


    public void filteredList(ArrayList<FoodData> filterList) {

        myFoodList = filterList;
        notifyDataSetChanged();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{



    TextView time,location,type,name;
    CardView mCardView;
    ImageView photo;


    public FoodViewHolder( View itemView) {
        super(itemView);

        //   photoView =itemView.findViewById(R.id.ivImage);
        time = itemView.findViewById(R.id.time);
        location = itemView.findViewById(R.id.location);
        type = itemView.findViewById(R.id.type);
        name = itemView.findViewById(R.id.name);
        photo = itemView.findViewById(R.id.photo);
        mCardView = itemView.findViewById(R.id.myCardView);
    }
}