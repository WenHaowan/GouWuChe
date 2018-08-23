package as.bwei.com.gouwuche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import as.bwei.com.gouwuche.adapter.CartAdapter;
import as.bwei.com.gouwuche.adapter.CartAllCheckboxListener;
import as.bwei.com.gouwuche.bean.CartBean;
import as.bwei.com.gouwuche.common.Constants;
import as.bwei.com.gouwuche.presenter.CartPresenter;
import as.bwei.com.gouwuche.view.IcartView;

public class CartActivity extends AppCompatActivity implements IcartView, CartAllCheckboxListener {

    private CartPresenter cartPresenter;
    private XRecyclerView xRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartBean.DataBean> list;
    private CheckBox allCheckbox;
    private TextView totalPriceTv;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();

        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {

        list = new ArrayList<>();
        xRecyclerView = (XRecyclerView) findViewById(R.id.cartGV);
        allCheckbox = (CheckBox) findViewById(R.id.allCheckbox);
        //设置线性布局管理器，listview的列表样式
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPriceTv = (TextView) findViewById(R.id.totalpriceTv);

        xRecyclerView.setLoadingMoreEnabled(true);//支持加载更多

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {//下拉刷新

                page = 1;
                loadData();//子线程
//                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {//加载更多
                page++;

                loadData();
//                xRecyclerView.loadMoreComplete();

            }
        });

        //设置点击事件
        allCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCheckbox.isChecked()) {//
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSelected(true);
                            for (int i1 = 0; i1 < list.get(i).getList().size(); i1++) {
                                list.get(i).getList().get(i1).setSelected(true);
                            }
                        }

                    }

                } else {
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSelected(false);
                            for (int i1 = 0; i1 < list.get(i).getList().size(); i1++) {
                                list.get(i).getList().get(i1).setSelected(false);
                            }
                        }

                    }
//                    totalPrice = 0;
                }

                cartAdapter.notifyDataSetChanged();//全部刷新

                totalPrice();

            }
        });


    }

    /**
     * 初始化数据
     */
    private void initData() {

        loadData();



    }

    /**
     * 请求数据
     */
    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uid", "71");
        params.put("page",page+"");

        cartPresenter = new CartPresenter(this);
        cartPresenter.getCarts(params, Constants.GETCARTS);
    }


    /**
     * 刷新购物车列表
     *
     * @param cartBean
     */
    @Override
    public void success(CartBean cartBean) {

        // TODO: 2018/8/21 展示列表数据

        if (cartBean != null && cartBean.getData() != null) {


            if (page==1){

                list = cartBean.getData();
                cartAdapter = new CartAdapter(this, list);

                xRecyclerView.setAdapter(cartAdapter);
                xRecyclerView.refreshComplete();//把下拉刷新的进度view隐藏掉
            }else {
                if (cartAdapter!=null){
                    cartAdapter.addPageData(cartBean.getData());
                }
                xRecyclerView.loadMoreComplete();//
            }


            cartAdapter.setCartAllCheckboxListener(this);

        }


    }

    /**
     * 失败提示
     *
     * @param msg
     */
    @Override
    public void failure(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 去结算
     *
     * @param view
     */
    public void buy(View view) {

    }

    /**
     * 全选按钮是否选中的回调
     */
    @Override
    public void notifyAllCheckboxStatus() {

        StringBuilder stringBuilder = new StringBuilder();
        if (cartAdapter != null) {
            for (int i = 0; i < cartAdapter.getCartList().size(); i++) {

                stringBuilder.append(cartAdapter.getCartList().get(i).isSelected());

                for (int i1 = 0; i1 < cartAdapter.getCartList().get(i).getList().size(); i1++) {

                    stringBuilder.append(cartAdapter.getCartList().get(i).getList().get(i1).isSelected());

                }
            }
        }

        System.out.println("sb=====" + stringBuilder.toString());

        //truetruefalsetruefalse

        if (stringBuilder.toString().contains("false")) {
            allCheckbox.setChecked(false);
//            totalPrice = 0;
        } else {
            allCheckbox.setChecked(true);
        }

        totalPrice();//计算总价

    }


    /**
     * 计算总价
     */
               private void totalPrice() {

                double totalPrice = 0;

                for (int i = 0; i < cartAdapter.getCartList().size(); i++) {

                    for (int i1 = 0; i1 < cartAdapter.getCartList().get(i).getList().size(); i1++) {

                        //计算总价的关键代码块
                        if (cartAdapter.getCartList().get(i).getList().get(i1).isSelected()) {
                            CartBean.DataBean.ListBean listBean = cartAdapter.getCartList().get(i).getList().get(i1);
                            totalPrice += listBean.getBargainPrice() * listBean.getTotalNum();
                        }


                    }
        }
        totalPriceTv.setText("总价：¥"+totalPrice);



    }
}
