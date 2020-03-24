package com.jackson_siro.visongbook.core;

import java.util.ArrayList;

public interface FilterResultListener<T> {
	void onFilter(ArrayList<T> items);
}
