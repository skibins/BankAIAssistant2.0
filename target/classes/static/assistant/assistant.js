// Get references to the chat container and input field
const chatContainer = document.getElementById("chatMessages");
const inputField = document.getElementById("userInput");

// Function to handle sending the message
function sendMessage(messageText) {
    const message = messageText || inputField.value.trim();

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

        // Push new topic to the carousel
        rotateTopics();
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

// Sample new cards that can appear at the bottom
const newTopics = [
    { title: "Update Address", text: "Easily update your mailing address in your profile settings." },
    { title: "Activate New Card", text: "Follow simple steps to activate your new debit or credit card." },
    { title: "Transaction Dispute", text: "Learn how to dispute unauthorized charges quickly." },
];

// Hot topics carousel functionality
function rotateTopics() {
    const wrapper = document.querySelector('.cards-wrapper');
    const cards = wrapper.querySelectorAll('.topic-card');

    if (cards.length === 0) return;

    const cardHeight = cards[0].offsetHeight + 20; // height + margin gap
    wrapper.style.transform = `translateY(-${cardHeight}px)`;

    // Wait for animation to finish
    setTimeout(() => {
        // Move first card to end
        const firstCard = cards[0];
        wrapper.appendChild(firstCard);
        wrapper.style.transition = 'none';
        wrapper.style.transform = 'translateY(0)';

        // Force reflow to apply the "no-transition" state
        void wrapper.offsetWidth;

        // Re-enable transition
        wrapper.style.transition = 'transform 0.5s ease-in-out';

        // Update last card with new random content
        const lastCard = wrapper.lastElementChild;
        const newTopic = newTopics[Math.floor(Math.random() * newTopics.length)];

        lastCard.querySelector('h3').textContent = newTopic.title;
        lastCard.querySelector('p').textContent = newTopic.text;

        // Update data attributes to reflect the new topic
        lastCard.setAttribute('data-title', newTopic.title);
        lastCard.setAttribute('data-description', newTopic.text);
    }, 500);

    attachTopicCardListeners();
}

// Enables communication with Gemini after clicking at the 'hot topic' card
function attachTopicCardListeners() {
    const topicCards = document.querySelectorAll(".topic-card");
    topicCards.forEach(card => {
        card.removeEventListener("click", topicCardClickHandler);
        card.addEventListener("click", topicCardClickHandler);
    });
}

function topicCardClickHandler(event) {
    const card = event.currentTarget;
    const title = card.getAttribute("data-title");
    const description = card.getAttribute("data-description");
    const message = `How to: ${title}, ${description}`;
    sendMessage(message);
}

document.addEventListener("DOMContentLoaded", () => {
    attachTopicCardListeners();
});

