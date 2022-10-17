package com.shopiroller.enums;

import com.shopiroller.Shopiroller;

public enum ColorEnum {

    Primary(Shopiroller.getTheme().primaryColor, 0),
    Secondary(Shopiroller.getTheme().secondaryColor, 1),
    Dark(Shopiroller.getTheme().darkColor, 2),
    Header(Shopiroller.getTheme().headerColor, 3),
    Text(Shopiroller.getTheme().textColor, 4),
    Paragraph(Shopiroller.getTheme().paragraphColor, 5),
    ButtonPrimary(Shopiroller.getTheme().buttonPrimaryColor, 6),
    TextPrimary(Shopiroller.getTheme().textPrimaryColor, 7),
    ImageTintPrimary(Shopiroller.getTheme().imagePrimaryColor, 8),
    TextSecondary(Shopiroller.getTheme().textSecondaryColor, 9);

    private int resId;
    private int resOrder;

    ColorEnum(int resId, int resOrder) {
        this.resId = resId;
        this.resOrder = resOrder;
    }

    public int getResId() {
        return resId;
    }

    public int getResOrder() {
        return resOrder;
    }

    public static int getResIdByResOrder(int order){
        for(ColorEnum e : ColorEnum.values()){
            if(order == e.resOrder) return e.getResId();
        }
        return 2;
    }

}
