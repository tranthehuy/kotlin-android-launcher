package com.example.mylauncher00h21

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mylauncher00h21.services.Preferences.Companion.savePreferences
import java.io.File

class SettingActivity : AppCompatActivity() {

    private fun getMimeType(context: Context, uri: Uri): String {
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path ?: "")).toString())
        }
        return extension ?: ""
    }

    private fun getFileFromUri(contentResolver: ContentResolver, uri: Uri, directory: File): File {
        val ext = getMimeType(this, uri)
        val file =
            File.createTempFile("background", ".$ext", directory)
        file.outputStream().use {
            contentResolver.openInputStream(uri)?.copyTo(it)
        }

        return file
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener { _ ->
            val ctx = applicationContext
            val pm = ctx.packageManager
            val intent = pm.getLaunchIntentForPackage(ctx.packageName)
            val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
            ctx.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
            finish()
        }

        val btnLock = findViewById<Button>(R.id.btnSetPassword)
        btnLock.setOnClickListener { _ ->
            showNumberInputDialog(this) { enteredNumber ->
                savePreferences(this.applicationContext, "pincode", enteredNumber)

            }
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val file = result.data?.data?.let {
                    getFileFromUri(contentResolver, it, cacheDir)
                }
                Log.d("save file...", file?.path.toString())
                savePreferences(this.applicationContext, "wallpaper", file?.path.toString());
            }
        }

        val btnSetBackground = findViewById<Button>(R.id.btnSetBackground)
        btnSetBackground.setOnClickListener { _ ->
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

            if (intent.resolveActivity(packageManager) != null) {
                resultLauncher.launch(intent)
            }
        }

        val btnChooseMode = findViewById<Button>(R.id.btnChooseMode)
        btnChooseMode.setOnClickListener { _ ->
            val options = arrayOf("Light", "Dark")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose a theme mode")
            builder.setItems(options) { dialog, which ->
                val selectedOption = options[which]
                savePreferences(this.applicationContext, "theme", selectedOption);
            }
            builder.show()
        }

        val btnMaxIcons = findViewById<Button>(R.id.btnMaxIcons)
        btnMaxIcons.setOnClickListener { _ ->
            val options = arrayOf("10", "20", "30")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Max Icon Each Page")
            builder.setItems(options) { dialog, which ->
                val selectedOption = options[which]
                savePreferences(this.applicationContext, "homepage", selectedOption);
            }
            builder.show()
        }
    }

    private fun showNumberInputDialog(context: Context, onNumberEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enter a Number")

        // Set up the input
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            onNumberEntered(input.text.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}