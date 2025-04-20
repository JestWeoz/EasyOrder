package com.example.easyoderba.Utils;

public class ConfigSQL {
    public interface Product{
        String SEARCH_BY_QUERY = "select p from ProductEntity p where p.name like Concat('%', :keyword, '%')";
    }

}
