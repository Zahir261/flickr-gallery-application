package com.zahir.flickrgalleryapplication.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFilter(
    val tags: List<String> = emptyList(),
    val tagMode: TagMode = TagMode.All,
    val language: Language = Language.English
) : Parcelable

sealed class TagMode(val type: String, val keyword: String) : Parcelable {
    @Parcelize
    object All : TagMode("All tags", "all")

    @Parcelize
    object Any : TagMode("Any tag", "any")

    companion object {
        fun parse(name: String): TagMode {
            return when (name) {
                All.type -> All
                Any.type -> Any
                else -> All
            }
        }
    }
}

sealed class Language(val name: String, val keyword: String) : Parcelable {
    @Parcelize
    object English : Language("English", "en-us")

    @Parcelize
    object German : Language("German", "de-de")

    @Parcelize
    object Spanish : Language("Spanish", "es-us")

    @Parcelize
    object French : Language("French", "fr-fr")

    @Parcelize
    object Italian : Language("Italian", "it-it")

    @Parcelize
    object Korean : Language("Korean", "ko-kr")

    @Parcelize
    object Portuguese : Language("Portuguese (Brazilian)", "pt-br")

    @Parcelize
    object Chinese : Language("Traditional Chinese (Hong Kong)", "zh-hk")

    companion object {
        fun parse(language: String): Language {
            return when (language) {
                English.name -> English
                German.name -> German
                Spanish.name -> Spanish
                French.name -> French
                Italian.name -> Italian
                Korean.name -> Korean
                Portuguese.name -> Portuguese
                Chinese.name -> Chinese
                else -> English
            }
        }
    }
}
