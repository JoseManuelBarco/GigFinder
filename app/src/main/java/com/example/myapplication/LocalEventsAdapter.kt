import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.api_objects.EventResponse
import com.google.gson.Gson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException

class LocalEventsAdapter(private var eventList: List<EventResponse> = emptyList()) :
    RecyclerView.Adapter<LocalEventsAdapter.EventViewHolder>() {

    // Formatos que podríamos recibir (según los ejemplos)
    private val possibleFormats = listOf(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,    // "2025-03-25T19:00:00"
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),  // "2025-04-25 02:45:00"
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),    // Por si acaso viene sin segundos
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")       // Por si acaso viene sin segundos y con espacio
                                        )

    // Formatos para mostrar
    private val displayDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    private val displayTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.date_eventTextView)
        val timeTextView: TextView = view.findViewById(R.id.textView10)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.local_offer_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        Log.d("AdapterDebug", "Procesando evento ID: ${event.id} - dateStart: ${event.dateStart}")

        Log.d("RawDataCheck", "Evento completo: ${Gson().toJson(event)}")


        try {
            when {
                event.dateStart.isNullOrEmpty() -> {
                    holder.dateTextView.text = "Fecha no disponible"
                    holder.timeTextView.text = ""
                    Log.w("Adapter", "Fecha vacía para evento ID: ${event.id}")
                }
                else -> {
                    val dateTime = parseDateTime(event.dateStart)
                    if (dateTime != null) {
                        holder.dateTextView.text = dateTime.format(displayDateFormatter)
                        holder.timeTextView.text = dateTime.format(displayTimeFormatter)
                    } else {
                        holder.dateTextView.text = "Formato inválido"
                        holder.timeTextView.text = ""
                        Log.e("Adapter", "No se pudo parsear fecha: ${event.dateStart}")
                    }
                    holder.descriptionTextView.text = event.description ?: "Sin descripción"
                }
            }
        } catch (e: Exception) {
            Log.e("Adapter", "Error inesperado con evento ID: ${event.id}", e)
            holder.dateTextView.text = "Error"
            holder.timeTextView.text = ""
        }
    }

    private fun parseDateTime(dateString: String): LocalDateTime? {
        return possibleFormats.firstNotNullOfOrNull { formatter ->
            try {
                LocalDateTime.parse(dateString, formatter)
            } catch (e: DateTimeParseException) {
                null
            }
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateData(newEvents: List<EventResponse>) {
        eventList = newEvents
        notifyDataSetChanged()
    }
}