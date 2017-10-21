package pub.tanzby.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by tanzb on 2017/10/20 0020.
 */

public class RCAdapter extends RecyclerView.Adapter<RCAdapter.VH> {

    private Context mContext;
    private List<Product> itemList;

    public void removeData(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }
    public Product getItems(int position)
    {
        return itemList.get(position);
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        VH h = new VH(LayoutInflater.from(mContext).inflate(R.layout.items_view_for_rc, parent,false));
        return h;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.mTextView.setText( itemList.get(position).getName() );
        holder.mfirstText.setText(itemList.get(position).getName().substring(0,1).toUpperCase());

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return itemList == null?0:itemList.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView mfirstText;
        public VH(View itemView) {
            super(itemView);
            mfirstText= (TextView)itemView.findViewById(R.id.id_ItemFirstChar);
            mTextView = (TextView)itemView.findViewById(R.id.id_ItemFullName);
        }
    }

    public RCAdapter(Context context,List<Product> itemList_) {
        itemList = itemList_;
        mContext = context;
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
