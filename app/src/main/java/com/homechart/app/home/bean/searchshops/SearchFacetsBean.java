package com.homechart.app.home.bean.searchshops;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/11/10.
 */

public class SearchFacetsBean  implements Serializable {

    private SearchFacetsPriceBean price;

    public SearchFacetsBean(SearchFacetsPriceBean price) {
        this.price = price;
    }

    public SearchFacetsPriceBean getPrice() {
        return price;
    }

    public void setPrice(SearchFacetsPriceBean price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SearchFacetsBean{" +
                "price=" + price +
                '}';
    }
}
