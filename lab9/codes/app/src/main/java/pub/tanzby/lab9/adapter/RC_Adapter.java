package pub.tanzby.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pub.tanzby.lab9.R;
import pub.tanzby.lab9.model.GitHub;

/**
 * Created by tan on 2017/12/12.
 */

public class RC_Adapter extends RecyclerView.Adapter<RC_Adapter.VH> {
    Context mContext;
    private List<GitHub> list_;

    public RC_Adapter(Context context){
        this.mContext=context;
        this.list_ = null;
    }

    public void addiItem(GitHub h)
    {
        list_.add(h);
        notifyDataSetChanged();
    }
    public void deleteItem(int posititon)
    {
        list_.remove(posititon);
        notifyDataSetChanged();
    }
    public void setNewList(List<GitHub> newlist)
    {
        list_ = newlist;
        notifyDataSetChanged();
    }
    public GitHub getItem(int position)
    {
        return list_.get(position);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH h = new VH(LayoutInflater
                .from(mContext)
                .inflate(R.layout.user_item_style, parent,false));
        return h;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final GitHub g = ((GitHub)list_.get(position));
        holder.title.setText(g.getTitle());
        holder.sub1.setText("id: "+g.getSub1());
        holder.sub2.setText("blog: "+g.getSub2());

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }});

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }});
        }
    }

    @Override
    public int getItemCount() {
        return list_!=null? list_.size():0;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView sub1;
        TextView sub2;
        public VH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            sub1   = itemView.findViewById(R.id.tv_sub_1);
            sub2 = itemView.findViewById(R.id.tv_sub_2);
        }
    }

    /*创建Item的点击接口*/
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
