# 💬 JavaFX Chat Application

#### ummm... this is a simple **JavaFX-based chat application** that supports real-time messaging using **MongoDB** as the backend database.  
 #### It has user authentication, chatroom creation between two users, and  UI rendering with message bubbles.

## 🚀 Features

- 👥 **User List** — Loads all registered users from MongoDB asynchronously.  
- 💬 **Chat Rooms** — chatrooms are created with the combination of the sender and receiver usernames
 ( `spiderman+hulk` is `hulk+spiderman`).  
- 🧩 **Message Bubbles** — Displays messages with timestamps aligned  on sender.  
- 📦 **MongoDB Storage** — Messages and users are stored in a MongoDB database.  
- 🔄 **Asynchronous Operations** — Database calls run on background threads.  
- 🎨 **UI** — Built with JavaFX.  
---
## 🛠️ Project Structure
javachatappp/
├── App.java
├── chatController.java 
├── LoginController.java 
├── Message.java 
├── User.java 
├── textbubble.java
├── usertab.java 
├── databaseConnect.java 
├── resources/
│ ├── chat.fxml 
│ ├── login.fxml
│ └── settings.fxml 
└── README.md 
getuserid is a .txt just incase you want to use their id's 
in creating the chatrooms rathher then the username. 
(should work if you tweak if a litle)
the "settings" page is a work in progress.
best of luck! 
---
## ⚙️ Setup Instructions
###  Prerequisites
- Java 17 or later  
- JavaFX SDK 17+  
- MongoDB installed locally or  cloud (do you)
- Maven 
---
###  Clone the repo with...
```bash
https://github.com/JothamHanson/javachatappp.git
