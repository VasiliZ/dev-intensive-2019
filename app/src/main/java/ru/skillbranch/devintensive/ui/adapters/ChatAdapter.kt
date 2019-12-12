package ru.skillbranch.devintensive.ui.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.archive_item.*
import kotlinx.android.synthetic.main.item_chat_group.*
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_chat_single.sv_indicator
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.ChatItem
import ru.skillbranch.devintensive.models.ChatType
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity

class ChatAdapter(private val listener: (ChatItem) -> Unit) :
    RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {
    var items: List<ChatItem> = listOf()

    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when (items[position].chatType) {
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.GROUP -> GROUP_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            SINGLE_TYPE -> SingleChatViewHolder(
                inflater.inflate(
                    R.layout.item_chat_single,
                    parent,
                    false
                )
            )

            GROUP_TYPE -> GroupChatViewHolder(
                inflater.inflate(
                    R.layout.item_chat_group,
                    parent,
                    false
                )
            )
            else ->
                ArchiveChatViewHolder(inflater.inflate(R.layout.archive_item, parent, false))
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun updateData(newItems: List<ChatItem>) {

        val differCallBack = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].id == newItems[newItemPosition].id

            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].hashCode() == newItems[newItemPosition].hashCode()

        }
        val diffResult = DiffUtil.calculateDiff(differCallBack)
        this.items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    abstract inner class ChatItemViewHolder(convertView: View) :
        RecyclerView.ViewHolder(convertView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)

    }


    inner class GroupChatViewHolder(itemView: View) :
        ChatItemViewHolder(itemView), ItemTouchViewHolder {
        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            iv_avatar_group.setInitials(item.initials)

            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            with(tv_date_group) {
                visibility = if (item.lastMessageDate == null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_group) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_text_title_group.text = item.title
            tv_text_message_group.text = item.shortDescription
            with(tv_text_message_author) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.author
            }

            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {

            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.BLACK)
        }
    }

    inner class SingleChatViewHolder(itemView: View) :
        ChatItemViewHolder(itemView), ItemTouchViewHolder {
        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            if (item.avatar == null) {
                Glide.with(itemView).clear(iv_avatar_single)
                iv_avatar_single.setInitials(item.initials)
            } else {
                Glide.with(itemView)
                    .load(item.avatar)
                    .into(iv_avatar_single)
            }

            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            with(tv_date_single) {
                visibility = if (item.lastMessageDate == null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_single) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_text_title_single.text = item.title
            tv_text_message_single.text = item.shortDescription
            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {

            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.BLACK)
        }
    }

    inner class ArchiveChatViewHolder(itemView: View) :
        ChatItemViewHolder(itemView) {
        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            with(tv_date_archive) {
                visibility = if (item.lastMessageDate == null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(tv_counter_archive) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_message_archive.text = item.shortDescription
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ArchiveActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }


    }
}
