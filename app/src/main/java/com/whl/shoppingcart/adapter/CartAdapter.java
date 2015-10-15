package com.whl.shoppingcart.adapter;

/**
 * Author:whl
 * Email:294084532@qq.com
 * 2015/10/14
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.shoppingcart.R;
import com.whl.shoppingcart.model.CartItem;

import java.util.List;

/**
 * 基本的购物车列表适配器
 */
public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItems;
    /**
     * 代表当前listView的显示模式，0：普通模式，1：编辑模式
     */
    private int listMode;
    /**
     * 当某一个条目通过checkBox选中状态发生变化时回调的接口
     */
    private CompoundButton.OnCheckedChangeListener itemCheckedListener;
    /**
     * 数量调整按钮的处理
     */
    private View.OnClickListener onCountChangedListener;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    /**
     * 设置接口用于条目的选中
     *
     * @param itemCheckedListener
     */
    public void setItemCheckedListener(CompoundButton.OnCheckedChangeListener itemCheckedListener) {
        this.itemCheckedListener = itemCheckedListener;
    }

    public void setOnCountChangedListener(View.OnClickListener onCountChangedListener) {
        this.onCountChangedListener = onCountChangedListener;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (cartItems != null) {
            ret = cartItems.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
//        1.视图复用
        if (convertView != null) {
            ret = convertView;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            ret = inflater.inflate(R.layout.cart_item, parent, false);
        }
//        2.ViewHolder创建
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
//            TODO 设置Ui控件
            holder.checkBox = (CheckBox) ret.findViewById(R.id.cart_item_checkbox);
//            TODO 设置CheckBox 选中变换的事件，改变carItem的内容
//            adapter 传递事件监听
            holder.checkBox.setOnCheckedChangeListener(itemCheckedListener);

            holder.btnDec = ret.findViewById(R.id.cart_item_dec);
            holder.btnDelete = ret.findViewById(R.id.cart_item_del);
            holder.btnInc = ret.findViewById(R.id.cart_item_inc);
            holder.textCount = (TextView) ret.findViewById(R.id.cart_item_count);
            holder.txtProductName = (TextView) ret.findViewById(R.id.cart_item_product_name);
            holder.txtProductPrice = (TextView) ret.findViewById(R.id.cart_item_product_price);
            holder.imgIcon = (ImageView) ret.findViewById(R.id.cart_item_product_icon);

            holder.btnInc.setOnClickListener(onCountChangedListener);
            holder.btnDec.setOnClickListener(onCountChangedListener);
            holder.btnDelete.setOnClickListener(onCountChangedListener);
            ret.setTag(holder);
        }
//        3.获取数据
        CartItem cartItem = cartItems.get(position);

//        4.显示数据
        String pName = cartItem.getProductName();
        holder.txtProductName.setText(pName);
//        显示数量量
        int count = cartItem.getCount();
        holder.textCount.setText(count + "");
        float price = cartItem.getProductPrice();
        holder.txtProductPrice.setText(String.valueOf(price));
//        不论任何状态viewholdergetView时中的所有控件都必须从新设置与刷新
        holder.checkBox.setVisibility(View.VISIBLE);
//        根据模式显示CheckBOx
//        设置按钮的tag,获取当前的位置
        holder.checkBox.setTag(position);
        holder.btnInc.setTag(position);
        holder.btnDec.setTag(position);
        holder.btnDelete.setTag(position);
        if (cartItem.getCount() <= 1) {
            holder.btnDec.setEnabled(false);
        } else {
            holder.btnDec.setEnabled(true);
        }
//        boolean isCecked = cartItem.isChecked();
//        holder.btnDelete.setVisibility(View.VISIBLE);
        if (listMode == 1) {
//            编辑模式可见
//            checkbox 是否选中依赖于cartItem的变量，
            boolean isCecked = cartItem.isChecked();
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(isCecked);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else if (listMode == 0) {
//            非编辑模式，CheckBox取消选中，
//            注意：数据是实体中的check也应该设置成false；
            holder.btnDelete.setVisibility(View.INVISIBLE);
            holder.checkBox.setChecked(false);
//            checkBox监听事件里设置
            cartItem.setChecked(false);
            holder.checkBox.setVisibility(View.GONE);

        }
        return ret;

    }

    /**
     * 切换内部变量，进行编辑模式的切换
     * 因为listView 显示内容的变化需要，使用getView方法
     * 那么切换模式的时候，让adapter进行notifyDataSetChanged();
     * 强制触发getView();
     */
    public void switchEditMode() {
        if (listMode == 1) {
            listMode = 0;
        } else if (listMode == 0) {
            listMode = 1;
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public CheckBox checkBox;
        public ImageView imgIcon;
        public TextView txtProductName;
        public TextView txtProductPrice;
        //        加号
        public View btnInc;
        //        数量
        public TextView textCount;
        //        减号
        public View btnDec;
        //        删除
        public View btnDelete;

    }
}
