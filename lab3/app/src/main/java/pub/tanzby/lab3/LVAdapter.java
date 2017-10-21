package pub.tanzby.lab3;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by tanzb on 2017/10/21 0021.
 */

public class LVAdapter extends BaseAdapter {
    private Context mContext;
    private List<Product> itemList;
    public LVAdapter(Context context) {
        itemList = new ArrayList<Product>();
        mContext = context;
    }
    public void removeData(int position) {
        itemList.remove(position);
        this.notifyDataSetChanged();
    }
    public void addData(Product product) {
        itemList.add(product);
        this.notifyDataSetChanged();
    }
    public void clearData()
    {
        itemList.clear();
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return itemList==null?0:itemList.size();
    }
    @Override
    public Product getItem(int position) {
        return itemList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_view_for_lv, null);
            holder = new ViewHolder();
            holder.mfirstText = (TextView) convertView.findViewById(R.id.id_ItemFirstChar);
            holder.mTextView = (TextView) convertView.findViewById(R.id.id_ItemFullName);
            holder.mPrice = (TextView) convertView.findViewById(R.id.id_ItemPrice);
            convertView.setTag(holder);
        }    else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mOnItemClickLitener != null)
        {
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(finalConvertView,position);
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(finalConvertView,position);
                    return false;
                }
            });
        }
        holder.mPrice.setText( itemList.get(position).getPrice() );
        holder.mTextView.setText( itemList.get(position).getName() );
        holder.mfirstText.setText(itemList.get(position).getName().substring(0,1).toUpperCase());
        return convertView;
    }

    interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private static class ViewHolder
    {
        TextView mTextView;
        TextView mfirstText;
        TextView mPrice;
    }
}
