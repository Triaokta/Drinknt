package com.dicoding.drinknt

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.drinknt.databinding.ActivityResultBinding
import org.json.JSONObject

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra(RESULTS)

        results?.let {
            displayFormattedResult(it)
        }
    }

    private fun displayFormattedResult(jsonString: String) {
        try {
            // Parse JSON string menjadi JSONObject
            val jsonObject = JSONObject(jsonString)

            // Ambil data dari JSONObject
            val jenisMinuman = jsonObject.getString("jenis minuman")
            val kandunganGula = jsonObject.getString("kandungan gula")
            val peringatanKesehatan = jsonObject.getString("peringatan kesehatan")

            // Buat format output
            val result = """
                Kandungan Gula:
                $kandunganGula

                Peringatan Kesehatan:
                $peringatanKesehatan
            """.trimIndent()

            // Create a SpannableString to format the result
            val spannableResult = SpannableString(result)

            // Apply bold style to specific parts of the text
            val boldStyle = StyleSpan(Typeface.BOLD)

            // Apply bold to "Kandungan Gula"
            val startIndexKandunganGula = result.indexOf("Kandungan Gula:")
            val endIndexKandunganGula = startIndexKandunganGula + "Kandungan Gula:".length
            spannableResult.setSpan(boldStyle, startIndexKandunganGula, endIndexKandunganGula, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Apply bold to "Peringatan Kesehatan"
            val startIndexPeringatanKesehatan = result.indexOf("Peringatan Kesehatan:")
            val endIndexPeringatanKesehatan = startIndexPeringatanKesehatan + "Peringatan Kesehatan:".length
            spannableResult.setSpan(boldStyle, startIndexPeringatanKesehatan, endIndexPeringatanKesehatan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Tampilkan hasil pada TextView
            binding.jenisMinumanText.text = "$jenisMinuman"
            binding.resultText.text = spannableResult

        } catch (e: Exception) {
            e.printStackTrace()
            binding.resultText.text = "Error parsing JSON"
        }
    }

    companion object {
        const val IMAGE_URI = "img_uri"
        const val RESULTS = "results"
    }
}
