package com.jackson_siro.visongbook.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.models.PostsSlider;

public class CallbackPostsSlider implements Serializable {
    public List<PostsSlider> data = new ArrayList<PostsSlider>();
}
