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

public class TwoFragment extends Fragment {
    private ImageView imageView;
    private TwoViewModel mViewModel;

    public static TwoFragment newInstance() {
        return new TwoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.two_fragment, container, false);
        imageView=view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TwoViewModel.class);
        // TODO: Use the ViewModel

        imageView.setScaleX(mViewModel.ScaleX);
        imageView.setScaleY(mViewModel.ScaleY);
        //缩放
        final ObjectAnimator animatorX=ObjectAnimator.ofFloat(imageView,"scaleX",0,0);
        final ObjectAnimator animatorY=ObjectAnimator.ofFloat(imageView,"scaleY",0,0);
        animatorX.setDuration(500);
        animatorY.setDuration(500);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否在运动，如果在就不执行
                if(!animatorX.isRunning()){
                    //设置关键帧（开始和结束），取得角度，在角度上+100
                    animatorX.setFloatValues(imageView.getScaleX(),imageView.getScaleX()+0.1f);
                    animatorY.setFloatValues(imageView.getScaleY(),imageView.getScaleY()+0.1f);
                    //记录数据
                    mViewModel.ScaleX+=0.1;
                    mViewModel.ScaleY+=0.1;
                    //启动效果
                    animatorX.start();
                    animatorY.start();
                }
            }
        });
    }

}
