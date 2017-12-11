package pub.tanzby.lab8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tanzb on 2017/10/21 0021.
 */

public class contactAdapter extends BaseAdapter {
    private Context mContext;
    private List<contactItem> itemList;
    public contactAdapter(Context context) {
        itemList = null;
        mContext = context;
    }
    public void UpdateData(List<contactItem> itemList)
    {
        this.itemList = itemList;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return itemList==null?0:itemList.size();
    }
    @Override
    public contactItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.list_item_style, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.header_name);
            holder.birthday = (TextView) convertView.findViewById(R.id.item_birthday);
            holder.gift = (TextView) convertView.findViewById(R.id.header_gift);
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
        holder.name.setText(itemList.get(position).name);
        holder.birthday.setText( itemList.get(position).birthday);
        holder.gift.setText( itemList.get(position).gift );
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
        TextView name;
        TextView birthday;
        TextView gift;
    }
}