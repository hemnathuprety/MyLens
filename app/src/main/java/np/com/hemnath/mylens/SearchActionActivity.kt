package np.com.hemnath.mylens

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import np.com.hemnath.mylens.databinding.ActivitySearchActionBinding
import np.com.hemnath.mylens.ui.adapter.ImagesRecyclerView
import java.io.IOException

class SearchActionActivity : AppCompatActivity() {
    private lateinit var bitmap: Bitmap
    private var detectedItems: MutableList<DetectedObject>? = null

    private lateinit var binding: ActivitySearchActionBinding

    private lateinit var objectDetector: ObjectDetector

    companion object {
        fun newIntent(context: Context, imageProxy: Uri?, type: String = "image") =
            Intent(context, SearchActionActivity::class.java)
                .putExtra("imageProxy", imageProxy.toString())
                .putExtra("type", type)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchActionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Search"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Multiple object detection in static images
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()  // Optional
            .build()

        objectDetector = ObjectDetection.getClient(options)


        val imagePath = intent.getStringExtra("imageProxy") ?: ""
        val type = intent.getStringExtra("type") ?: ""

        if (imagePath.isEmpty()) {
            finish()
        }

        val fileUri: Uri = Uri.parse(imagePath)
        binding.image.setImageURI(fileUri)

        /*binding.image.setOnTouchListener { v, e ->
            detectedItems?.let {
                for (detectedObject in it) {
                    Log.e("Search Fragment", "Photo capture success: ${v.clipBounds}")
                        val rect = Rect(v.left, v.top, v.right, v.bottom)
                        Log.e("Search Fragment", "Photo capture success: ${rect},${v.x}, ${v.y}")
                        if (detectedObject.boundingBox.contains(e.x.toInt(), e.y.toInt())) {
                            drawRect(detectedObject)
                        }

                }
            }
            false
        }*/

        try {
            val image: InputImage = InputImage.fromFilePath(this, fileUri)
            bitmap = image.bitmapInternal!!

            if (type == "image") {
                //analyzeImage(image)
                imageCustomModel(image)
            } else {
                analyzeText(image)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //binding.webview.settings.javaScriptEnabled = true
        //binding.webview.loadUrl("https://www4.bing.com/images/search?view=detailv2&iss=sbi&form=SBIVSP&sbisrc=UrlPaste&q=imgurl:https://ae01.alicdn.com/kf/S82b776b956c6426e93f5b9c95f3e83a1I/700-ML-Cute-Square-Plastic-Water-Bottle-With-Straw-For-Adults-Portable-Large-Capacity-Sports-Bottles.jpg&idpbck=1&selectedindex=0&id=https://ae01.alicdn.com/kf/S82b776b956c6426e93f5b9c95f3e83a1I/700-ML-Cute-Square-Plastic-Water-Bottle-With-Straw-For-Adults-Portable-Large-Capacity-Sports-Bottles.jpg&ccid=P8ZNdS8R&mediaurl=https://ae01.alicdn.com/kf/S82b776b956c6426e93f5b9c95f3e83a1I/700-ML-Cute-Square-Plastic-Water-Bottle-With-Straw-For-Adults-Portable-Large-Capacity-Sports-Bottles.jpg&exph=800&expw=800&vt=2&sim=1")
    }

    private fun analyzeImage(imageProxy: InputImage) {

        // To use default options:
        //val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        // Or, to set the minimum confidence required:
        // val options = ImageLabelerOptions.Builder()
        //     .setConfidenceThreshold(0.7f)
        //     .build()
        // val labeler = ImageLabeling.getClient(options)


        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 10f
        }


        /*labeler.process(imageProxy)
            .addOnSuccessListener { labels ->
                // Task completed successfully
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    val index = label.index

                    val chip = Chip(this)
                    chip.text = text.trim()
                    chip.setChipBackgroundColorResource(R.color.holo_orange_light)
                    chip.isCloseIconVisible = true
                    chip.setTextColor(getColor(R.color.white))
                    chip.setTextAppearance(R.style.TextAppearance)

                    binding.chipGroup.addView(chip)

                }
            }
            .addOnFailureListener { e ->
                Log.e("Search Fragment", "Photo capture success: ${e.localizedMessage}")
            }*/


        objectDetector.process(imageProxy)
            .addOnSuccessListener { detectedObjects ->

                detectedItems = detectedObjects

                /*if (detectedObjects.isNotEmpty()) {
                    val detectedObject = detectedObjects[0]
                    drawRect(detectedObject)

                    val croppedBmp: Bitmap = Bitmap.createBitmap(
                        bitmap,
                        (detectedObject.boundingBox.exactCenterX() - (detectedObject.boundingBox.width() / 2)).toInt(),
                        (detectedObject.boundingBox.exactCenterY() - (detectedObject.boundingBox.height() / 2)).toInt(),
                        detectedObject.boundingBox.width(),
                        detectedObject.boundingBox.height()
                    )

                    binding.image.setImageBitmap(croppedBmp)

                }*/

                val dataSet: MutableList<Bitmap> = mutableListOf()

                for (detectedObject in detectedObjects) {
                    Log.e(
                        "Search Fragment",
                        "Photo capture success: ${Gson().toJson(detectedObject.labels)}"
                    )
                    var canvas = Canvas(bitmap)
                    canvas.drawRect(detectedObject.boundingBox, paint)
                    bitmap?.let { Canvas(it) }?.apply {
                        canvas
                    }

                    dataSet.add(getBitmap(detectedObject))
                }
                binding.image.setImageBitmap(bitmap)

                val adapter = ImagesRecyclerView(dataSet)
                val layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.rvImages.layoutManager = layoutManager
                binding.rvImages.itemAnimator = DefaultItemAnimator()
                binding.rvImages.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("Search Fragment", "Photo capture success: ${e.localizedMessage}")
            }
    }

    private fun getBitmap(detectedObject: DetectedObject) = Bitmap.createBitmap(
        bitmap,
        (detectedObject.boundingBox.exactCenterX() - (detectedObject.boundingBox.width() / 2)).toInt(),
        (detectedObject.boundingBox.exactCenterY() - (detectedObject.boundingBox.height() / 2)).toInt(),
        detectedObject.boundingBox.width(),
        detectedObject.boundingBox.height()
    )

    private fun drawRect(detectedObject: DetectedObject) {
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 10f
        }

        var canvas = Canvas(bitmap)
        canvas.drawRect(
            detectedObject.boundingBox.left.toFloat(),
            detectedObject.boundingBox.top.toFloat(),
            detectedObject.boundingBox.right.toFloat(),
            detectedObject.boundingBox.bottom.toFloat(),
            paint
        )
        bitmap?.let { Canvas(it) }?.apply {
            canvas
        }

        binding.image.setImageBitmap(bitmap)
    }

