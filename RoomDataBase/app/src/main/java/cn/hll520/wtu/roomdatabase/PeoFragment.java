package cn.hll520.wtu.roomdatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeoFragment extends Fragment {

    private static final String FLAG_CARD = "flag_card";
    private static final String SETT = "sett";
    //创建ViewModel实例
    private PeopleViewModel peopleViewModel;
    private RecyclerView recyclerView;
    private PeoAdapter adapterNorm, adapterCard;
    private LiveData<List<People>> findPeo;
    private List<People> peos; //定义中间参数，避免异步为完成导致崩溃
    public PeoFragment() {
        // Required empty public constructor
        //显示工具栏
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peo, container, false);
    }

    //创建页面
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        peopleViewModel = new ViewModelProvider(this).get(PeopleViewModel.class);
        //在Fragment中使用requireActivity()获取当前活动
        recyclerView = requireActivity().findViewById(R.id.body);
        //设置组件维度
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        //初始化适配器
        adapterNorm = new PeoAdapter(false);
        adapterCard = new PeoAdapter(true);
        //设置适配器
        SharedPreferences shp = requireActivity().getSharedPreferences(SETT, Context.MODE_PRIVATE);
        boolean flag = shp.getBoolean(FLAG_CARD, false);
        if (flag)
            recyclerView.setAdapter(adapterCard);
        else
            recyclerView.setAdapter(adapterNorm);
        //添加观察者，两个参数，当前活动和观察者
        findPeo = peopleViewModel.getAllPeos();
        findPeo.observe(getViewLifecycleOwner(), new Observer<List<People>>() {
            @Override
            public void onChanged(List<People> people) {
                peos = people;
                adapterNorm.submitList(people);
                adapterCard.submitList(people);
            }
        });
        FloatingActionButton floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取导航控制器，参数为当前页面
                NavController controller = Navigation.findNavController(v);
                //设置动作
                controller.navigate(R.id.action_peoFragment_to_addFragment);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delAll:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity()).setTitle("确定清空？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                peopleViewModel.delAll();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                break;
            case R.id.changeView:
                SharedPreferences shp = requireActivity().getSharedPreferences(SETT, Context.MODE_PRIVATE);
                boolean flag = shp.getBoolean(FLAG_CARD, false);
                SharedPreferences.Editor editor = shp.edit();
                if (flag) {
                    recyclerView.setAdapter(adapterNorm);
                    editor.putBoolean(FLAG_CARD, false);
                } else {
                    recyclerView.setAdapter(adapterCard);
                    editor.putBoolean(FLAG_CARD, true);
                }
                editor.apply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //绑定菜单
        inflater.inflate(R.menu.peo_menu, menu);
        //获取搜索框对象
        SearchView searchView = (SearchView) menu.findItem(R.id.peo_search).getActionView();
        //设置宽度
        searchView.setMaxWidth(1000);
        //设置监听——————返回值为是否终止事件的传递
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //按下确定键时
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //内容改变时
            @Override
            public boolean onQueryTextChange(String newText) {
                String name = newText.trim();
                findPeo.removeObservers(getViewLifecycleOwner());//移除原有观察
                findPeo = peopleViewModel.findPeosForName(name);
                //添加观察
                findPeo.observe(getViewLifecycleOwner(), new Observer<List<People>>() {
                    @Override
                    public void onChanged(List<People> people) {
                        //向上滚动200
                        recyclerView.smoothScrollBy(0, -200);
                        peos = people;
                        adapterNorm.submitList(people);
                        adapterCard.submitList(people);

                    }
                });
                return true;
            }
        });

        //手势效果   参数（拖动，滑动）    （开始和结束都可以，即左到右，和右到左）   (Up是上下拖动）
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) {

            //会有BUG，当拖动的足够快时候，数据来不及处理会导致混乱
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //获取拖动的内容
                People people = peos.get(viewHolder.getAdapterPosition());
                //获取目的地内容
                People peopleend = peos.get(target.getAdapterPosition());
                //交换他们的id
                int idTemp = people.get_id();
                people.set_id(peopleend.get_id());
                peopleend.set_id(idTemp);
                //更新数据
                peopleViewModel.updatePeo(people, peopleend);
                //改变位置  （参数 起  终）
                adapterCard.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapterNorm.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            //滑动
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Livedata获取内容，从viewholder的位置，viewHolder.getAdapterPosition()获取当前的位置
                final People people = peos.get(viewHolder.getAdapterPosition());
                peopleViewModel.delPeo(people);

                //底部横幅，  三个参数（显示在那个组件上，显示的内容，时间）
                Snackbar.make(requireView().findViewById(R.id.peoView), "删除了一个联系人", Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                peopleViewModel.insertPeo(people);
                            }
                        }).show();
            }
        }).attachToRecyclerView(recyclerView);


    }
}
