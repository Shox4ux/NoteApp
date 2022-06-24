package com.example.noteapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Model.Note;
import com.example.noteapp.NoteClickListener;
import com.example.noteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<NoteViewHolder> {
    Context context;
    List<Note> list;
    NoteClickListener listener;

    public Adapter(Context context, List<Note> list, NoteClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list,parent,false));
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.title.setSelected(true);

        holder.note.setText(list.get(position).getNotes());

        holder.date.setText(list.get(position).getData());
        holder.date.setSelected(true);


        if (list.get(position).isPinned()){
            holder.pinIcon.setImageResource(R.drawable.ic_star);
        }else {
            holder.pinIcon.setImageResource(0);
        }

        int colorCodes = Random();

        holder.container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCodes,null));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()),holder.container);
                return true;
            }
        });
    }



    private int Random(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.red);
        colorCode.add(R.color.green);
        colorCode.add(R.color.vague);
        colorCode.add(R.color.blue);
        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());


        return colorCode.get(randomColor);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Note> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

}
class NoteViewHolder extends RecyclerView.ViewHolder{
CardView container;
TextView title,note,date;
ImageView pinIcon;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.note_container);
        title = itemView.findViewById(R.id.text_tilte);
        note = itemView.findViewById(R.id.text_note);
        date = itemView.findViewById(R.id.text_date);
        pinIcon = itemView.findViewById(R.id.pin_icon);
    }
}
