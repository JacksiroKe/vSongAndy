package com.jackson_siro.visongbook.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.models.CategoryModel;

public class CallbackCategory implements Serializable {
    public List<CategoryModel> data = new ArrayList<>();
}
