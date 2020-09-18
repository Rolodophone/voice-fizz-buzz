package net.rolodophone.voicefizzbuzz

import android.content.Intent
import android.os.Bundle
import android.speech.*
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import net.rolodophone.voicefizzbuzz.databinding.FragmentGameBinding
import java.util.*

class GameFragment: Fragment() {
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

		val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

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

		override fun onPartialResults(partialResults: Bundle?) {}
		override fun onEvent(eventType: Int, params: Bundle?) {}
	}
}