package ru.skillbranch.devintensive.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user_list.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.UserItem

class UserAdapter(private val listener: (UserItem) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var item: List<UserItem> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(convertView)
    }

    fun updateData(data: List<UserItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                item[oldItemPosition].id == data[newItemPosition].id

            override fun getOldListSize(): Int = item.size
            override fun getNewListSize(): Int = data.size


            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                item[oldItemPosition].hashCode() == data[newItemPosition].hashCode()
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        item = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = item.size
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(item[position], listener)

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(user: UserItem, listenet: (UserItem) -> Unit) {
            if (user.avatar != null) {
                Glide.with(itemView)
                    .load(user.avatar)
                    .into(iv_avatar_user)
            } else {
                Glide.with(itemView)
                    .clear(iv_avatar_user)
                iv_avatar_user.setInitials(user.initials ?: "??")
            }

            sv_indicator.visibility = if (user.isOnline) View.VISIBLE else View.GONE
            tv_user_name.text = user.fullName
            tv_last_activity.text = user.lastActivity
            iv_selected.visibility = if (user.isSelected) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                listenet.invoke(user)
            }
        }
    }
}