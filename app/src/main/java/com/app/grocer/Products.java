package com.app.grocer;
import com.google.firebase.database.IgnoreExtraProperties;
public class Products {
    public int productId;
    public String productName;
    public String productPrice;
    public int productQuantity;
    public String productCategory;
    public String productDescription;

    public Products(){}

    // Class to store products
    public Products(int pid, String pname,String pprice, int pquantity,String pcategory,String pdesc){
        productId = pid;
        productName = pname;
        productPrice = pprice;
        productQuantity = pquantity;
        productCategory = pcategory;
        productDescription = pdesc;


    }


}
