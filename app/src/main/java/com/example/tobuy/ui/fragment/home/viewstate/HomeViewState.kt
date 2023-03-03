package com.example.tobuy.ui.fragment.home.viewstate

data class HomeViewState(
    val dataList: List<DataItem<*>> = emptyList(),
    val isLoading: Boolean = false,
    val sort: Sort = Sort.NONE,
) {

    data class DataItem<T>(
        val data: T,
        val isHeader: Boolean = false
    )

    enum class Sort(val displayName: String) {
        NONE("None"),
        CATEGORY("Category"),
        OLDEST("Oldest"),
        NEWEST("Newest")
    }

}
