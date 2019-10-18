package com.jackson_siro.visongbook.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.models.PostModel;

public class CallbackPostsLists implements Serializable{
    public int total = -1;
    public List<PostModel> data = new ArrayList<PostModel>();
}
