package as.bwei.com.gouwuche.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/21
 * Description:
 */
public class SpaceItemDecoration  extends RecyclerView.ItemDecoration{

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
