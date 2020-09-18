package net.rolodophone.voicefizzbuzz

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.speech.*
import android.view.*
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import net.rolodophone.voicefizzbuzz.databinding.FragmentGameBinding
import java.util.*

class GameFragment: Fragment() {
	companion object {
		const val RECORD_AUDIO_REQUEST_CODE = 1
	}

	private val args: GameFragmentArgs by navArgs()

	private lateinit var numbers: IntArray
	private var includeSubstring = false

	lateinit var textView: TextView

	lateinit var speechRecognizer: SpeechRecognizer

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater, R.layout.fragment_game, container, false)

		numbers = args.numbers
		includeSubstring = args.includeSubstring

		textView = binding.textView

		return binding.root
	}

	override fun onStart() {
		super.onStart()
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
		speechRecognizer.setRecognitionListener(FizzBuzzRecognitionListener())
	}

	override fun onResume() {
		super.onResume()

		//request permissions
		if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
			}
		}

		//start speechRecognizer
		val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

		speechRecognizer.startListening(recognizerIntent)
	}

	override fun onPause() {
		super.onPause()
		speechRecognizer.stopListening()
	}

	override fun onStop() {
		super.onStop()
		speechRecognizer.destroy()
	}


	inner class FizzBuzzRecognitionListener: RecognitionListener {
		override fun onReadyForSpeech(params: Bundle?) {}
		override fun onBeginningOfSpeech() {}
		override fun onRmsChanged(rmsdB: Float) {}
		override fun onBufferReceived(buffer: ByteArray?) {}
		override fun onEndOfSpeech() {}

		override fun onError(error: Int) {
			textView.text = "ERROR"
		}

		override fun onResults(results: Bundle?) {
			textView.text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString()
		}

		override fun onPartialResults(partialResults: Bundle?) {
			textView.text = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString()
		}
		override fun onEvent(eventType: Int, params: Bundle?) {}
	}
}