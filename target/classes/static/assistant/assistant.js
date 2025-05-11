// Get references to the chat container and input field
const chatContainer = document.getElementById("chatMessages");
const inputField = document.getElementById("userInput");

// Main function to send the message
async function sendMessage(messageText) {
    const message = messageText || inputField.value.trim();

    if (!message) return;

    addMessage(message, 'user');

    // Clear the input field
    inputField.value = '';

    // Send the message to the AI chat
    await sendChatMessage(message);

    // Fetch a new topic and update the carousel
    try {
        const newTopic = await fetchNewTopic(message);
        if (newTopic && newTopic.title && newTopic.text) {
            rotateTopics(newTopic); // pass the new topic to the carousel rotation
        }
    } catch (error) {
        console.error("Failed to fetch topic:", error);
    }
}

// Helper function – sends message to AI chat
async function sendChatMessage(message) {
    try {
        const response = await fetch('/assistant/get_response', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message })
        });

        const data = await response.json();

        if (data.response) {
            addMessage(data.response, 'ai');
        } else {
            addMessage("Error: No response from server.", 'ai');
        }
    } catch (error) {
        console.error("Chat API error:", error);
        addMessage("Error: Unable to connect to AI.", 'ai');
    }
}

// Helper function – fetches a new topic from the API
async function fetchNewTopic(message) {
    const response = await fetch('/assistant/get_topic', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message })
    });

    const data = await response.json();

    // Expected structure: { title: "New Title", text: "New text" }
    return data;
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

// Hot topics carousel functionality
function rotateTopics(newTopic) {
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
    const message = `Please explain: ${title}, ${description}`;
    sendMessage(message);
}

document.addEventListener("DOMContentLoaded", () => {
    attachTopicCardListeners();
});

