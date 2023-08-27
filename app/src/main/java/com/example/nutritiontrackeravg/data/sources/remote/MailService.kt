package com.example.nutritiontrackeravg.data.sources.remote

import com.example.nutritiontrackeravg.data.models.MealDto
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailService {
    companion object {
        fun sendEmail(email: String, listMonday: List<MealDto>, listTuesday: List<MealDto>, listFriday: List<MealDto>) {
            // SMTP server configuration
            val properties = Properties().apply {
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "587")
                put("mail.smtp.starttls.enable", "true") // Enable STARTTLS
                put("mail.smtp.auth", "true")
                // Add additional properties as needed (e.g., authentication, SSL/TLS settings)
            }

            // Sender and recipient information
            val senderEmail = "kotlinprobic@gmail.com"
            val senderPassword = "hppaichjwgagwvmy"
            val recipientEmail = email

            val formattedMealsMonday = listMonday.joinToString("\n") { meal ->
                """
            Meal ID: ${meal.id}
            Name: ${meal.name}
            Image: ${meal.img}
            Instructions: ${meal.instructions}
            Link: ${meal.link}
            Category: ${meal.category}
            Date: ${meal.date}
            Type: ${meal.type}
            Calorie Value: ${meal.calValue}
            Ingredients: ${meal.ingredients.entries.joinToString("\n") { (ingredient, quantity) ->
                            "$ingredient: $quantity"
                        }}
            """.trimIndent()
                    }

            val formattedMealsTuesday = listTuesday.joinToString("\n") { meal ->
                """
            Meal ID: ${meal.id}
            Name: ${meal.name}
            Image: ${meal.img}
            Instructions: ${meal.instructions}
            Link: ${meal.link}
            Category: ${meal.category}
            Date: ${meal.date}
            Type: ${meal.type}
            Calorie Value: ${meal.calValue}
            Ingredients: ${meal.ingredients.entries.joinToString("\n") { (ingredient, quantity) ->
                    "$ingredient: $quantity"
                }}
            """.trimIndent()
            }

            val formattedMealsFriday = listFriday.joinToString("\n") { meal ->
                """
            Meal ID: ${meal.id}
            Name: ${meal.name}
            Image: ${meal.img}
            Instructions: ${meal.instructions}
            Link: ${meal.link}
            Category: ${meal.category}
            Date: ${meal.date}
            Type: ${meal.type}
            Calorie Value: ${meal.calValue}
            Ingredients: ${meal.ingredients.entries.joinToString("\n") { (ingredient, quantity) ->
                    "$ingredient: $quantity"
                }}
            """.trimIndent()
            }
            var monText = "Monday:\n"
            if(listMonday.isEmpty()){
                monText = ""
            }
            var tueText = "Tuesday:\n"
            if(listTuesday.isEmpty()){
                tueText = ""
            }
            var friText = "Friday:\n"
            if(listFriday.isEmpty()){
                friText = ""
            }

            // Email content
            val subject = "Your meal plan"
            val body = """
            Dear recipient,
            
            This is your meal plan for the week:
            Current date: ${getCurrentDate()}
            
            $monText
            $formattedMealsMonday
            
            $tueText
            $formattedMealsTuesday
            
            $friText
            $formattedMealsFriday
            
            Regards,
            NutritionTracker
        """.trimIndent()

            // Create a session with the SMTP server
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            try {
                // Create a new email message
                val message = MimeMessage(session)
                message.setFrom(InternetAddress(senderEmail))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                message.subject = subject
                message.setText(body)

                // Send the email
                Transport.send(message)
                println("Email sent successfully.")
            } catch (e: MessagingException) {
                println("Failed to send email. Error: ${e.message}")
            }
        }

        private fun getCurrentDate(): String {
            // Example function to get the current date
            return java.time.LocalDate.now().toString()
        }

        private fun generateRandomNumber(): Int {
            // Example function to generate a random number
            return (0..100).random()
        }
    }
}