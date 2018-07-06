package com.padcmyanmar.charles.and.keith.assignment.tra.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.padcmyanmar.charles.and.keith.assignment.tra.R;
import com.padcmyanmar.charles.and.keith.assignment.tra.adapters.CharlesAndKeithDetailsListAdapter;
import com.padcmyanmar.charles.and.keith.assignment.tra.adapters.CharlesAndKeithShowNextItemsListAdapter;

public class CharlesAndKeithDetailsListActivity extends BaseActivity {
    private CharlesAndKeithDetailsListAdapter mcharlesAndKeithDetailsListAdapter;
    private CharlesAndKeithShowNextItemsListAdapter charlesAndKeithShowNextItemsListAdapter;

    /* private NewProductsVO mNewProducts;

     @BindView(R.id.iv_shoe_show_one)
     ImageView ivShoeShowOne;
 */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charles_and_keith_details_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);


        RecyclerView rvDetailsList = findViewById(R.id.rv_charles_and_keith_details_list);
        mcharlesAndKeithDetailsListAdapter = new CharlesAndKeithDetailsListAdapter();
        rvDetailsList.setAdapter(mcharlesAndKeithDetailsListAdapter);
        rvDetailsList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        RecyclerView rvShowNextItems = findViewById(R.id.rv_charles_and_keith_next_items_list);
        charlesAndKeithShowNextItemsListAdapter = new CharlesAndKeithShowNextItemsListAdapter();
        rvShowNextItems.setAdapter(charlesAndKeithShowNextItemsListAdapter);
        rvShowNextItems.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));


       /* ButterKnife.bind( this,this);

        int productsId = getIntent().getIntExtra(NewProductsConstants.PRODUCT_ID, 1);
        Log.d("CharlesAndKeithDetails", "productsId : " + productsId);

        NewProductsVO newProducts = NewProductsModel.getObjInstance().getNewProductsById(productsId);
        if(newProducts != null) {
            bindData(newProducts);
        }*/


    }


   /* private void bindData(NewProductsVO newProducts) {
        mNewProducts = newProducts;

        GlideApp.with(ivShoeShowOne.getContext())
                .load(newProducts.getProductImage())
                .placeholder(R.drawable.img_news_placeholder)
                .error(R.drawable.error_img)
                .into(ivShoeShowOne);

    }*/
}
