
# 💼 BankAIAssistant 2.0

**BankAIAssistant 2.0** is a redesigned version of the original *BankAIAssistant*, now rebuilt using Java and Spring (instead of Python and Flask). This version offers a more scalable and enterprise-friendly architecture while retaining all the intelligent features of the original assistant.

---

## 📘 Program Description

**MyBank AI Assistant** is an intelligent system supporting online banking customer service, integrated with the bank’s website. The application runs in a client-server model and allows users to interact with the chatbot in real time through a web browser.

After logging into their account, users gain access to personalized functions such as:

- checking their balance  
- viewing transaction history  
- making transfers

The bot processes user queries in natural language, analyzes their content, and performs actions based on account data. This allows users to solve many issues that previously required contact with a bank employee, providing a fast, secure, and automated support channel.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java** & **Spring Boot** | Backend logic and web API |
| **HTML & CSS** | Page layout and design |
| **JavaScript** | UI handling and frontend-backend integration |
| **Google GenAI SDK** | Communication with Gemini AI model |
| **JSON** | Local user data and topic storage |

---

## 🎯 Features

- 🔒 **User authentication system** (login, session management)
- 🧠 **AI Assistant powered by Gemini** for natural language conversations
- 📊 **Personalized hot topics** based on user history
- 🏦 **Custom "dummy" banking website** with pages like:
  - Home
  - Services
  - About Us
- 📁 **JSON-based storage** for user data and topics (no database used)

---

## 🧪 Project Purpose

This project was created as a personal learning exercise to:

- Learn **Java and Spring**
- Understand **API integration** and **Gemini AI model usage**
- Explore how AI can support **domain-specific chatbots** (banking in this case)
- Build an **end-to-end web application** combining frontend, backend, and AI

---

## 🖥️ How to Run the Project Locally

### Requirements:
- Java (17+ recommended)
- Maven (you are using `pom.xml`, so the project uses **Maven**)
- IntelliJ IDEA or other IDE with Spring Boot support

### Steps:

#### 1. Clone the repository
 ```bash
git clone https://github.com/your-username/BankAIAssistant2.0.git
cd BankAIAssistant2.0
```
#### 2. Open in IntelliJ IDEA
- Launch IntelliJ IDEA.
- Choose **"Open"** and select the root directory of the project.
- IntelliJ will detect the `pom.xml` file and automatically import the project as a **Maven** project.
- Wait for the indexing and dependency resolution to complete.

#### 3. Gemini API Key Setup
To enable the AI assistant functionality, you must provide your own **Gemini API Key**.

- Visit the official Gemini platform to generate your key: [https://aistudio.google.com/app/apikey](https://aistudio.google.com/app/apikey)
- Once you have the key, create a file named `api_key.txt` in the following directory:
```bash
src/
└── main/
    └── resources/
        └── data/
            └── api_key.txt ← paste your Gemini API key here
```
- The application reads this key at runtime to authenticate requests to the Gemini API.


#### 4. Build and run the project

**Option A – IntelliJ Run Button**
- Navigate to the main class: `BankAIAssistantApplication.java`
- Click the green **Run** button (▶️) next to the class name.

**Option B – Command Line**
```bash
./mvnw spring-boot:run
```

#### 5. Access the application
- Once the application is running, open your web browser.
- Navigate to: [http://localhost:8080](http://localhost:8080)
- You will be taken to the home page of the dummy bank website.

#### 6. Interact with the assistant
- Click on **"Log In"** and use one of the test users stored in the local `data.json` file.
- After logging in, navigate to the **Assistant** page.
- There you can ask the AI assistant natural-language questions related to the bank, such as:
  - `How do I delete my account?`
  - `What services do you offer?`
  - `I want to speak to a human representative.`
  - `Who founded the bank?`
- The assistant will respond in real time, using **Google Gemini** to interpret the message and reply accordingly.

#### 7. Data storage
- All user accounts and their personalized "hot topics" are stored locally in a `data.json` file.
- No external database is used, as the project's goal is to demonstrate integration with AI and backend API design.

---

### 🌐 External Resources

📚 **Gemini API Documentation**: [https://ai.google.dev/](https://ai.google.dev/)

---

### 🧑‍💻 Author’s Note

This was a personal project built from scratch to explore Spring Boot, Java development, and AI integration.  
The focus was not on databases but rather on API functionality, local data handling, and AI responses tailored to the banking/CX domain.

---

### 🖼️ Preview

![MyBank's Assistant Page](README_IMAGE.png?raw=true "README_IMAGE")

---
