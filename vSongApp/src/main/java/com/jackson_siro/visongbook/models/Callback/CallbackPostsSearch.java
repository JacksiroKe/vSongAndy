package com.jackson_siro.visongbook.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.models.PostsSearch;

public class CallbackPostsSearch implements Serializable {
    public List<PostsSearch> data = new ArrayList<PostsSearch>();
}
