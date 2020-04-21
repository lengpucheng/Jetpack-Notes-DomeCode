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

import java.util.Random;

public class ThreeFragment extends Fragment {

    private ThreeViewModel mViewModel;
    private ImageView imageView;
    public static ThreeFragment newInstance() {
        return new ThreeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.three_fragment, container, false);
        imageView=view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ThreeViewModel.class);
        // TODO: Use the ViewModel
        imageView.setX(mViewModel.yd);
        final ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,"X",0,0);
        animator.setDuration(500);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!animator.isRunning()){
                    //随机生成一个Boolean
                    float yd=new Random().nextBoolean() ?100:-100;
                    animator.setFloatValues(imageView.getX(),imageView.getX()+yd);
                    mViewModel.yd+=yd;
                    animator.start();
                }
            }
        });
    }

}
