package adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.R
import com.example.exchangeapp.databinding.ItemRvBinding
import model.Exchange
import java.util.*

class UserAdapter(var context: Context, var list: List<Exchange>, var myClick: MyClick) :
    RecyclerView.Adapter<UserAdapter.Vh>() {

    inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(user: Exchange, position: Int) {
            itemRv.rvRate.text = user.Rate
            itemRv.rvDate.text = "Date: ${user.Date}"
            itemRv.rvDiff.text = user.Diff.toString()
            itemRv.rvRegionName.text = user.CcyNm_UZ
            itemRv.rvFlag.text = getFlag(user.Ccy.substring(0, 2))

            if (user.Diff < 1) {
                itemRv.imageDifference.setImageResource(R.drawable.ic_baseline_trending_down_24)
            }

            val valute = user.Rate.toDouble()

            itemRv.rvExchange.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /*  val uzs = itemRv.rvExchange.text.toString().toInt()
                      val ssd = itemRv.rvRate.text.toString().toInt()
                      val a = uzs * ssd
                      itemRv.rvExchange.text = a*/
                    if (s!!.isNotEmpty()) {
                        val count = itemRv.rvExchange.text.toString().toInt()
                        val total = String.format("%.2f", count * valute)
                        itemRv.rvRate.text = total
                    } else {
                        itemRv.rvRate.text = user.Rate
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            itemRv.rvExchange.setOnClickListener {
                myClick.click(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount():Int{
        return try {
            list.size
        } catch (ex: Exception) {
            return 0
        }
    }

    interface MyClick {
        fun click(valute: Exchange)
    }

    fun getFlag(countryCode: String): String {
        return countryCode
            .toUpperCase(Locale.US).map { char ->
                Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
            }
            .map { codePoint ->
                Character.toChars(codePoint)
            }
            .joinToString(separator = "") { charArray ->
                String(charArray)
            }
    }

}