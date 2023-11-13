# Project Title

Vending Machine

## Description

* This is a simple three activity java project. The project is Vending machine with some basic operations. Using Jetpack library - Room. Room with data access objects (DAO) allows you to save and access your application local database. 

## Getting Started

### Installing

* You need to download and install [Android studio](https://developer.android.com/studio)

### Executing program

* Open project folder through the IDE.
* Create virtual device with API between 24 and 32.
* Select your virtual device and Run the project.

### How to use

* After the installation is completed the application starts. You can deposit money in order to buy different items from the vending machine, adding new items in the inventory, removing and editing items.

## Additional information

* External data source: 
    1. The external resource for data is loaded from a JSON file only once when the app is opened for first time. To reload the data you need to uninstall and install the app again. 
    2. The JSON file is in the project resource directory ‘…/res/raw’
* CRUD operations:
    1. “ADD PRODUCT” button on the action bar that lead to new activity that can insert new items to the database.
    2. Removing an item is done by swiping left or right on the items.
    3. On each item there is a small black info button that lead to new activity that can update the selected item.
* Vending operations:
    1. Deposit cash: First right button with coin icon. You need to type in the alert dialog the amount of money you want to deposit and click "Deposit" button.
    2. Buying: After depositing cash you can select into an item from the inventory. Below the list with items you can see what you bought.
    3. Take change: Button with the dollar image will return your change.
    4. Reset process: Bottom right button with arrow will reset the process.

## Authors

Boyko Kovachki
