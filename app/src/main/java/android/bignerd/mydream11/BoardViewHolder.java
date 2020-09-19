package android.bignerd.mydream11;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class BoardViewHolder extends RecyclerView.ViewHolder{
    private TextView teamTextView;
    private TextView scoreTextView;

    public BoardViewHolder(@NonNull View itemView) {
        super(itemView);
        teamTextView = itemView.findViewById(R.id.nameText);
        scoreTextView = itemView.findViewById(R.id.scoreText);
    }

    public void bind(ScoreModel model) {
        teamTextView.setText(model.getName());
        scoreTextView.setText(String.valueOf(model.getScore()));
    }
}