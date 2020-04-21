package cn.hll520.wtu.bottomnavigation;

import androidx.lifecycle.ViewModelProviders;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class OneFragment extends Fragment {
    private ImageView imageView;
    private OneViewModel mViewModel;

    public static OneFragment newInstance() {
        return new OneFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.one_fragment, container, false);
       imageView=view.findViewById(R.id.imageView);
       return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OneViewModel.class);
        //读取初始角度
        imageView.setRotation(mViewModel.Rotation);
        //定义属性动画  参数   ——对象，属性（旋转），关键帧
        final ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"rotation",0,0);
        //设置时间
        animator.setDuration(500);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否在运动，如果在就不执行
                if(!animator.isRunning()){
                //设置关键帧（开始和结束），取得角度，在角度上+100
                animator.setFloatValues(imageView.getRotation(),imageView.getRotation()+100);
                //记录数据
                    mViewModel.Rotation+=100;
                //启动效果
                animator.start();
                }
            }
        });
    }

}
