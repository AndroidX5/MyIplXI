package android.bignerd.mydream11;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {

    interface OnItemClickListener {
        void onItemClicked(ScoreModel model);
    }

    private List<ScoreModel> scoreModels = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    BoardAdapter(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BoardViewHolder holder, int position) {
        if (position == 0) {
            holder.bind(new ScoreModel("Team", "Bat", "Bowl", "Field", "11",  "Total"));
        } else {
            holder.bind(scoreModels.get(holder.getAdapterPosition() - 1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(scoreModels.get(holder.getAdapterPosition() - 1));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return scoreModels.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    void setData(List<ScoreModel> models) {
        scoreModels.clear();
        scoreModels.addAll(models);
        notifyDataSetChanged();
    }
}
