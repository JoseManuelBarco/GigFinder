package com.example.myapplication.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.chats.data.ChatRoom

class ChatRoomAdapter(private val chatRooms: List<ChatRoom>, private val userId: Int, private val onItemClick: (Int, String) -> Unit) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        val otherUser = if (chatRoom.user1.id == userId) chatRoom.user2 else chatRoom.user1
        holder.chatRoomName.text = otherUser.name
        holder.profileImage.setImageResource(R.drawable.gigfinder_default_0) // Default image

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick(chatRoom.id, otherUser.name)
        }
    }

    override fun getItemCount(): Int = chatRooms.size

    class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.profileImage)
        val chatRoomName: TextView = view.findViewById(R.id.chatRoomName)
    }
}