package org.ros.android.android_tutorial_image_transport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Product> itemList;

    public RecyclerAdapter(ArrayList<Product> dataSet) {
        this.itemList = dataSet;
    }

    public void setItemList(ArrayList<Product> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void addItemList(Product item) {
        this.itemList.add(item);
        notifyDataSetChanged();
    }

    public Product getItemList(int id) {
        for (Product item : itemList) {
            if (id == item.getId())
                return item;
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView id;
        private final TextView name;
        private final TextView price;
        private final TextView amount;
        private final TextView total;

        public ViewHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.product_id);
            name = (TextView) itemView.findViewById(R.id.product_name);
            price = (TextView) itemView.findViewById(R.id.product_price);
            amount = (TextView) itemView.findViewById(R.id.product_amount);
            total = (TextView) itemView.findViewById(R.id.product_total);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = itemList.get(position);
        int id = position + 1;

        holder.id.setText(Integer.toString(id));
        holder.name.setText(product.getName());
        holder.price.setText(Integer.toString(product.getPrice()));
        holder.amount.setText(Integer.toString(product.getAmount()));
        holder.total.setText(Integer.toString(product.getTotal()));
    }

    @Override
    public int getItemCount() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }

}
