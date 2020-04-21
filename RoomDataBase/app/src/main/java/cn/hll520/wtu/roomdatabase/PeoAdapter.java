package cn.hll520.wtu.roomdatabase;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PeoAdapter extends ListAdapter<People, PeoAdapter.peoViewHolder> {
    boolean useCardView;

    public PeoAdapter(boolean useCardView) {
        super(new DiffUtil.ItemCallback<People>() {
            //比较元素是否相同
            @Override
            public boolean areItemsTheSame(@NonNull People oldItem, @NonNull People newItem) {
                //直接比较id是否相同，相同就是同一个元素
                return oldItem.get_id() == newItem.get_id();
            }

            //比较内容是否相同
            @Override
            public boolean areContentsTheSame(@NonNull People oldItem, @NonNull People newItem) {
                //比较内容
                boolean flag = (oldItem.getCollege().equals(newItem.getCollege()) && oldItem.getName().equals(newItem.getName()));
                return flag;
            }
        });
        this.useCardView = useCardView;
    }


    //构造组件
    @NonNull
    @Override
    public peoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //定义一个构造
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //创建卡片  参数——————  布局，组件组，是否是根节点
        View itemView;
        if (useCardView)
            itemView = layoutInflater.inflate(R.layout.cell_card, parent, false);
        else
            itemView = layoutInflater.inflate(R.layout.cell_normal, parent, false);
        //返回组件卡片
        final peoViewHolder holder = new peoViewHolder(itemView);
        //添加点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.baidu.com/s?wd=" + holder.name.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });


        return holder;
    }

    //绑定组件
    @Override
    public void onBindViewHolder(@NonNull final peoViewHolder holder, int position) {
        People people = getItem(position);
        holder.id.setText(String.valueOf(position + 1));
        holder.name.setText(people.getName());
        holder.college.setText(people.getCollege());

    }



    //创建内部ViewHolder
    static class peoViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, college;

        public peoViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.cell_id);
            name = itemView.findViewById(R.id.cell_name);
            college = itemView.findViewById(R.id.cell_college);
        }
    }
}
