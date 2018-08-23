package as.bwei.com.gouwuche.adapter;

/**
 * Created by HP on 2018/8/22.
 */

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/21
 * Description:productAdapter，点击checkbox之后，通知cartAdapter进行刷新，目的是重新走步一遍onbindViewhodler
 */
public interface CartCheckListener {
    void notifyParent();//通知父adapter刷新适配器

}
