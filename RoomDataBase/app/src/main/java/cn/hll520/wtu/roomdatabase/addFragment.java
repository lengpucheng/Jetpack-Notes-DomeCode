package cn.hll520.wtu.roomdatabase;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;



/**
 * A simple {@link Fragment} subclass.
 */
public class addFragment extends Fragment {
    private Button button;
    private EditText addName;
    private EditText addCollege;
    private PeopleViewModel viewModel;
    private String college;
    private String name;
    private InputMethodManager imm;

    public addFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity).get(PeopleViewModel.class);
        button = activity.findViewById(R.id.button);
        addName = activity.findViewById(R.id.addName);
        addCollege = activity.findViewById(R.id.addCollege);
        //没输入时候是不可以点击
        button.setEnabled(false);
        //__________自动弹出键盘————————————
        //设置默认焦点
        addName.requestFocus();
        //获取键盘对象
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //显示键盘  参数   在那个界面上，标注
        assert imm != null;
        imm.showSoftInput(addName, 0);

        //——————————设置输入框监听————————————


        TextWatcher textWatcher = new TextWatcher() {
            //改变前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //内容改变时
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = addName.getText().toString().trim();
                college = addCollege.getText().toString().trim();
                //启用按键，当名字和学院都不为空时
                button.setEnabled(!name.isEmpty() && !college.isEmpty());

            }

            //改变后
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        //给输入框添加监听
        addName.addTextChangedListener(textWatcher);
        addCollege.addTextChangedListener(textWatcher);

        //给按键设置事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                People peo = new People(name, college);
                viewModel.insertPeo(peo);
                //返回
                NavController controller = Navigation.findNavController(v);
                controller.navigateUp();//回到来的页面

                //关闭键盘
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    //当页面退出时候执行
    @Override
    public void onStop() {
        super.onStop();
        //关闭键盘
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
