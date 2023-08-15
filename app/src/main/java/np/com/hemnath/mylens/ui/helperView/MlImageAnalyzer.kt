package np.com.hemnath.mylens.ui.helperView

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage
class MlImageAnalyzer(val onCompleted: (image: InputImage) -> Unit) :
    ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            onCompleted(image)
        }
    }

}