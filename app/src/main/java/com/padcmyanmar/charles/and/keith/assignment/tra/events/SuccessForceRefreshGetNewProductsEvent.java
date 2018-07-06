package com.padcmyanmar.charles.and.keith.assignment.tra.events;

import com.padcmyanmar.charles.and.keith.assignment.tra.data.vos.NewProductsVO;

import java.util.List;

public class SuccessForceRefreshGetNewProductsEvent extends SuccessGetNewProductsEvent {


    public SuccessForceRefreshGetNewProductsEvent(List<NewProductsVO> newProductsList) {
        super(newProductsList);
    }
}
