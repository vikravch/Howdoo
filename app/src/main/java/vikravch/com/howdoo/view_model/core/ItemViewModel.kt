package vikravch.com.howdoo.view_model.core

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.MutableLiveData
import com.vikravch.poloniexproject.view_model.core.BaseViewModel
import vikravch.com.howdoo.model.entities.Item
import java.text.SimpleDateFormat

class ItemViewModel : BaseViewModel() {
    val name = MutableLiveData<Spanned>()
    val fullName = MutableLiveData<Spanned>()
    val avatar = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    fun bind(item: Item, query: String) {
        name.value = createSpanned(item.name, query)
        fullName.value = createSpanned(item.fullName, query)
        avatar.value = item.owner.avatar
        date.value = convertDate(item.udatedAt)
    }

    private fun createSpanned(text: String, search: String): Spanned {
        val res = SpannableString(text)
        val start = text.toLowerCase().indexOf(search.toLowerCase())
        if (start >= 0)
            res.setSpan(
                ForegroundColorSpan(Color.parseColor("#d9175e")),
                start,
                start + search.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        return res
    }

    private fun convertDate(dateStr: String) = SimpleDateFormat("yyyy','MM'/'dd HH:mm").format(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateStr)
    )
}