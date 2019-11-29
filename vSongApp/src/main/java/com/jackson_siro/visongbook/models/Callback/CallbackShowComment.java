package com.jackson_siro.visongbook.models.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.visongbook.models.Comment;

public class CallbackShowComment implements Serializable {
    public List<Comment> data = new ArrayList<Comment>();
}
