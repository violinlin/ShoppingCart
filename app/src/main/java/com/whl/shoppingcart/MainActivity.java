package com.whl.shoppingcart;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.whl.shoppingcart.adapter.CartAdapter;
import com.whl.shoppingcart.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    ListView listView;
    CartAdapter adapter;
    List<CartItem> datas;
    TextView txtTotal;
    CheckBox chooseAll;
    boolean isEditMode = false;
    double sum = 0;
    Button editButton;
//    Test下更新有提交吗
    //    计算金额的观察者，观察adapter的更新
    private DataSetObserver sumObser = new DataSetObserver() {
//    当adapter的datasetChanged（）调用自动回调该方法

        @Override
        public void onChanged() {
//        TODO 计算总金额
            sum = 0;
            for (CartItem item : datas) {
                if (item.isChecked()) {
                    int count = item.getCount();
                    float price = item.getProductPrice();
                    sum += (count * price);
                }
            }
            txtTotal.setText("￥" + sum);

        }

        //当adapter调用notifyAdataSetInvalidate()方法调用时回调
        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.cart_list_viewID);
        txtTotal = (TextView) findViewById(R.id.cart_total_text);
        datas = new ArrayList<CartItem>();
        chooseAll = (CheckBox) findViewById(R.id.chooseAll);
        chooseAll.setOnCheckedChangeListener(this);
        editButton= (Button) findViewById(R.id.editButton);
        for (int i = 0; i < 30; i++) {
            CartItem item = new CartItem();
            item.setProductName("商品" + i);
            item.setProductPrice(10.0f);
            item.setCount(2);
            datas.add(item);
        }
        adapter = new CartAdapter(this, datas);
        /**
         * 有Activity监听ListView条目Checked内部的选中变化
         */
        adapter.setItemCheckedListener(this);
        adapter.setOnCountChangedListener(this);
        /**
         * 设置adapter的数据变换观察者，只要notifyDataSetChanged（）方法调用，观察者自动回调
         *
         */
        adapter.registerDataSetObserver(sumObser);
        listView.setAdapter(adapter);
//        首次启动计算购物车里的总金额
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击按钮切换编辑模式
     *
     * @param view
     */
    public void btnSwitchEditMode(View view) {
        isEditMode = !isEditMode;
        if (isEditMode){
            editButton.setText("清零");

        }else {
            chooseAll.setChecked(false);
            editButton.setText("编辑");
        }
        adapter.switchEditMode();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.chooseAll) {
            if (chooseAll.isChecked()) {
                for (CartItem item : datas) {
                    item.setChecked(true);
                }
                if (!isEditMode) {
                    btnSwitchEditMode(null);

                }
            } else {
                for (CartItem item : datas) {
                    item.setChecked(false);
                }
//                adapter.switchEditMode();
            }
        }else {
            Object tag = buttonView.getTag();
            if (tag != null && tag instanceof Integer) {
                int position = (Integer) tag;
//            Toast.makeText(this,"选中条目"+position+isChecked,Toast.LENGTH_SHORT).show();
                CartItem cartItem = datas.get(position);
//            数据状态改变不需要强制notifaction
                cartItem.setChecked(isChecked);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
//        点击接口用于listView内部的点击
        int id = v.getId();
        Object tag = v.getTag();
        int position = 0;
        if (tag != null && tag instanceof Integer) {
            position = (Integer) tag;
            CartItem item = datas.get(position);
            int c = item.getCount();
            switch (id) {
                case R.id.cart_item_inc:
                    c++;
                    break;
                case R.id.cart_item_dec:
                    c--;
                    break;
                case R.id.cart_item_del:
                    deleteProduct(position);
                    break;
            }
            if (c >= 0) {
                item.setCount(c);
                adapter.notifyDataSetChanged();
            } else {
//                对于小于1的情况，可以不处理也可以删除条目
            }
        }
    }
    public void deleteProduct(int position){
        datas.remove(position);
    }

    @Override
    protected void onDestroy() {
        adapter.unregisterDataSetObserver(sumObser);
        super.onDestroy();

    }

    /**
     * 调用支付宝DemoActivity进行实际的支付操作
     *
     * @param view
     */
    public void btnPay(View view) {
        if(sum<=0){
            Toast.makeText(this,"购物车为空，请添加商品",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PayDemoActivity.class);

        intent.putExtra("price", sum);
        intent.putExtra("subject", "自定义购物车的商品");
        intent.putExtra("body", "别着急，这只是模拟交易！！！");
//        TODO  传递订单信息，让payDemo 能够正确的显示
        startActivity(intent);

      
    }
}
