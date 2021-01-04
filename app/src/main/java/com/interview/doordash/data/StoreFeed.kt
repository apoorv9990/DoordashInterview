package com.interview.doordash.data

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.interview.doordash.R
import kotlinx.parcelize.Parcelize

data class StoreFeed(val stores: List<Store>)

@Parcelize
data class Store(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("cover_img_url")
    val restaurantImageUrl: String,
    val status: StoreStatus,
    @SerializedName("is_consumer_subscription_eligible")
    val isSubscriptionEligible: Boolean,
    @SerializedName("num_ratings")
    val numberOfRatings: Int,
    @SerializedName("average_rating")
    val averageRating: Double,
    @SerializedName("distance_from_consumer")
    val distanceToStore: Double,
    @SerializedName("price_range")
    val priceRange: Int,
    val menus: List<Menu>?
) : Parcelable

@Parcelize
data class StoreStatus(
    @SerializedName("unavailable_reason")
    val unavailableReason: String,
    @SerializedName("pickup_available")
    val isPickupAvailable: Boolean,
    @SerializedName("asap_available")
    val isAsapAvailable: Boolean,
    @SerializedName("scheduled_available")
    val isScheduledAvailable: Boolean,
    @SerializedName("asap_minutes_range")
    val minutesRange: IntArray
) : Parcelable {
    val isAvailable
    get() = isPickupAvailable || isScheduledAvailable || isAsapAvailable

    fun getTimeFromStore(context: Context): String {
        return if (isPickupAvailable || isScheduledAvailable || isAsapAvailable) {
            context.getString(R.string.time_from_store_minutes, minutesRange.average().toInt().toString())
        } else {
            context.getString(R.string.time_from_store_closed)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreStatus

        if (unavailableReason != other.unavailableReason) return false
        if (isPickupAvailable != other.isPickupAvailable) return false
        if (isAsapAvailable != other.isAsapAvailable) return false
        if (isScheduledAvailable != other.isScheduledAvailable) return false
        if (!minutesRange.contentEquals(other.minutesRange)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unavailableReason.hashCode()
        result = 31 * result + isPickupAvailable.hashCode()
        result = 31 * result + isAsapAvailable.hashCode()
        result = 31 * result + isScheduledAvailable.hashCode()
        result = 31 * result + minutesRange.contentHashCode()
        return result
    }

}

@Parcelize
data class Menu(
    @SerializedName("popular_items")
    val popularItems: List<MenuItem>
) : Parcelable

@Parcelize
data class MenuItem(
    val id: Int,
    val price: Int,
    @SerializedName("img_url")
    val menuImage: String,
    val description: String,
    val name: String
) : Parcelable