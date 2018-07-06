package com.padcmyanmar.charles.and.keith.assignment.tra.data.models;

import com.padcmyanmar.charles.and.keith.assignment.tra.data.vos.NewProductsVO;
import com.padcmyanmar.charles.and.keith.assignment.tra.events.SuccessForceRefreshGetNewProductsEvent;
import com.padcmyanmar.charles.and.keith.assignment.tra.events.SuccessGetNewProductsEvent;
import com.padcmyanmar.charles.and.keith.assignment.tra.network.NewProductsDataAgent;
import com.padcmyanmar.charles.and.keith.assignment.tra.network.RetrofitDataAgentImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewProductsModel {

    private static final String DUMMY_ACCESS_TOKEN = "b002c7e1a528b7cb460933fc2875e916";

    private int mPage;

    private Map<Integer, NewProductsVO> mNewProductsMap;

    private static NewProductsModel objInstance;

    private NewProductsDataAgent mNewProductsDataAgent;

    private NewProductsModel() {
        mNewProductsDataAgent = RetrofitDataAgentImpl.getsObjInstance();
        mPage = 1;

        mNewProductsMap = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    public static NewProductsModel getObjInstance() {
        if(objInstance == null){
            objInstance = new NewProductsModel();
        }
        return objInstance;
    }

    public void loadNewProductList(){

        mNewProductsDataAgent.loadNewProductList(DUMMY_ACCESS_TOKEN, mPage,false);
    }

    public void forceRefreshNewsList(){
        mPage = 1;

        mNewProductsDataAgent.loadNewProductList(DUMMY_ACCESS_TOKEN,1,true);

    }

    public NewProductsVO getNewProductsById(int productsId){
        return mNewProductsMap.get(productsId);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onSuccessGetNewProducts(SuccessGetNewProductsEvent event){
        setDataIntoRepository(event.getNewProductsList());

        mPage++;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onSuccessForceRefreshGetNews(SuccessForceRefreshGetNewProductsEvent event){
        setDataIntoRepository(event.getNewProductsList());
    }

    private void setDataIntoRepository(List<NewProductsVO> newProductsList){
        for(NewProductsVO newProducts : newProductsList){
            mNewProductsMap.put(newProducts.getProductId(), newProducts);
        }
    }
}
