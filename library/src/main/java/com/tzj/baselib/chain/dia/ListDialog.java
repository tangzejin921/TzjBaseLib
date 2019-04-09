package com.tzj.baselib.chain.dia;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.tzj.baselib.R;
import com.tzj.baselib.holder.DiaListHolder;
import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.TzjAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表dialog
 */
public class ListDialog extends BaseDialog{

    private Point point;
    private TzjRecyclerView recyclerView;
    private TzjAdapter.OnItemClickListener listener;
    private List mList = new ArrayList();

    public ListDialog(Context context) {
        super(context);
    }

    public ListDialog setList(List list) {
        mList.clear();
        mList.addAll(list);
        return this;
    }

    public ListDialog add(Object object){
        mList.add(object);
        return this;
    }

    public ListDialog setItemClickListener(final TzjAdapter.OnItemClickListener listener){
        this.listener = listener;
        return this;
    }


    @Override
    protected View createView() {
        return View.inflate(getContext(), R.layout.dia_list,null);
    }


    @Override
    protected void init() {
        super.init();
        setFromBottom();
        point = new Point(0,0);

        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);

        recyclerView = mRoot.findViewById(R.id.recyclerView);
        recyclerView.setDivider(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,getContext().getResources().getDisplayMetrics()));
        recyclerView.setDivider(false,false);

        recyclerView.setLineLayoutManager();
        if (mList.size() == 0 || (mList.size() > 0 && !(mList.get(0) instanceof IViewType))){
            recyclerView.setViewType(R.layout.item_dia_list,DiaListHolder.class);
        }
        recyclerView.setList(mList);
        recyclerView.setItemClickListener(new TzjAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TzjAdapter adapter, View v, int index, Object obj) {
                if (listener != null){
                    listener.onItemClick(adapter,v,index,obj);
                    dismiss();
                }
            }
        });

        findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //限制高度
        recyclerView.notifyDataSetChanged();
        ViewGroup.LayoutParams lp = mRoot.getLayoutParams();
        if (mList.size() >= 10){
            lp.height = point.y*2/3;
        }
        mRoot.setLayoutParams(lp);
    }

    /**
     * 确定的按键
     */
    public ListDialog setPositiveButton(String str, View.OnClickListener listener) {
        TextView viewById = mRoot.findViewById(R.id.sure);
        viewById.setText(str);
        viewById.setVisibility(View.VISIBLE);
        viewById.setOnClickListener(listener);
        return this;
    }


    public interface Show{
        /**
         *  ListDialog 显示的内容
         */
        String diaStr();
    }

}
