package as.bwei.com.gouwuche.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import as.bwei.com.gouwuche.R;
import as.bwei.com.gouwuche.bean.CartBean;
import as.bwei.com.gouwuche.widget.MyJIaJianView;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/21
 * Description:
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CartViewHolder> {

    private Context mContext;
    private List<CartBean.DataBean.ListBean> listBeanList;
    private CartCheckListener checkListener;//接口回调引用
    private CartAllCheckboxListener cartAllCheckboxListener;

    public ProductAdapter(Context context, List<CartBean.DataBean.ListBean> list) {
        mContext = context;
        listBeanList = list;
    }

    /**
     * 暴露给调用者去进行回调：对checklisener进行初始化
     * @param checkListener
     */
    public void setCheckListener(CartCheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public void setCartAllCheckboxListener(CartAllCheckboxListener cartAllCheckboxListener) {
        this.cartAllCheckboxListener = cartAllCheckboxListener;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.product_item_layout, parent, false);
        CartViewHolder viewHolder = new CartViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, int position) {

        final CartBean.DataBean.ListBean bean = listBeanList.get(position);

        holder.priceTv.setText("优惠价：¥" + bean.getBargainPrice());
        holder.titleTv.setText(bean.getTitle());
        String[] imgs = bean.getImages().split("\\|");//分割images，得到图片数组
        //校验数组大小是否>0，防止空指针
        if (imgs != null && imgs.length > 0) {

            Glide.with(mContext).load(imgs[0]).into(holder.productIv);
        } else {
            holder.productIv.setImageResource(R.mipmap.ic_launcher);
        }
        holder.checkBox.setChecked(bean.isSelected());

        holder.myJIaJianView.setNumEt(bean.getTotalNum());

        holder.myJIaJianView.setJiaJianListener(new MyJIaJianView.JiaJianListener() {
            @Override
            public void getNum(int num) {
                bean.setTotalNum(num);
                if (checkListener!=null){
                    checkListener.notifyParent();//通知一级列表适配器刷新
                }
            }
        });



        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {//选中
                    bean.setSelected(true);
                } else {//非选中
                    bean.setSelected(false);
                }
                if (checkListener!=null){
                    checkListener.notifyParent();//通知一级列表适配器刷新
                }

            }
        });

//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.checkBox.isChecked()){
//                    bean.setChecked(true);
//                }else{
//                    bean.setChecked(false);
//                }
//
//                checkListener.notifyParent();
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return listBeanList.size() == 0 ? 0 : listBeanList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView titleTv, priceTv;
        private ImageView productIv;
        private MyJIaJianView myJIaJianView;


        public CartViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.productCheckbox);
            titleTv = (TextView) itemView.findViewById(R.id.title);
            priceTv = (TextView) itemView.findViewById(R.id.price);
            productIv = (ImageView) itemView.findViewById(R.id.product_icon);
            myJIaJianView = (MyJIaJianView) itemView.findViewById(R.id.jiajianqi);

        }
    }
}
