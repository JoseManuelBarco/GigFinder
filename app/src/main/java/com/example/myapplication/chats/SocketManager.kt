package com.example.myapplication.chats

import java.io.*
import java.net.Socket

object SocketManager {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null

    private const val HOST = "172.20.10.3" // Replace with your server IP
    private const val PORT = 12345 // Replace with your server port

    private var messageHandler: ((String) -> Unit)? = null
    private var onDisconnected: (() -> Unit)? = null

    // Initialize the socket connection
    fun initSocket(onConnected: (() -> Unit)? = null, onDisconnected: (() -> Unit)? = null): Boolean {
        System.out.println("🟡 Starting socket initialization...")

        this.onDisconnected = onDisconnected

        if (socket == null || socket?.isClosed == true) {
            System.out.println("🔄 Initializing new socket connection...")

            Thread {
                try {
                    socket = Socket(HOST, PORT)
                    writer = PrintWriter(BufferedWriter(OutputStreamWriter(socket!!.getOutputStream())), true)
                    reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                    System.out.println("✅ Socket connected")
                    onConnected?.invoke() // Notify when connection is established

                    // Send initial HTTP request to establish a persistent connection
                    initialConnectToChat()

                    // Start listening for messages from the server
                    listenForMessages()

                } catch (e: Exception) {
                    e.printStackTrace()
                    notifyDisconnected()
                }
            }.start()

            return true
        }
        return true
    }

    // Send an HTTP GET request to establish the connection
    private fun initialConnectToChat() {
        val token = "your-token-here"  // Replace with actual token

        val request =  """
    GET /user-connect HTTP/1.1
    Content-Type: application/json
    Connection: keep-alive
    
""".trimIndent() + "\r\n"

        writer?.print(request)
        writer?.flush()
        System.out.println("📤 Sent HTTP request to establish connection")
    }

    // Listen for incoming messages and pass them to the handler
    private fun listenForMessages() {
        Thread {
            try {
                System.out.println("📡 Listening for messages...")
                while (true) {
                    val line = reader?.readLine()
                    if (line == null) {
                        System.out.println("⚠️ Server closed the connection")
                        notifyDisconnected()
                        break
                    }
                    System.out.println("📩 Received: $line")
                    messageHandler?.invoke(line) // Trigger callback with received message
                }
            } catch (e: IOException) {
                System.out.println("⚠️ Connection lost")
                notifyDisconnected()
            }
        }.start()
    }

    // Set a callback to handle incoming messages
    fun setMessageHandler(handler: (String) -> Unit) {
        this.messageHandler = handler
    }

    // Send a message to the server
    fun sendMessage(message: String) {
        Thread {
            try {
                if (socket?.isConnected == true) {
                    writer?.println(message)
                    writer?.flush()
                    System.out.println("📤 Sent: $message")
                } else {
                    System.out.println("❌ Error: Socket is not connected")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    // Notify when socket is disconnected
    private fun notifyDisconnected() {
        System.out.println("❌ Socket disconnected")
        onDisconnected?.invoke()
        closeSocket()
    }

    // Close the socket connection
    fun closeSocket() {
        try {
            socket?.close()
            reader?.close()
            writer?.close()
            socket = null
            System.out.println("🔌 Socket closed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
