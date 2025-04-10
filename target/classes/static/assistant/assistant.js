// Get references to the chat container and input field
const chatContainer = document.getElementById("chatMessages");
const inputField = document.getElementById("userInput");

// Function to handle sending the message
function sendMessage() {
    const message = inputField.value.trim();

    if (message) {
        // Display the user's message in the chat
        addMessage(message, 'user');

        // Send the message to the backend API
        fetch('/assistant/get_response', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: message }) // Sending user input in the expected format
        })
        .then(response => response.json())  // Parse JSON response from the server
        .then(data => {
            if (data.response) {
                // Display the AI's response in the chat
                addMessage(data.response, 'ai');
            } else {
                // Handle missing response gracefully
                addMessage("Error: No response from server.", 'ai');
            }
        })
        .catch(error => {
            // Log and show error if the API call fails
            console.error("Error:", error);
            addMessage("Error: Unable to connect to AI.", 'ai');
        });

        // Clear the input field after sending
        inputField.value = '';
    }
}

// Function to append messages to the chat container
function addMessage(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message`;  // Set class for styling
    messageDiv.innerHTML = text;  // Set the message content
    chatContainer.appendChild(messageDiv);  // Append the message to the container

    // Scroll the chat container to the bottom for new messages
    chatContainer.scrollTo({
        top: chatContainer.scrollHeight,
        behavior: 'smooth'
    });
}

// Event listener for sending message when the Enter key is pressed
inputField.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();  // Prevent the default behavior (form submission)
        sendMessage();  // Call the sendMessage function
    }
});
