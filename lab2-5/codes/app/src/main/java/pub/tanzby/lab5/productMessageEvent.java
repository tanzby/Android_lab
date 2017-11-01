package pub.tanzby.lab5;

/**
 * Created by tan on 17-10-27.
 */

public class productMessageEvent     {
    public Integer productIndex;
    public Integer productPurchaseNum;
    public Boolean isProductFavor;

    public productMessageEvent(Product product,Integer productIndex) {
        this.productPurchaseNum=product.getPurchaseNumber();
        this.isProductFavor=product.getIs_favorite();
        this.productIndex=productIndex;
    }
}
