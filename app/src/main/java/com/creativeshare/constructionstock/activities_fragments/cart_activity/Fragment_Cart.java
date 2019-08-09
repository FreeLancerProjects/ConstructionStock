package com.creativeshare.constructionstock.activities_fragments.cart_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.adapters.CartAdapter;
import com.creativeshare.constructionstock.adapters.Swipe;
import com.creativeshare.constructionstock.models.ItemCartModel;
import com.creativeshare.constructionstock.singleton.CartSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Cart extends Fragment implements Swipe.SwipeListener {

    private RecyclerView recView;
    private LinearLayoutManager manager;
    private LinearLayout llNext,llEmptyCart;
    private ImageView arrow;
    private String lang;
    private CartSingleton singleton;
    private CartActivity activity;
    private CartAdapter adapter;
    private List<ItemCartModel> itemCartModelList;
    public static Fragment_Cart newInstance()
    {
        return new Fragment_Cart();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        itemCartModelList = new ArrayList<>();
        activity = (CartActivity) getActivity();
        singleton = CartSingleton.newInstance();
        itemCartModelList.addAll(singleton.getItemCartModelList());
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = view.findViewById(R.id.arrow);
        if (lang.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }
        llEmptyCart = view.findViewById(R.id.llEmptyCart);

        llNext = view.findViewById(R.id.llNext);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new CartAdapter(itemCartModelList,activity);
        recView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new Swipe(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(recView);
        updateUI();

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayFragmentAddress();
            }
        });



    }

    private void updateUI() {
        if (itemCartModelList.size()==0)
        {
            llNext.setVisibility(View.GONE);
            llEmptyCart.setVisibility(View.VISIBLE);
        }else {
            llNext.setVisibility(View.VISIBLE);
            llEmptyCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSwipe(int pos, int dir) {
        CreateCartDeleteDialog(pos);
    }

    private void CreateCartDeleteDialog(final int pos) {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_delete,null);
        TextView tvDelete = view.findViewById(R.id.tvDelete);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    itemCartModelList.remove(pos);
                    singleton.deleteItem(pos);
                    adapter.notifyDataSetChanged();
                    if (itemCartModelList.size()==0)
                    {
                        llNext.setVisibility(View.GONE);
                        llEmptyCart.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){}

                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();

    }

}
