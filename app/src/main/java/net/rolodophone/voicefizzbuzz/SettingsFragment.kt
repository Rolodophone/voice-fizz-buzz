package net.rolodophone.voicefizzbuzz

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import net.rolodophone.voicefizzbuzz.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater, R.layout.fragment_settings, container, false)

		binding.startButton.setOnClickListener {
			val strNumbers = binding.editTextNumber.text.split(',')

			var numbersAreValid = true
			val numbers = strNumbers.map {
				if (it.isDigitsOnly() && it.isNotEmpty()) it.toInt()
				else  {
					numbersAreValid = false
					-1
				}
			}

			if (numbersAreValid) findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToGameFragment(numbers.toIntArray(), binding.checkBox.isChecked))
			else Snackbar.make(binding.root, "Please specify one or more positive integers separated by single commas", Snackbar.LENGTH_SHORT).show()
		}

		return binding.root
    }
}