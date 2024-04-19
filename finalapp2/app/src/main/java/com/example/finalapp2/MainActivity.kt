import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_destination.*

class AddDestinationFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_destination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rankings = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rankings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_ranking.adapter = adapter

        button_add_destination.setOnClickListener {
            val destinationName = editText_destination_name.text.toString().trim()
            val description = editText_description.text.toString().trim()
            val ranking = spinner_ranking.selectedItem.toString()

            if (destinationName.isEmpty() || description.isEmpty()) {
                showToast("All fields need to be populated")
            } else {
                saveDestinationToFirestore(destinationName, description, ranking)
            }
        }
    }

    private fun saveDestinationToFirestore(destinationName: String, description: String, ranking: String) {
        val destination = hashMapOf(
            "name" to destinationName,
            "description" to description,
            "ranking" to ranking
        )

        firestore.collection("destinations")
            .add(destination)
            .addOnSuccessListener {
                showToast("Destination added successfully")
                requireActivity().supportFragmentManager.popBackStack()
            }
            .addOnFailureListener {
                showToast("Failed to add destination")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
