package com.example.nutritiontrackeravg.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.data.models.entities.MealEntity
import com.example.nutritiontrackeravg.databinding.FragmentSaveMealBinding
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealDetailedViewModel
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealEntityViewModel
import com.example.nutritiontrackeravg.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Date


class SaveMealFragment: Fragment(R.layout.fragment_save_meal) {
    private val mealDetailedVM: MealDetailedViewModel by activityViewModel<MealDetailedViewModel>()
    private val mealEntityViewModel: MealEntityViewModel by activityViewModel<MealEntityViewModel>()


    private var _binding: FragmentSaveMealBinding? = null
    private val binding get() = _binding!!

    private var date:Date= Date()
    private lateinit var imgPath: String

    private lateinit var nameTV: TextView
    private lateinit var areaTV: TextView
    private lateinit var instructionsTV: TextView
    private lateinit var linkTV: TextView
    private lateinit var typeET: EditText
    private lateinit var categoryTV: TextView
    private lateinit var saveBT: Button
    private lateinit var quitBT: Button
    private lateinit var photoIV: ImageView
    private lateinit var photoBT: Button
    private lateinit var dateButton: Button
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var calValueTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
        initValues()
        initListeners()
        initObservers()
    }

    private fun initUi() {
        dateButton = binding.datePicker
        photoIV = binding.photo
        photoBT = binding.photoBT
        nameTV = binding.name
        areaTV = binding.area
        instructionsTV = binding.instructions
        linkTV = binding.link
        typeET = binding.type
        categoryTV = binding.category
        saveBT = binding.save
        quitBT = binding.quit
        calValueTV = binding.calValueTV
    }

    private fun initValues(){
        initDatePicker()

        dateButton.setText(getTodaysDate())

        var meal = mealDetailedVM.getMealDetailed()
        if (meal != null) {
            nameTV.setText(meal.name)
            areaTV.setText(meal.area)
            instructionsTV.setText(meal.instructions)
            categoryTV.setText(meal.category)
            linkTV.setText(meal.link)
            imgPath = meal.mealThumb?:"Not available"
            calValueTV.setText(meal.calValue)

            DownloadImageFromInternet(photoIV).execute(meal.mealThumb)
        }
    }

    private fun initListeners(){
        photoBT.setOnClickListener(View.OnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        })

        binding.datePicker.setOnClickListener {
            openDatePicker()
//            Timber.e("Value of dateButton: "+ dateButton.text)
        }

        //TODO: Save ingredients to database
        saveBT.setOnClickListener {
            mealDetailedVM.getMealDetailed()?.ingredients?.let { it1 ->
                mealDetailedVM.getMealDetailed()?.id?.let { it2 ->
                    MealEntity(
                        name = nameTV.text.toString(),
//                        ingredients = it1,
                        instructions = instructionsTV.text.toString(),
                        link = linkTV.text.toString(),
                        category = categoryTV.text.toString(),
                        type = typeET.text.toString(),
                        img = imgPath,
                        date = date,
                        id = it2,
                        calValue = calValueTV.text.toString()
                    )
                }
            }?.let { it2 -> mealDetailedVM.saveMealToDB(it2) }

            Toast.makeText(requireContext().applicationContext, "Meal saved!", Toast.LENGTH_SHORT).show()
//            Timber.e("Picture saved with path: $imgPath")

            //TODO mozda je prerano?
            mealEntityViewModel.getAllMeals()

            requireActivity().supportFragmentManager.popBackStack()
            requireActivity().supportFragmentManager.popBackStack()
        }

        quitBT.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Quit")
            builder.setMessage("Are you sure you want to quit?")
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                requireActivity().supportFragmentManager.popBackStack()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                Toast.makeText(
                    requireContext().applicationContext,
                    "Keep grinding",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }



    private fun initObservers() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Image download
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(requireContext().applicationContext, "Please wait, it may take a few minute...",     Toast.LENGTH_SHORT).show()
        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }

    // Image capture
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoIV.setImageBitmap(imageBitmap)

            // Save the image to the local storage
            val savedImagePath = saveImageToStorage(imageBitmap)

            // Save the path to the image in the database
//            mealDetailedVM.setMealImageFilePath(savedImagePath)
            imgPath= savedImagePath
        }
    }

    private fun saveImageToStorage(image: Bitmap): String {
        val storageDir = requireContext().getExternalFilesDir(null)
        val imageFile = File.createTempFile(
            "meal_image",
            ".jpg",
            storageDir
        )
        val outputStream = FileOutputStream(imageFile)
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return imageFile.absolutePath
    }

    //   Date Picker
    private fun getTodaysDate(): String? {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH-1)
        month = month+1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        date= Date(year, month, day)
        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month = month + 1
                date= Date(year, month, day)
                val date = makeDateString(day, month, year)
                dateButton.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
//        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"

        //default should never happen
    }

    fun openDatePicker() {
        datePickerDialog.show()
    }

}