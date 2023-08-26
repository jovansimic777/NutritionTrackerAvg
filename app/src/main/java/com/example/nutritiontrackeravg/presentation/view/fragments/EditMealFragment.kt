package com.example.nutritiontrackeravg.presentation.view.fragments

import com.bumptech.glide.Glide
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.nutritiontrackeravg.R
import com.example.nutritiontrackeravg.data.models.MealDto
import com.example.nutritiontrackeravg.data.sources.remote.converters.MealDtoConverter
import com.example.nutritiontrackeravg.databinding.FragmentEditMealBinding
import com.example.nutritiontrackeravg.presentation.view.states.SingleMealState
import com.example.nutritiontrackeravg.presentation.view.viewmodels.MealEntityViewModel
import com.example.nutritiontrackeravg.util.Constants
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Date

class EditMealFragment: Fragment(R.layout.fragment_edit_meal) {
    private val mealEntityViewModel: MealEntityViewModel by activityViewModel<MealEntityViewModel>()

    private var _binding: FragmentEditMealBinding? = null
    private val binding get() = _binding!!

    private var date: Date = Date()
    private lateinit var imgPath: String

    private lateinit var nameTV: TextView
    private lateinit var areaTV: TextView
    private lateinit var instructionsTV: TextView
    private lateinit var linkTV: TextView
    private lateinit var typeET: EditText
    private lateinit var categoryTV: TextView
    private lateinit var editBT: Button
    private lateinit var deleteBT: Button
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
        _binding = FragmentEditMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initUi()
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
        editBT = binding.edit
        deleteBT = binding.delete
        calValueTV = binding.calValueTV
    }


    private fun initListeners(){
        photoBT.setOnClickListener(View.OnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, Constants.REQUEST_IMAGE_CAPTURE)
        })

        binding.datePicker.setOnClickListener {
            openDatePicker()
        }

        // Convert MealDto to MealEntity and update it in the database
        editBT.setOnClickListener {
            if (mealEntityViewModel.meal.value is SingleMealState.Success) {
                val mealDto = (mealEntityViewModel.meal.value as SingleMealState.Success).meal
                val mealEntity = MealDtoConverter.mapMealDtoToMealEntity(mealDto)
                mealEntityViewModel.editMealInDB(mealEntity)
                Toast.makeText(requireContext().applicationContext, "Meal change saved!", Toast.LENGTH_SHORT).show()
                mealEntityViewModel.getAllMeals()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        deleteBT.setOnClickListener {
            if (mealEntityViewModel.meal.value is SingleMealState.Success) {
                val mealDto = (mealEntityViewModel.meal.value as SingleMealState.Success).meal
                mealEntityViewModel.deleteMealFromDB(mealDto.id)
                Toast.makeText(requireContext().applicationContext, "Meal deleted!", Toast.LENGTH_SHORT).show()
                mealEntityViewModel.getAllMeals()
                //TODO ne redi
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun initObservers() {
        mealEntityViewModel.meal.observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
    }

    private fun renderState(state: SingleMealState) {
        when (state) {
            is SingleMealState.Success -> {
                showLoadingState(false)
                renderData(state.meal)
            }
            is SingleMealState.Error -> {
                showLoadingState(false)
                renderData(null)
                Toast.makeText(requireContext().applicationContext, state.message, Toast.LENGTH_SHORT).show()
            }
            is SingleMealState.Loading -> {
                showLoadingState(true)
            }
        }
    }

    private fun renderData(meal: MealDto?) {
        if (meal != null) {
            initDatePicker()
            dateButton.setText(meal.date.toString())
            nameTV.setText(meal.name)
            instructionsTV.setText(meal.instructions)
            linkTV.setText(meal.link)
            typeET.setText(meal.type)
            categoryTV.setText(meal.category)
            //TODO: copy how to show img
            if(meal.img.startsWith("http"))
                DownloadImageFromInternet(photoIV).execute(meal.img)
            else  {
                Glide.with(requireContext().applicationContext)
                    .load(File(meal.img))
                    .into(photoIV)
            }
        }
    }

    private fun showLoadingState(loading: Boolean) {
        binding.loadingPb.isVisible = loading
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
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
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
        var month: Int = cal.get(Calendar.MONTH)
        month = month + 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        date= Date(year, month, day)
        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month = month + 1
                date = Date(year, month, day)
                val date = makeDateString(day, month, year)
                dateButton.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
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