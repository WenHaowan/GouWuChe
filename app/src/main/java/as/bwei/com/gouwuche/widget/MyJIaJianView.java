package as.bwei.com.gouwuche.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import as.bwei.com.gouwuche.R;

import static java.security.AccessController.getContext;

/**
 * Author:kson
 * E-mail:19655910@qq.com
 * Time:2018/08/22
 * Description:自定义加减器
 */
public class MyJIaJianView extends LinearLayout{

    private TextView jiaTv,jiantv;
    private EditText numEt;
    private int num = 1;
    public MyJIaJianView(Context context) {
        this(context,null);
    }

    public MyJIaJianView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyJIaJianView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化自定义的布局
     */
    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.jia_jian_layout,this,true);
//        addView(view);
        jiantv = (TextView) view.findViewById(R.id.jian);
        jiaTv = (TextView) view.findViewById(R.id.jia);
        numEt = (EditText) view.findViewById(R.id.num);



        numEt.setText(num+"");



        jiaTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                numEt.setText(num+"");
                if (jiaJianListener!=null){
                    jiaJianListener.getNum(num);
                }
            }
        });
        jiantv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num--;
                if (num<=0){
                    Toast.makeText(getContext(), "数量不能小于1", Toast.LENGTH_SHORT).show();
                    num = 1;
                }

                numEt.setText(num+"");

                if (jiaJianListener!=null){
                    jiaJianListener.getNum(num);
                }



            }
        });

    }

    /**
     * 设置editext数量
     * @param
     */
    public void setNumEt(int n) {
        numEt.setText(n+"");
        num = Integer.parseInt(numEt.getText().toString());
    }

    private JiaJianListener jiaJianListener;

    public void setJiaJianListener(JiaJianListener jiaJianListener) {
        this.jiaJianListener = jiaJianListener;
    }

    public interface JiaJianListener{
        void getNum(int num);
    }

}
