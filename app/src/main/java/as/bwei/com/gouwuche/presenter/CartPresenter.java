package as.bwei.com.gouwuche.presenter;

import java.util.HashMap;

import as.bwei.com.gouwuche.bean.CartBean;
import as.bwei.com.gouwuche.model.CartModel;
import as.bwei.com.gouwuche.view.IcartView;

/**
 * Created by HP on 2018/8/22.
 */

public class CartPresenter {

    private CartModel cartModel;
    private IcartView icartView;

    public CartPresenter(IcartView icartView) {
        cartModel =new CartModel();
        attachView(icartView);

    }

    /**
     * 绑定view到presenter
     * @param icartView
     */
    public void attachView(IcartView icartView){
        this.icartView = icartView;
    }

    public void getCarts(HashMap<String,String> params, String url){

        cartModel.getCarts(params, url, new CartModel.CartCallback() {
            @Override
            public void success(CartBean cartBean) {

                if (icartView!=null){
                    icartView.success(cartBean);
                }

            }

            @Override
            public void fail(String msg) {

                if (icartView!=null){
                    icartView.failure(msg);
                }

            }
        });

    }

    /**
     * 解除绑定，把view和presenter解绑，避免内存泄漏
     */
    public void detachView(){
        this.icartView = null;
    }
}
