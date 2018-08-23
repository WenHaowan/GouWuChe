package as.bwei.com.gouwuche.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import as.bwei.com.gouwuche.R;
import as.bwei.com.gouwuche.bean.CartBean;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/21
 * Description:
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> implements CartCheckListener {

    private Context mContext;
    private List<CartBean.DataBean> cartList;
    private CartAllCheckboxListener allCheckboxListener;

    public CartAdapter(Context context, List<CartBean.DataBean> list) {
        mContext = context;
        cartList = list;

    }

    public void addPageData(List<CartBean.DataBean> list){
        if (cartList!=null){
            cartList.addAll(list);
            notifyDataSetChanged();
        }
    }

    //暴露给购物车页面进行回调
    public void setCartAllCheckboxListener(CartAllCheckboxListener cartAllCheckboxListener) {
        allCheckboxListener = cartAllCheckboxListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.cart_item_layout, parent, false);
        CartViewHolder viewHolder = new CartViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        final CartBean.DataBean bean = cartList.get(position);

        holder.nameTv.setText(bean.getSellerName());

        holder.checkBox.setChecked(bean.isSelected());//根据bean对象的ischecked属性，动态设置选中状态
//        System.out.println("ischecked:" + bean.isChecked());
//        holder.checkBox.setChecked(bean.isChecked());


        holder.productXRV.setLayoutManager(new LinearLayoutManager(mContext));
        final ProductAdapter productAdapter = new ProductAdapter(mContext, bean.getList());
        holder.productXRV.setAdapter(productAdapter);
        productAdapter.setCheckListener(this);

        for (int i = 0; i < bean.getList().size(); i++) {

            if (!bean.getList().get(i).isSelected()){
                holder.checkBox.setChecked(false);
                break;//跳出循环
            }else{
                holder.checkBox.setChecked(true);
            }



        }
        //设置商家的checkbox点击事件，逻辑：勾选则子列表全部勾选，取消则全部取消
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkBox.isChecked()){
                    bean.setSelected(true);

                    for (int i = 0; i < bean.getList().size(); i++) {
                        bean.getList().get(i).setSelected(true);
                    }

                }else{

                    bean.setSelected(false);
                    for (int i = 0; i < bean.getList().size(); i++) {
                        bean.getList().get(i).setSelected(false);
                    }
                }

                notifyDataSetChanged();

                if (allCheckboxListener!=null){
                    allCheckboxListener.notifyAllCheckboxStatus();
                }

            }
        });
    }


    /**
     * 暴露修改之后的最新的集合数据
     * @return
     */
    public List<CartBean.DataBean> getCartList() {
        return cartList;
    }

    @Override
    public int getItemCount() {
        return cartList.size() == 0 ? 0 : cartList.size();
    }

    /**
     * 刷新适配器的回调
     */
    @Override
    public void notifyParent() {

        notifyDataSetChanged();
        if (allCheckboxListener!=null){
            allCheckboxListener.notifyAllCheckboxStatus();
        }

    }


//    @Override
//    public void notifyParent() {
//        notifyDataSetChanged();
//    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView nameTv;
        private RecyclerView productXRV;

        public CartViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.sellerCheckbox);
            nameTv = (TextView) itemView.findViewById(R.id.sellerNameTv);
            productXRV = (RecyclerView) itemView.findViewById(R.id.productXRV);
        }
    }
}
