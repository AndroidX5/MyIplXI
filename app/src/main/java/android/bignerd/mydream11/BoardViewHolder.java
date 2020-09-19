package android.bignerd.mydream11;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class BoardViewHolder extends RecyclerView.ViewHolder{
    private TextView teamTextView;
    private TextView battingScoreView;
    private TextView bowlingScoreTextView;
    private TextView fieldingScoreTextView;
    private TextView scoreTextView;

    public BoardViewHolder(@NonNull View itemView) {
        super(itemView);
        teamTextView = itemView.findViewById(R.id.nameText);
        battingScoreView = itemView.findViewById(R.id.battingScoreText);
        bowlingScoreTextView = itemView.findViewById(R.id.bowlingScoreText);
        fieldingScoreTextView = itemView.findViewById(R.id.FieldingScoreText);
        scoreTextView = itemView.findViewById(R.id.scoreText);
    }

    public void bind(ScoreModel model) {
        teamTextView.setText(model.getName());
        battingScoreView.setText(model.getBattingScore());
        bowlingScoreTextView.setText(model.getBowlingScore());
        fieldingScoreTextView.setText(model.getFieldingScore());
        scoreTextView.setText(model.getScore());
    }
}