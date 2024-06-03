package kyonggi.bookslyserver.domain.shop.constant;

public enum CategoryName {
    HAIR("헤어"),
    NAIL("네일"),
    MASSAGE("마사지"),
    EYEBROW("눈썹"),
    MAKEUP("메이크업"),
    WAXING("왁싱"),
    ETC("기타")
    ;
    private String name;
    CategoryName(String name){this.name=name;}
    public String getName(){return name;}
}