    private fun analyzeText(imageProxy: InputImage) {
        // When using Latin script library
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 10f
        }

        recognizer.process(imageProxy)
            .addOnSuccessListener { visionText ->
                for (text in visionText.textBlocks) {
                    Log.e(
                        "Search Fragment",
                        "Analyze Text success: ${text.text} = ${text.boundingBox}"
                    )

                    val chip = Chip(this)
                    chip.text = text.text.trim()
                    chip.setChipBackgroundColorResource(R.color.holo_orange_light)
                    chip.isCloseIconVisible = true
                    chip.setTextColor(getColor(R.color.white))
                    chip.setTextAppearance(R.style.TextAppearance)

                    binding.chipGroup.addView(chip)

                    text.boundingBox?.let { box ->
                        var canvas = Canvas(bitmap)
                        canvas.drawRect(box, paint)
                        bitmap?.let { Canvas(it) }?.apply {
                            canvas
                        }
                    }

                }

                binding.image.setImageBitmap(bitmap)
            }
            .addOnFailureListener { e ->
                Log.e("Search Fragment", "Analyze Text error: ${e.localizedMessage}")
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun imageCustomModel(imageProxy: InputImage) {
        val localModel = LocalModel.Builder()
            .setAssetFilePath("lite_model_object_detection_1.tflite")
            // or .setAbsoluteFilePath(absolute file path to model file)
            // or .setUri(URI to model file)
            .build()

        // Multiple object detection in static images
        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(3)
                .build()

        val objectDetector =
            ObjectDetection.getClient(customObjectDetectorOptions)

        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 10f
        }

        objectDetector
            .process(imageProxy)
            .addOnFailureListener { e ->
                Log.e("Search Fragment", "Object detect failed: ${e.localizedMessage}")
            }
            .addOnSuccessListener { detectedObjects ->
                for (detectedObject in detectedObjects) {
                    Log.e(
                        "Search Fragment",
                        "Photo detect success: ${Gson().toJson(detectedObject)}"
                    )

                    detectedItems = detectedObjects

                    val dataSet: MutableList<Bitmap> = mutableListOf()

                    for (detectedObject in detectedObjects) {
                        Log.e(
                            "Search Fragment",
                            "Photo capture success: ${Gson().toJson(detectedObject.labels)}"
                        )
                        var canvas = Canvas(bitmap)
                        canvas.drawRect(detectedObject.boundingBox, paint)
                        bitmap?.let { Canvas(it) }?.apply {
                            canvas
                        }

                        dataSet.add(getBitmap(detectedObject))


                        val labels = detectedObject.labels
                        for (label in labels) {
                            val text = label.text
                            val chip = Chip(this)
                            chip.text = text.trim()
                            chip.setChipBackgroundColorResource(R.color.holo_orange_light)
                            chip.isCloseIconVisible = true
                            chip.setTextColor(getColor(R.color.white))
                            chip.setTextAppearance(R.style.TextAppearance)

                            binding.chipGroup.addView(chip)
                        }
                    }
                    binding.image.setImageBitmap(bitmap)

                    val adapter = ImagesRecyclerView(dataSet)
                    val layoutManager =
                        LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                    binding.rvImages.layoutManager = layoutManager
                    binding.rvImages.itemAnimator = DefaultItemAnimator()
                    binding.rvImages.adapter = adapter

                }
            }
    }

}