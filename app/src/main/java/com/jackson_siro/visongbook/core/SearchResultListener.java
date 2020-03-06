package com.jackson_siro.visongbook.core;

public interface SearchResultListener<T> {
	void onSelected(BaseSearchDialogCompat dialog, T item, int position);
}
