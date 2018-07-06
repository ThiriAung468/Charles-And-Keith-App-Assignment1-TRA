package com.padcmyanmar.charles.and.keith.assignment.tra.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.padcmyanmar.charles.and.keith.assignment.tra.R;
import com.padcmyanmar.charles.and.keith.assignment.tra.adapters.CharlesAndKeithListOneAdapter;
import com.padcmyanmar.charles.and.keith.assignment.tra.data.models.NewProductsModel;
import com.padcmyanmar.charles.and.keith.assignment.tra.data.vos.NewProductsVO;
import com.padcmyanmar.charles.and.keith.assignment.tra.delegates.CharlesAndKeithShowItemsDelegate;
import com.padcmyanmar.charles.and.keith.assignment.tra.events.ApiErrorEvent;
import com.padcmyanmar.charles.and.keith.assignment.tra.events.SuccessForceRefreshGetNewProductsEvent;
import com.padcmyanmar.charles.and.keith.assignment.tra.events.SuccessGetNewProductsEvent;
import com.padcmyanmar.charles.and.keith.assignment.tra.utils.NewProductsConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharlesAndKeithListActivity extends BaseActivity implements CharlesAndKeithShowItemsDelegate {
    private CharlesAndKeithListOneAdapter mcharlesAndKeithListOneAdapter;
    private GridLayoutManager mGridLayoutManager;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_charles_and_keith_list_one)
    RecyclerView rvShowListOne;

    @BindView(R.id.iv_pagination_2)
    ImageView ivShowTwoItem;

    @BindView(R.id.iv_pagination_1)
    ImageView ivShowOneItem;

    @BindView(R.id.vp_empty)
    RelativeLayout vpEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charles_and_keith_list);

        setSupportActionBar(toolbar);
        ButterKnife.bind(this, this);

        //for want to know end of scroll list.
        rvShowListOne.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean isListEndReached = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("NewProductsListActivity", "OnScrollListener : onScrollStateChanged  : " + newState);

                // Scroll for end state
                if(newState == RecyclerView.SCROLL_STATE_IDLE &&
                        ( (LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition()
                                == recyclerView.getAdapter().getItemCount() - 1
                        && !isListEndReached){

                    isListEndReached = true;
                    NewProductsModel.getObjInstance().loadNewProductList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("NewProductsListActivity", "OnScrollListener : onScroll - dx  : " + dx + ", dy : " + dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();

                if(visibleItemCount+pastVisibleItems < totalItemCount){
                    isListEndReached = false;
                }
            }
        });

        /*final RecyclerView rvShowListOne = findViewById(R.id.rv_charles_and_keith_list_one);*/
        mcharlesAndKeithListOneAdapter = new CharlesAndKeithListOneAdapter(this);
        rvShowListOne.setAdapter(mcharlesAndKeithListOneAdapter);
        /*rvShowListOne.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));*/

        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvShowListOne.setLayoutManager(mGridLayoutManager);

        ivShowTwoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridLayoutManager.setSpanCount(2);

            }
        });


        ivShowOneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rvListOne.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                mGridLayoutManager.setSpanCount(1);
            }
        });

        NewProductsModel.getObjInstance().loadNewProductList();
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewProductsModel.getObjInstance().forceRefreshNewsList();
            }
        });
    }

    @Override
    public void onTapShowItems(NewProductsVO newProducts) {
        Intent intent = new Intent(getApplicationContext(), CharlesAndKeithDetailsListActivity.class);
        intent.putExtra(NewProductsConstants.PRODUCT_ID, newProducts.getProductId());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessGetNewProducts(SuccessGetNewProductsEvent event) {
        Log.d("onSuccessGetNewProducts", "onSuccessGetNewProducts : " + event.getNewProductsList().size());

        mcharlesAndKeithListOneAdapter.appendNewsList(event.getNewProductsList());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFailGetNews(ApiErrorEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(swipeRefreshLayout, event.getErrorMsg(), Snackbar.LENGTH_INDEFINITE).show();

        if(mcharlesAndKeithListOneAdapter.getItemCount()<0) {
            vpEmpty.setVisibility(View.VISIBLE);
        }else  vpEmpty.setVisibility(View.GONE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessForceRefreshGetNews(SuccessForceRefreshGetNewProductsEvent event) {
        mcharlesAndKeithListOneAdapter.setNewProductsList(event.getNewProductsList());
        swipeRefreshLayout.setRefreshing(false);
    }
}
