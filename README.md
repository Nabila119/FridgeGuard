﻿# FridgeGuard
  
https://github.com/Nabila119/FridgeGuard/tree/main
FridgeGuard Software Development Report
Introduction
FridgeGuard is an Android application designed to help users keep track of the items in their refrigerator, including their expiration dates. By using barcode scanning and a user-friendly interface, the application ensures that users are aware of when their food items are about to expire, thereby reducing food waste and saving money.
Objective
The primary objective of FridgeGuard is to provide a seamless experience for users to monitor their food inventory. This is achieved through the following key functionalities:
•	Scanning barcodes to retrieve product information.
•	Adding items manually.
•	Displaying a list of items with their respective number of days before expiry.
•	Providing reminders or alerts for items nearing expiration.
•	Allowing users to delete items from the list.
•	Sending notifications if the expiry date is less than 2 days.
User Interface Design
Main Activities and Layouts
1.	Home Activity: The main screen displaying the list of items in the fridge.
o	Layout: activity_home.xml
o	Components:
	Toolbar with title "Fridge"
	RecyclerView to display the list of products
	ImageButton for launching the scan functionality
	ImageButton for adding new items manually
2.	Add Item Activity: The screen where users can manually add a new item.
o	Layout: activity_main.xml
o	Components:
	Toolbar with title "Add Item"
	EditText fields for product name and expiry date
	Buttons to set expiry dates quickly (1 week, 2 weeks, 3 weeks)
	Button to submit the new item
	ImageView for displaying or adding a product image
3.	Scan Activity: The screen that appears after scanning a barcode, displaying the scanned product's information.
o	Layout: activity_scan.xml
o	Components:
	ImageView for displaying the scanned product image
	TextView for showing the product name
	EditText for entering the expiry date
	Buttons for setting quick expiry dates (1 week, 2 weeks, 3 weeks)
	Button to submit the scanned item to the database
Item Layout
•	Item Product Layout: The layout used for displaying each product in the RecyclerView.
o	Layout: item_product.xml
o	Components:
	ImageView for the product image
	TextView for the product name
	TextView for the remaining days until expiration
	Button to delete the product from the list
Core Functionalities
Barcode Scanning The application uses the journeyapps/barcodescanner library for barcode scanning. Upon a successful scan, the product's barcode is used to fetch product information from the Open Food Facts API.
Fetching Product Information The scanned barcode triggers a network request to the Open Food Facts API, which returns a JSON response containing product details such as the product name and image URL. This data is then displayed on the Scan Activity screen.
Database Management A SQLite database is utilized to store product information, including product name, expiry date, and image data. The DatabaseHelper class handles all database operations such as inserting, querying, and deleting products. It also calculates the number of remaining days for expiry by taking the expiry date and subtracting it from the current date.
RecyclerView Adapter The ProductAdapter class manages the RecyclerView that displays the list of products in the fridge. It binds product data to the views and handles item deletion through a custom OnDeleteClickListener interface.
Image Handling Product images are fetched from URLs provided by the Open Food Facts API using the Glide library. These images are then converted to byte arrays for storage in the SQLite database.
Expiry Date Management Users can manually set the expiry date through a DatePicker dialog or use the quick-set buttons for 1, 2, or 3 weeks from the current date. The expiry date is stored in the database and displayed in the list of products.
Implementation Overview
Home Activity
•	Displays the list of products in the fridge
•	Allows users to add new items via manual entry or barcode scan
Add Item Activity
•	Provides a form for users to enter product details manually
•	Includes options for quick setting of expiry dates
Scan Activity
•	Shows the product information retrieved from the barcode scan
•	Allows users to enter the expiry date and save the product to the database
Product Class A simple model class representing a product, including fields for ID, name, remaining days, and image data.
ProductAdapter Class Handles the display of product items in a RecyclerView. Manages item deletion and binds data to the views.
DatabaseHelper Class Manages SQLite database operations, ensuring smooth data storage and retrieval.
ImageDownloader Class Handles downloading images from URLs and converting them to byte arrays for database storage.
NotificationHelper Class Responsible for creating and sending notifications to alert users about products nearing their expiration dates. This ensures users are informed in a timely manner to prevent food waste.
CheckProductsWorker Class Performs periodic background checks to identify products nearing their expiration dates and triggers notifications accordingly. This background service ensures that users are reminded of expiring products without needing to manually check the app.
Database Schema
•	Table: Products_Table
o	Columns: ID (Primary Key), Name, Expiry Date, Image (BLOB), Remaining Days
Challenges and Solutions
Barcode Scanning Integration
•	Challenge: Ensuring accurate and quick barcode scanning.
•	Solution: Implemented the journeyapps/barcodescanner library, known for its reliability and ease of use. For the image of the product, we were getting image URLs but our database column for images was of type BLOB. To handle these two different sources of images, we converted these URLs into BLOB.
API Data Handling
•	Challenge: Parsing and displaying data from the Open Food Facts API, including outdated expiry dates.
•	Solution: Used the Volley library for network requests and Glide for image loading, ensuring efficient data handling. Users are allowed to set the expiry date manually to account for outdated data from the API.
User Interface Design
•	Challenge: Creating a user-friendly and intuitive interface.
•	Solution: Designed clean and simple layouts with ConstraintLayout for flexibility and responsiveness.
Code Maintenance
•	Challenge: Maintaining code quality and version control.
•	Solution: Leveraging GitHub for version control provided a safety net, allowing me to track changes, revert to previous versions, and isolate experimental features. It ensured code stability and facilitated efficient solo development
Future Enhancements
•	Enhanced Search: Allow users to search for items within the app.
•	Multi-Language Support: Add support for multiple languages to cater to a wider audience.
•	Cloud Sync: Implement cloud synchronization for data backup and multi-device access.
Conclusion
FridgeGuard is an efficient and user-friendly application designed to help users manage their fridge inventory and reduce food waste. With its intuitive interface, barcode scanning capabilities, and reliable data handling, FridgeGuard offers a seamless experience for users to keep track of their food items and ensure they are consumed before expiration. The current implementation provides a solid foundation for future enhancements to further improve user engagement and functionality.

                           

