package com.interview.doordash

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.interview.doordash.data.MenuItem
import com.interview.doordash.databinding.RowPopularItemBinding
import com.interview.doordash.ui.storedetail.PopularMenuViewHolder
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.text.NumberFormat

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class PopularMenuAdapterTest {

    @Test
    fun `Test that proper values are set in the ViewHolder`() {
        val name = "Chicken Tikka"
        val menuItem = MenuItem(id = 1, price = 1200, menuImage = "", description = "", name)

        val context: Context = ApplicationProvider.getApplicationContext()
        val inflater = LayoutInflater.from(context)
        val parent = LinearLayout(context)
        val binding = RowPopularItemBinding.inflate(inflater, parent, false)

        val viewHolder = PopularMenuViewHolder(binding)

        viewHolder.bind(menuItem)

        Truth.assertThat(binding.popularMenuItemName.text).isEqualTo(name)

        val price = NumberFormat.getCurrencyInstance().format(menuItem.price/100)
        Truth.assertThat(binding.popularMenuItemPrice.text).isEqualTo(price)
    }
}