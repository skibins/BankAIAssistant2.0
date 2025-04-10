// Get references to the menu buttons
const homeButton = document.getElementById("homeButton");
const loginButton = document.getElementById("loginButton");
const servicesButton = document.getElementById("servicesButton");
const aboutButton = document.getElementById("aboutButton");

// Function to redirect the user to a specific page
function redirectTo(pageUrl) {
    window.location.href = pageUrl;
}

// Add event listeners to the buttons if they exist
document.addEventListener("DOMContentLoaded", () => {
    // Check if each button exists and attach event listeners
    if (homeButton) {
        homeButton.addEventListener("click", () => redirectTo('/'));
    }
    if (loginButton) {
        loginButton.addEventListener("click", () => redirectTo('/login'));
    }
    if (servicesButton) {
        servicesButton.addEventListener("click", () => redirectTo('/services'));
    }
    if (aboutButton) {
        aboutButton.addEventListener("click", () => redirectTo('/about'));
    }
});
