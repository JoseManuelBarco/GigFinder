import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

object NavigationBarHelper {

    fun setIconSelected(activity: AppCompatActivity, selectedIconId: Int) {
        // Obtener los ImageView de la barra de navegación desde la actividad actual
        val searchIcon = activity.findViewById<ImageView>(R.id.searchIconImageView)
        val messagesIcon = activity.findViewById<ImageView>(R.id.messagesIconImageView)
        val opportunitiesIcon = activity.findViewById<ImageView>(R.id.opportunitiesIconImageView)
        val eventsIcon = activity.findViewById<ImageView>(R.id.eventsIconImageView)
        val settingsIcon = activity.findViewById<ImageView>(R.id.settingsIconImageView)

        // Desmarcar todos los iconos
        searchIcon.isSelected = false
        messagesIcon.isSelected = false
        opportunitiesIcon.isSelected = false
        eventsIcon.isSelected = false
        settingsIcon.isSelected = false

        // Marcar el icono seleccionado
        when (selectedIconId) {
            R.id.searchIconImageView -> searchIcon.isSelected = true
            R.id.messagesIconImageView -> messagesIcon.isSelected = true
            R.id.opportunitiesIconImageView -> opportunitiesIcon.isSelected = true
            R.id.eventsIconImageView -> eventsIcon.isSelected = true
            R.id.settingsIconImageView -> settingsIcon.isSelected = true
        }
    }
}
