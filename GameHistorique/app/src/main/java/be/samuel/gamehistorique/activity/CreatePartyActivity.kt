package be.samuel.gamehistorique.activity

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.samuel.gamehistorique.databinding.ActivityCreatePartyBinding
import be.samuel.gamehistorique.viewModel.CreatePartyViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
class CreatePartyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding : ActivityCreatePartyBinding
    private val createPartyViewModel : CreatePartyViewModel by viewModels()
    private var pathImage : String? = null
    private var gameSelect : Int = 0

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
        binding = ActivityCreatePartyBinding.inflate(layoutInflater)
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
            Toast.makeText(this@CreatePartyActivity, "photo enregisté", Toast.LENGTH_SHORT).show()
        }, handler)

        binding.pictureGame.surfaceTextureListener = object: TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                openCamera(cameraManager, imageReader)
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

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

        val locationListener = LocationListener { location ->
            val longitude = location.longitude
            val latitude = location.latitude

            binding.longitudeInput.setText(longitude.toString())
            binding.latitudeInput.setText(latitude.toString())
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        binding.buttonBackButton.setOnClickListener{
            cameraDevice.close()
            handler.removeCallbacksAndMessages(null)
            handlerThread.quitSafely()
            backEdit()
        }

        binding.saveButton.setOnClickListener{
            saveParty()
        }

        binding.addGameButton.setOnClickListener {
            startActivity(CreateGameActivity.newInstent(this@CreatePartyActivity))
        }
    }

    companion object{
        fun newInstent(packageContext: Context): Intent {
            return Intent(packageContext, CreatePartyActivity::class.java).apply{

            }
        }
    }

    private fun saveParty(){
        val nameGame = binding.gameNameInput.text.toString()
        val nameParty = binding.namePartyInput.text.toString()
        val timeParty = binding.timePartyInput.text.toString()
        val description = binding.descriptionPartyInput.text.toString()
        val longitude = binding.longitudeInput.text.toString()
        val latitude = binding.latitudeInput.text.toString()
        if(!createPartyViewModel.existGame(nameGame)){
            binding.errorMessageParty.text = "le jeu n'exte pas, vérifie l'orthographe ou créer le jeu"
        }else if(gameSelect >= 0 && nameParty != "" && timeParty != "" && description != "" && pathImage != null && longitude != "" && latitude != ""){
            try{
                createPartyViewModel.createParty(nameGame, nameParty, timeParty.toInt(), description, longitude.toDouble(), latitude.toDouble(), pathImage!!)
                Toast.makeText(this, "partie créé",Toast.LENGTH_LONG).show()
                backEdit()
            }catch (ex : NumberFormatException) {
                binding.errorMessageParty.text = "le format de longitude et/ou latitude ne sont pas correct"
            }

        }else{
            binding.errorMessageParty.text = "veuillez remplir tous les champs"
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        gameSelect = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun backEdit(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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