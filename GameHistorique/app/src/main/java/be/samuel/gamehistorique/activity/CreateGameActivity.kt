package be.samuel.gamehistorique.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.samuel.gamehistorique.databinding.ActivityCreateGameBinding
import be.samuel.gamehistorique.viewModel.CreateGameViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateGameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateGameBinding
    private val createGameViewModel : CreateGameViewModel by viewModels()
    private var pathImage : String? = null

    lateinit var currentPhotoPath: String
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var builder: CaptureRequest.Builder

    private val captureStateCallback =  object: CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraCaptureSession = session
            cameraCaptureSession.setRepeatingRequest(builder.build(), null, handler)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        handlerThread = HandlerThread("imageThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)

        val imageReader = ImageReader.newInstance(200,200, ImageFormat.JPEG,4)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader?.acquireLatestImage()
            val buffer = image!!.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            val opStream = FileOutputStream(createImageFile())
            opStream.write(bytes)

            opStream.close()
            image.close()
            Toast.makeText(this@CreateGameActivity, "photo enregistré", Toast.LENGTH_SHORT).show()
        }, handler)

        binding.pictureGame.surfaceTextureListener = object: TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture,width: Int,height: Int) {
                openCamera(cameraManager, imageReader)
            }

            override fun onSurfaceTextureSizeChanged( surface: SurfaceTexture, width: Int,height: Int) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                return Unit
            }

        }

        binding.captureButton.setOnClickListener{
            val orientations : SparseIntArray = SparseIntArray(4).apply {
                append(Surface.ROTATION_0, 0)
                append(Surface.ROTATION_90, 90)
                append(Surface.ROTATION_180, 180)
                append(Surface.ROTATION_270, 270)
            }
            builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            val rotation = windowManager.defaultDisplay.rotation
            builder.set(CaptureRequest.JPEG_ORIENTATION, orientations.get(rotation))

            builder.addTarget(imageReader.surface)
            cameraCaptureSession.capture(builder.build(), object: CameraCaptureSession.CaptureCallback() {

            }, null)
        }

        binding.buttonBackButton.setOnClickListener{
            backEdit()
        }

        binding.saveButton.setOnClickListener{
            saveGame()
        }
    }
    private fun saveGame(){
        val nameGame = binding.nameGameInput.text.toString()
        val numberPlayer = binding.numberPlayerInput.text.toString()
        val timeGame = binding.timeGameInput.text.toString()
        val description = binding.descriptionGame.text.toString()

        if(nameGame != "" && numberPlayer != "" && timeGame != "" && description != "" && pathImage != null){
            createGameViewModel.createGame(nameGame, description, numberPlayer.toInt(), timeGame.toInt(), pathImage!!)
            Toast.makeText(this, "jeu créé",Toast.LENGTH_LONG).show()
            backEdit()
        }else{
            binding.errorMessage.text = "veuillez remplir tous les champs"
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(cameraManager: CameraManager, imageReader: ImageReader){
        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                val surface = Surface(binding.pictureGame.surfaceTexture)
                builder.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), captureStateCallback, handler)
            }

            override fun onDisconnected(camera: CameraDevice) {

            }

            override fun onError(camera: CameraDevice, error: Int) {

            }

        }, handler)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            pathImage = absolutePath
            currentPhotoPath = absolutePath
        }
    }

    private fun backEdit(){
        val intent = Intent(this, CreatePartyActivity::class.java)
        startActivity(intent)
    }

    companion object{
        fun newInstent(packageContext: Context): Intent {
            return Intent(packageContext, CreateGameActivity::class.java).apply{

            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraDevice.close()
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }
}